package br.com.finalcraft.finalchat.common.commands;

import br.com.finalcraft.evernifecore.commands.finalcmd.FinalCMDManager;
import br.com.finalcraft.evernifecore.commands.finalcmd.argument.ArgParserManager;
import br.com.finalcraft.evernifecore.ecplugin.ECPluginData;
import br.com.finalcraft.finalchat.common.FinalChatBootstrap;
import br.com.finalcraft.finalchat.common.commands.argparser.ArgParserFancyChannel;
import br.com.finalcraft.finalchat.common.config.fancychat.FancyChannel;
import br.com.finalcraft.finalchat.common.config.fancychat.FancyChannelController;

public class CommandRegisterer {

    public static void registerCommands(ECPluginData ecPluginData, boolean firstLoad) {

        if (firstLoad) {
            ArgParserManager.addPluginParser(ecPluginData, FancyChannel.class, ArgParserFancyChannel.class);

            registerOrWarn(ecPluginData, CoreCommand.class);
            registerOrWarn(ecPluginData, CMDTell.class);
            registerOrWarn(ecPluginData, CMDChannelLock.class);
            registerOrWarn(ecPluginData, CMDMuteAll.class);
        }

        //The per-channel commands are unregistered by FancyChannelController when it rebuilds the
        //channel set, so bookkeeping lives in exactly one place.
        for (FancyChannel fancyChannel : FancyChannelController.getAllChannels()) {
            if (FinalCMDManager.registerCommand(ecPluginData, new CMDInChannel(fancyChannel)).isEmpty()) {
                FinalChatBootstrap.get().getLog().warning("Could not register the command for channel [{}].", fancyChannel.getName());
            }
        }
    }

    private static void registerOrWarn(ECPluginData ecPluginData, Class<?> commandClass) {
        if (FinalCMDManager.registerCommand(ecPluginData, commandClass).isEmpty()) {
            FinalChatBootstrap.get().getLog().warning("Could not register the command class [{}].", commandClass.getSimpleName());
        }
    }

}
