package br.com.finalcraft.finalchat.util;

import br.com.finalcraft.finalchat.config.fancychat.FancyChannel;
import br.com.finalcraft.finalchat.config.fancychat.FancyChannelController;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ChannelManager {

    public static void refresh(){
        for (Player player : Bukkit.getOnlinePlayers()){
            for (FancyChannel fancyChannel : FancyChannelController.getAllChannels()){
                if (fancyChannel.getPermission().isEmpty() || player.hasPermission(fancyChannel.getPermission())){
                    fancyChannel.addMember(player);
                }
            }
        }
    }

    public static void playerJoined(Player player){
        for (FancyChannel fancyChannel : FancyChannelController.getAllChannels()){
            if (fancyChannel.getPermission().isEmpty() || player.hasPermission(fancyChannel.getPermission())){
                fancyChannel.addMember(player);
            }
        }
    }

    public static void playerLeaved(Player player){
        for (FancyChannel fancyChannel : FancyChannelController.getAllChannels()){
            fancyChannel.removeMember(player);
        }
    }
}
