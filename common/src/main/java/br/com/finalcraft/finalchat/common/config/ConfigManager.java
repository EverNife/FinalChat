package br.com.finalcraft.finalchat.config;

import br.com.finalcraft.evernifecore.config.Config;
import br.com.finalcraft.evernifecore.config.playerdata.PlayerController;
import br.com.finalcraft.evernifecore.locale.FCLocaleManager;
import br.com.finalcraft.finalchat.config.data.FancyPlayerData;
import br.com.finalcraft.finalchat.config.fancychat.FancyChannelController;
import br.com.finalcraft.finalchat.config.fancychat.FancyTag;
import br.com.finalcraft.finalchat.config.fancychat.TellTag;
import br.com.finalcraft.finalchat.messages.FChatMessages;
import br.com.finalcraft.finalchat.util.ChannelManager;
import br.com.finalcraft.finalchat.util.IgnoreUtil;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class ConfigManager {

    public static Config mainConfig;
    public static Config dataStore;
    public static Config customTags;

    public static Config getMainConfig(){
        return mainConfig;
    }
    public static Config getDataStore(){
        return dataStore;
    }
    public static Config getCustomTags() {
        return customTags;
    }

    public static void initialize(JavaPlugin instance){
        mainConfig  = new Config(instance,"config.yml"      ,false);
        dataStore   = new Config(instance,"DataStore.yml"      ,false);
        customTags  = new Config(instance,"CustomTags.yml"      ,false);

        setDefaultValues();

        FancyTag.initialize();                      //Ler Tags
        TellTag.initialize();                       //Ler TellTag
        FancyChannelController.initialize();        //Ler Canais
        ChannelManager.refresh();                   //Carregar ChannelManager
        PlayerController.clearPDSections(FancyPlayerData.class);    //Erase previous Lock channels
        try {
            IgnoreUtil.initialize();                //Carregar ignores
        }catch (Exception e){
            e.printStackTrace();
        }
        FCLocaleManager.loadLocale(instance, FChatMessages.class);
    }

    private static final int CURRENT_VERSION = 3;
    private static void setDefaultValues(){
        if (ConfigManager.getMainConfig().getInt("ConfigVersion",0) != CURRENT_VERSION){
            ConfigManager.getMainConfig().setDefaultValue("ConfigVersion",CURRENT_VERSION);

            ConfigManager.getMainConfig().setDefaultValue("Settings.globalChannelName","Global");
            ConfigManager.getMainConfig().setDefaultValue("Settings.defaultChannelName","Global");

            ConfigManager.getMainConfig().setDefaultValue("TellTag.sender-format"," {sender} &b-> &r{receiver}&r > &f");
            ConfigManager.getMainConfig().setDefaultValue("TellTag.receiver-format"," {sender} &c-> &r{receiver}&r > &f");
            ConfigManager.getMainConfig().setDefaultValue("TellTag.hover-messages", Arrays.asList("&3Isso é uma mensagem privada!","This is a private message"));

            ConfigManager.getMainConfig().setDefaultValue("TagFormats.ch-global.format","&7[&aG&7]");
            ConfigManager.getMainConfig().setDefaultValue("TagFormats.ch-global.run-command","/ch g");
            ConfigManager.getMainConfig().setDefaultValue("TagFormats.ch-global.hover-messages",Arrays.asList("&3Channel: &a&oGlobal","&bCliquei aqui para travar nesse canal de mensagem"));

            ConfigManager.getMainConfig().setDefaultValue("TagFormats.ch-local.format","&7[&eL&7]");
            ConfigManager.getMainConfig().setDefaultValue("TagFormats.ch-local.run-command","/ch l");
            ConfigManager.getMainConfig().setDefaultValue("TagFormats.ch-local.hover-messages",Arrays.asList("&3Channel: &a&oLocal","&bCliquei aqui para travar nesse canal de mensagem"));

            ConfigManager.getMainConfig().setDefaultValue("TagFormats.nickname.format","{player}");
            ConfigManager.getMainConfig().setDefaultValue("TagFormats.nickname.hover-messages",Arrays.asList("Essa é a linha um do HoverMessage","Essa é a linha dois :D"));
            ConfigManager.getMainConfig().setDefaultValue("TagFormats.nickname.run-command","/info {player}");

            ConfigManager.getMainConfig().setDefaultValue("TagFormats.global-premes.format","&r > {msg}");
            ConfigManager.getMainConfig().setDefaultValue("TagFormats.local-premes.format","&r > &6{msg}");

            ConfigManager.getMainConfig().setDefaultValue("TagFormats.factions-prefix.format","{faction-name}");
            ConfigManager.getMainConfig().setDefaultValue("TagFormats.factions-prefix.run-command","/f");
            ConfigManager.getMainConfig().setDefaultValue("TagFormats.factions-prefix.hover-messages",Arrays.asList("&7Faction Name: &e{faction-name}","&7Faction MOTD: &e{faction-motd}","&7Faction KDR: &e{faction-kdr}"));

            ConfigManager.getMainConfig().setDefaultValue("ChannelFormats.Global.alias","g");
            ConfigManager.getMainConfig().setDefaultValue("ChannelFormats.Global.distance",-1);
            ConfigManager.getMainConfig().setDefaultValue("ChannelFormats.Global.tag-builder","ch-global,nickname,global-premes");
            ConfigManager.getMainConfig().setDefaultValue("ChannelFormats.Global.permission","");

            ConfigManager.getMainConfig().setDefaultValue("ChannelFormats.Local.alias","l");
            ConfigManager.getMainConfig().setDefaultValue("ChannelFormats.Local.distance",150);
            ConfigManager.getMainConfig().setDefaultValue("ChannelFormats.Local.tag-builder","ch-local,nickname,local-premes");
            ConfigManager.getMainConfig().setDefaultValue("ChannelFormats.Local.permission","");

            ConfigManager.getMainConfig().save();
        }
    }

}
