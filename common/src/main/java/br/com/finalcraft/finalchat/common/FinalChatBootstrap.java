package br.com.finalcraft.finalchat.common;

import br.com.finalcraft.evernifecore.EverNifeCore;
import br.com.finalcraft.evernifecore.api.events.player.ECPlayerFullyLoggedInEvent;
import br.com.finalcraft.evernifecore.api.events.player.ECPlayerQuitEvent;
import br.com.finalcraft.evernifecore.ecplugin.ECBootstrap;
import br.com.finalcraft.evernifecore.ecplugin.ECPluginData;
import br.com.finalcraft.evernifecore.ecplugin.IECPluginBootstrap;
import br.com.finalcraft.evernifecore.eventbus.ECSubscribeOptions;
import br.com.finalcraft.evernifecore.playerdata.PDSectionConfiguration;
import br.com.finalcraft.evernifecore.playerdata.PlayerController;
import br.com.finalcraft.evernifecore.playerdata.storage.SectionLifecycle;
import br.com.finalcraft.finalchat.common.commands.CommandRegisterer;
import br.com.finalcraft.finalchat.common.config.ConfigManager;
import br.com.finalcraft.finalchat.common.config.data.FancyPlayerData;
import br.com.finalcraft.finalchat.common.integration.builtin.DefaultParser;
import br.com.finalcraft.finalchat.common.platform.ChatPlatformBridge;
import br.com.finalcraft.finalchat.common.util.ChannelManager;
import br.com.finalcraft.finalchat.common.util.messages.SpyMessage;

/**
 * The platform-agnostic bootstrap: every enable phase both entry points share lives here, once.
 * Each platform main class implements this on top of its platform base class and adds its own
 * extras in {@code onECPluginEnablePost()}.
 */
public interface FinalChatBootstrap extends IECPluginBootstrap {

    ECBootstrap<FinalChatBootstrap> INSTANCE = ECBootstrap.of(FinalChatBootstrap.class);

    /** The plugin running on this server, whatever the platform, or {@code null} while there is none. */
    static FinalChatBootstrap get() {
        return INSTANCE.get();
    }

    /**
     * Mirrors a delivered chat line into the console. Old servers cannot print colour codes, so the
     * platform decides what the line looks like there.
     */
    static void chatLog(String line) {
        get().getLog().info("[ChatLog] {}", ChatPlatformBridge.get().forConsoleLog(line));
    }

    @Override
    default void onECPluginEnable() {
        ECPluginData pd = getPluginData();

        getLog().info("Loading up configurations...");
        ConfigManager.initialize(pd);

        getLog().info("Registering PlayerData...");
        //RESIDENT because nothing in this section is persisted: a released cell comes back blank and
        //the player silently loses the channel they locked, with no error anywhere.
        PlayerController.registerPDSectionCfg(PDSectionConfiguration
                .builder(pd, FancyPlayerData.class, "chatsession")
                .lifecycle(SectionLifecycle.RESIDENT)
                .build());

        getLog().info("Registering commands...");
        CommandRegisterer.registerCommands(pd, true);

        getLog().info("Subscribing on the event bus...");
        subscribeToTheBus(pd);

        DefaultParser.initialize();
    }

    /**
     * Channel membership follows login and quit, and both events are platform-agnostic - so the
     * seating is taken here rather than in a platform listener, and every platform gets it.
     * Subscriptions are taken in this plugin's name, so the default pre-shutdown drains them.
     */
    default void subscribeToTheBus(ECPluginData pd) {
        EverNifeCore.getEventBus().subscribe(ECPlayerFullyLoggedInEvent.class,
                ECSubscribeOptions.ownedBy(pd),
                event -> ChannelManager.playerJoined(event.getPlayer()));

        EverNifeCore.getEventBus().subscribe(ECPlayerQuitEvent.class,
                ECSubscribeOptions.ownedBy(pd),
                event -> {
                    ChannelManager.playerLeaved(event.getPlayer());
                    SpyMessage.changeSpyState(event.getPlayer(), "", false);
                });
    }

    @Override
    default void onECPluginShutdown() {
        DefaultParser.shutdown();
        ChatPlatformBridge.use(null);
    }

    @Override
    default void onECPluginReload() {
        ConfigManager.reload();
        CommandRegisterer.registerCommands(getPluginData(), false);
    }
}
