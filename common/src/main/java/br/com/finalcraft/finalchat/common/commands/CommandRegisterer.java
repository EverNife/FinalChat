package br.com.finalcraft.finalchat.commands;

import br.com.finalcraft.evernifecore.commands.finalcmd.FinalCMDManager;
import br.com.finalcraft.evernifecore.commands.finalcmd.argument.ArgParserManager;
import br.com.finalcraft.finalchat.commands.argparser.ArgParserFancyChannel;
import br.com.finalcraft.finalchat.config.fancychat.FancyChannel;
import br.com.finalcraft.finalchat.config.fancychat.FancyChannelController;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class CommandRegisterer {

    private static final List<String> registeredAliases = new ArrayList<>();

    public static void registerCommands(JavaPlugin pluginInstance, boolean firstLoad) {

        if (firstLoad){
            //Add Parsers
            ArgParserManager.addGlobalParser(FancyChannel.class, ArgParserFancyChannel.class);

            //Add Commands
            FinalCMDManager.registerCommand(pluginInstance, CoreCommand.class);
            FinalCMDManager.registerCommand(pluginInstance, CMDTell.class);
            FinalCMDManager.registerCommand(pluginInstance, CMDChannelLock.class);
            FinalCMDManager.registerCommand(pluginInstance, CMDMuteAll.class);
        }

        for (String registeredCommand : registeredAliases) {
            FinalCMDManager.unregisterCommand(registeredCommand, pluginInstance);
        }
        registeredAliases.clear();

        for (FancyChannel fancyChannel : FancyChannelController.getAllChannels()) {
            registeredAliases.add(fancyChannel.getName()); //Add for removal in case of reload
            FinalCMDManager.registerCommand(pluginInstance, new CMDInChannel(fancyChannel));
        }
    }

}
