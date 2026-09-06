package br.com.finalcraft.finalchat.util;

import br.com.finalcraft.evernifecore.util.FCReflectionUtil;
import br.com.finalcraft.finalchat.FinalChat;
import br.com.finalcraft.finalchat.config.ConfigManager;
import br.com.finalcraft.finalchat.config.fancychat.FancyChannel;
import com.earth2me.essentials.api.ESAPIUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IgnoreUtil {

    public static Map<String, List<String>> playerIgnoreListMap = new HashMap<String, List<String>>();

    private static boolean essentialsEnabled = false;
    public static void initialize(){

        essentialsEnabled = Bukkit.getPluginManager().isPluginEnabled("Essentials");
        if (essentialsEnabled){
            if (FCReflectionUtil.isClassLoaded("com.earth2me.essentials.api.ESAPIUtil")){
                FinalChat.info("[Essentials Found] Utilizando ESS-Ignore System");
            }else{
                essentialsEnabled = false;
            }
            return;
        }

        playerIgnoreListMap.clear();

        for (String playerName : ConfigManager.getDataStore().getKeys("IgnoreList")){
            List<String> ignoreList = ConfigManager.getDataStore().getStringList("IgnoreList." + playerName);
            playerIgnoreListMap.put(playerName,ignoreList);
        }
    }

    public static List<String> getIgnoreList(String playerName){
        return playerIgnoreListMap.getOrDefault(playerName, Collections.emptyList());
    }

    public static boolean isIgnoring(Player player, Player otherPlayer){
        try {
            if (essentialsEnabled){
                return ESAPIUtil.isIgnoring(player,otherPlayer);
            }else {
                return isIgnoring(player.getName(),otherPlayer.getName());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isIgnoring(String playerName, String otherPlayerName){
        for (String name : getIgnoreList(playerName)){
            if (name.equalsIgnoreCase(otherPlayerName)){
                return true;
            }
        }
        return false;
    }

    public static boolean hasChannelPermission(Player player, FancyChannel fancyChannel){
        if (fancyChannel.getPermission().isEmpty()){
            return true;
        }
        return player.hasPermission(fancyChannel.getPermission());
    }

    public static boolean ignorePlayer(String playerName, String otherPlayerName){
        List<String> playerCurrentIgnorelist = getIgnoreList(playerName);

        boolean addedToMutelsit;
        if (playerCurrentIgnorelist.contains(otherPlayerName)){
            playerCurrentIgnorelist.remove(otherPlayerName);
            addedToMutelsit = true;
        }else {
            playerCurrentIgnorelist.add(otherPlayerName);
            addedToMutelsit = false;
        }

        playerIgnoreListMap.put(playerName,playerCurrentIgnorelist);
        ConfigManager.getDataStore().setValue("IgnoreList." + playerName,playerCurrentIgnorelist);
        return addedToMutelsit;
    }
}
