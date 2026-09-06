package br.com.finalcraft.finalchat.common.config;

import br.com.finalcraft.evernifecore.config.ConfigFactory;
import br.com.finalcraft.evernifecore.ecplugin.ECPluginData;
import br.com.finalcraft.evernifecore.locale.FCLocaleManager;
import br.com.finalcraft.everyconfig.config.Config;
import br.com.finalcraft.finalchat.common.FinalChatBootstrap;
import br.com.finalcraft.finalchat.common.commands.CMDMuteAll;
import br.com.finalcraft.finalchat.common.commands.CMDTell;
import br.com.finalcraft.finalchat.common.commands.CoreCommand;
import br.com.finalcraft.finalchat.common.commands.argparser.ArgParserFancyChannel;
import br.com.finalcraft.finalchat.common.config.fancychat.FancyChannelController;
import br.com.finalcraft.finalchat.common.config.fancychat.FancyTag;
import br.com.finalcraft.finalchat.common.config.fancychat.TellTag;
import br.com.finalcraft.finalchat.common.messages.FChatMessages;
import br.com.finalcraft.finalchat.common.util.ChannelManager;
import br.com.finalcraft.finalchat.common.util.IgnoreUtil;

import java.util.Arrays;

/**
 * Owns the plugin's config files and the order they load in. Called from the enable AND from the
 * reload hook, so everything here has to be safe to run twice.
 */
public class ConfigManager {

    public static Config mainConfig;
    public static Config dataStore;

    public static Config getMainConfig() {
        return mainConfig;
    }

    public static Config getDataStore() {
        return dataStore;
    }

    public static void initialize(ECPluginData plugin) {
        mainConfig  = ConfigFactory.open(plugin, "config.yml");
        dataStore   = ConfigFactory.open(plugin, "DataStore.yml");

        loadState();

        //Every holder of @FCLocale, including the ArgParser the framework also scans on registration:
        //a holder nothing lists never reaches lang_XX.yml, and its field stays null.
        FCLocaleManager.loadLocale(plugin,
                FChatMessages.class,
                CoreCommand.class,
                CMDMuteAll.class,
                CMDTell.class,
                ArgParserFancyChannel.class);
    }

    public static void reload() {
        mainConfig.reload();
        dataStore.reload();
        loadState();
    }

    private static void loadState() {
        seedDefaults();

        FancyTag.initialize();                  //Read the tags
        TellTag.initialize();                   //Read the TellTag
        FancyChannelController.initialize();    //Read the channels
        ChannelManager.refresh();               //Seat everyone who is online into them

        try {
            IgnoreUtil.initialize();            //Read the ignore lists
        } catch (Exception e) {
            FinalChatBootstrap.get().getLog().warning("Could not read the ignore lists from DataStore.yml. {}", e);
        }
    }

    /**
     * Seeds whatever config.yml is missing. There is no version gate: {@code getOrSetValueIfAbsent}
     * only writes keys that are not there, so an operator's edits and additions survive untouched.
     */
    private static void seedDefaults() {
        mainConfig.getOrSetValueIfAbsent("Settings.globalChannelName", "Global");
        mainConfig.getOrSetValueIfAbsent("Settings.defaultChannelName", "Global");

        mainConfig.getOrSetValueIfAbsent("TellTag.sender-format", " {sender} &b-> &r{receiver}&r > &f");
        mainConfig.getOrSetValueIfAbsent("TellTag.receiver-format", " {sender} &c-> &r{receiver}&r > &f");
        mainConfig.getOrSetValueIfAbsent("TellTag.hover-messages", Arrays.asList("&3Isso é uma mensagem privada!", "This is a private message"));

        mainConfig.getOrSetValueIfAbsent("TagFormats.ch-global.format", "&7[&aG&7]");
        mainConfig.getOrSetValueIfAbsent("TagFormats.ch-global.run-command", "/ch g");
        mainConfig.getOrSetValueIfAbsent("TagFormats.ch-global.hover-messages", Arrays.asList("&3Channel: &a&oGlobal", "&bCliquei aqui para travar nesse canal de mensagem"));

        mainConfig.getOrSetValueIfAbsent("TagFormats.ch-local.format", "&7[&eL&7]");
        mainConfig.getOrSetValueIfAbsent("TagFormats.ch-local.run-command", "/ch l");
        mainConfig.getOrSetValueIfAbsent("TagFormats.ch-local.hover-messages", Arrays.asList("&3Channel: &a&oLocal", "&bCliquei aqui para travar nesse canal de mensagem"));

        mainConfig.getOrSetValueIfAbsent("TagFormats.nickname.format", "{player}");
        mainConfig.getOrSetValueIfAbsent("TagFormats.nickname.hover-messages", Arrays.asList("Essa é a linha um do HoverMessage", "Essa é a linha dois :D"));
        mainConfig.getOrSetValueIfAbsent("TagFormats.nickname.run-command", "/info {player}");

        mainConfig.getOrSetValueIfAbsent("TagFormats.global-premes.format", "&r > {msg}");
        mainConfig.getOrSetValueIfAbsent("TagFormats.local-premes.format", "&r > &6{msg}");

        mainConfig.getOrSetValueIfAbsent("ChannelFormats.Global.alias", "g");
        mainConfig.getOrSetValueIfAbsent("ChannelFormats.Global.distance", -1);
        mainConfig.getOrSetValueIfAbsent("ChannelFormats.Global.tag-builder", "ch-global,nickname,global-premes");
        mainConfig.getOrSetValueIfAbsent("ChannelFormats.Global.permission", "");

        mainConfig.getOrSetValueIfAbsent("ChannelFormats.Local.alias", "l");
        mainConfig.getOrSetValueIfAbsent("ChannelFormats.Local.distance", 150);
        mainConfig.getOrSetValueIfAbsent("ChannelFormats.Local.tag-builder", "ch-local,nickname,local-premes");
        mainConfig.getOrSetValueIfAbsent("ChannelFormats.Local.permission", "");

        mainConfig.saveIfNewSeededDefaults();
    }

}
