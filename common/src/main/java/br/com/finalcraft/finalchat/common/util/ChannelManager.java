package br.com.finalcraft.finalchat.common.util;

import br.com.finalcraft.evernifecore.EverNifeCore;
import br.com.finalcraft.evernifecore.api.common.player.FPlayer;
import br.com.finalcraft.finalchat.common.config.fancychat.FancyChannel;
import br.com.finalcraft.finalchat.common.config.fancychat.FancyChannelController;

public class ChannelManager {

    /** Re-seats every online player into the channels they are allowed on. Called after a reload. */
    public static void refresh() {
        for (FPlayer player : EverNifeCore.getPlatform().getOnlinePlayers()) {
            playerJoined(player);
        }
    }

    public static void playerJoined(FPlayer player) {
        for (FancyChannel fancyChannel : FancyChannelController.getAllChannels()) {
            if (fancyChannel.getPermission().isEmpty() || player.hasPermission(fancyChannel.getPermission())) {
                fancyChannel.addMember(player);
            }
        }
    }

    public static void playerLeaved(FPlayer player) {
        for (FancyChannel fancyChannel : FancyChannelController.getAllChannels()) {
            fancyChannel.removeMember(player);
        }
    }
}
