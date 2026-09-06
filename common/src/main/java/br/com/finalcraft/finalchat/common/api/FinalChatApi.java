package br.com.finalcraft.finalchat.common.api;

import br.com.finalcraft.evernifecore.api.common.player.FPlayer;
import br.com.finalcraft.evernifecore.playerdata.PlayerController;
import br.com.finalcraft.finalchat.common.config.data.FancyPlayerData;
import br.com.finalcraft.finalchat.common.config.fancychat.FancyChannel;
import br.com.finalcraft.finalchat.common.config.fancychat.FancyChannelController;

import java.util.Collection;

public class FinalChatApi {

    public static boolean isMainChannel(FancyChannel fancyChannel) {
        return FancyChannelController.GLOBAL_CHANNEL == fancyChannel;
    }

    public static FancyChannel getChannel(String name) {
        return FancyChannelController.getFancyChannel(name);
    }

    public static Collection<FancyChannel> getAllChannels() {
        return FancyChannelController.getAllChannels();
    }

    /**
     * The channel this player speaks in by default, or {@code null} for a player whose session is
     * not loaded (offline). Never blocks: this reads the in-memory section only.
     */
    public static FancyChannel getPlayerChannel(FPlayer player) {
        FancyPlayerData playerData = PlayerController.getLoadedSection(player.getUniqueId(), FancyPlayerData.class);
        return playerData == null ? null : playerData.getLockChannel();
    }

    public static void sendMessage(String message, FancyChannel fancyChannel) {
        for (FPlayer player : fancyChannel.getPlayersOnThisChannel()) {
            player.sendMessage(message);
        }
    }


}
