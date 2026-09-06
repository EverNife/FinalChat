package br.com.finalcraft.finalchat.minecraft;

import br.com.finalcraft.evernifecore.ecplugin.annotations.ECPlugin;
import br.com.finalcraft.evernifecore.listeners.base.ECListener;
import br.com.finalcraft.evernifecore.minecraft.ecplugin.ECBukkitPlugin;
import br.com.finalcraft.finalchat.common.FinalChat;
import br.com.finalcraft.finalchat.common.platform.ChatPlatformBridge;
import br.com.finalcraft.finalchat.minecraft.integration.EssentialsIgnoreIntegration;
import br.com.finalcraft.finalchat.minecraft.listener.FancyChatListener;
import br.com.finalcraft.finalchat.minecraft.platform.BukkitChatPlatformBridge;

/**
 * Bukkit entry point. The shared wiring lives in {@link FinalChat} (common) - the same
 * phases the Hytale entry point runs; only the Bukkit-specific extras live here.
 */
@ECPlugin
public class FinalChatBukkitPlugin extends ECBukkitPlugin implements FinalChat {

    @Override
    public void onECPluginEnablePre() {
        //Installed before anything renders a line: the shared code asks the bridge for the mention
        //sound and for vanish-aware visibility from its very first message.
        ChatPlatformBridge.use(new BukkitChatPlatformBridge());
    }

    @Override
    public void onECPluginEnablePost() {
        ECListener.register(getPluginData(), FancyChatListener.class);
        EssentialsIgnoreIntegration.install(getPluginData());
    }

    @Override
    public void onECPluginReload() {
        FinalChat.super.onECPluginReload();
        //Idempotent, and it also picks up an Essentials that was installed after boot.
        EssentialsIgnoreIntegration.install(getPluginData());
    }

    @Override
    public void onECPluginShutdownPre() {
        EssentialsIgnoreIntegration.uninstall();
        FinalChat.super.onECPluginShutdownPre();
    }
}
