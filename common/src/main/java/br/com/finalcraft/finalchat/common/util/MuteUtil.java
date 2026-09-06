package br.com.finalcraft.finalchat.util;

import br.com.finalcraft.finalchat.PermissionNodes;
import org.bukkit.entity.Player;

public class MuteUtil {

    public static boolean globalMute = false;

    public static boolean toggleGlobalMute(boolean value){
        return (globalMute = value);
    }

    public static boolean isMuted(Player player){
        if (globalMute){
            return !player.hasPermission(PermissionNodes.MUTE_BYPASS);
        }
        return false;
    }

    public static String getMuteMessage(Player player){
        if (globalMute && !player.hasPermission(PermissionNodes.MUTE_BYPASS)){
            return "§c  §l(GlobalMute está ativado!)";
        }
        return "";
    }

}

