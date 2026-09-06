package br.com.finalcraft.finalchat.api;

import br.com.finalcraft.evernifecore.config.playerdata.PlayerController;
import br.com.finalcraft.finalchat.config.data.FancyPlayerData;
import br.com.finalcraft.finalchat.config.fancychat.FancyChannel;
import br.com.finalcraft.finalchat.config.fancychat.FancyChannelController;
import org.bukkit.entity.Player;

import java.util.Collection;

public class FinalChatApi {

    public static boolean isMainChannel(FancyChannel fancyChannel) {
        return FancyChannelController.GLOBAL_CHANNEL == fancyChannel;
    }

    public static FancyChannel getChannel(String name){
        return FancyChannelController.getFancyChannel(name);
    }

    public static Collection<FancyChannel> getAllChannels(){
        return FancyChannelController.getAllChannels();
    }

    public static FancyChannel getPlayerChannel(Player player){
        return PlayerController.getPDSection(player, FancyPlayerData.class).getLockChannel();
    }

    public static void sendMessage(String message, FancyChannel fancyChannel){
        for (Player player : fancyChannel.getPlayersOnThisChannel()){
            player.sendMessage(message);
        }
    }


}
