package br.com.finalcraft.finalchat.config.fancychat;

import br.com.finalcraft.evernifecore.commands.finalcmd.FinalCMDManager;
import br.com.finalcraft.finalchat.FinalChat;
import br.com.finalcraft.finalchat.config.ConfigManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FancyChannelController {

    public static FancyChannel GLOBAL_CHANNEL;
    public static FancyChannel DEFAULT_CHANNEL = null;

    public static String globalChannelName;
    public static String defaultChannelName;

    public static Map<String, FancyChannel> mapOfFancyChannels = new HashMap<>();

    public static void initialize(){

        for (FancyChannel oldChannel : mapOfFancyChannels.values()) {
            FinalCMDManager.unregisterCommand(oldChannel.getAlias());
        }

        mapOfFancyChannels.clear();

        globalChannelName   = ConfigManager.getMainConfig().getString("Settings.globalChannelName");
        defaultChannelName  = ConfigManager.getMainConfig().getString("Settings.defaultChannelName");

        for (String fancyChannelName : ConfigManager.getMainConfig().getKeys("ChannelFormats")){
            FancyChannel fancyChannel = new FancyChannel(fancyChannelName);
            mapOfFancyChannels.put(fancyChannelName,fancyChannel);
            if (fancyChannelName.equalsIgnoreCase(globalChannelName)){
                GLOBAL_CHANNEL = fancyChannel;
            }
            if (fancyChannelName.equalsIgnoreCase(defaultChannelName)){
                DEFAULT_CHANNEL = fancyChannel;
            }
        }

        if (GLOBAL_CHANNEL == null || DEFAULT_CHANNEL == null){
            FinalChat.info("[WARNING] Meu consagrado, você setou um canal default/global que não existe!");
        }

        FinalChat.info("§aFinished Loading " + mapOfFancyChannels.size() + " FancyChannels!");
    }

    public static Collection<FancyChannel> getAllChannels(){
        return mapOfFancyChannels.values();
    }

    public static FancyChannel getFancyChannel(String name){
        for (FancyChannel fancyChannel : getAllChannels()){
            if (fancyChannel.getName().equalsIgnoreCase(name)){
                return fancyChannel;
            }
        }
        return null;
    }
}
