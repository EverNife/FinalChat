package br.com.finalcraft.finalchat.common.util;

import br.com.finalcraft.evernifecore.api.common.player.FPlayer;
import br.com.finalcraft.finalchat.common.FinalChat;
import br.com.finalcraft.finalchat.common.config.ConfigManager;
import br.com.finalcraft.finalchat.common.config.fancychat.FancyChannel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Who does not want to hear whom. FinalChat keeps a list of its own in {@code DataStore.yml}, but a
 * server running a plugin that already owns ignores hands one in through {@link #useSource} and
 * that answer wins - two ignore lists disagreeing is worse than either.
 */
public class IgnoreUtil {

    /** Where the real answer comes from on a server whose ignore list belongs to another plugin. */
    public interface IgnoreSource {
        boolean isIgnoring(FPlayer player, FPlayer otherPlayer);
    }

    public static Map<String, List<String>> playerIgnoreListMap = new HashMap<String, List<String>>();

    private static volatile IgnoreSource source;

    public static void useSource(IgnoreSource ignoreSource) {
        source = ignoreSource;
    }

    public static void clearSource() {
        source = null;
    }

    public static void initialize() {
        playerIgnoreListMap.clear();

        for (String playerName : ConfigManager.getDataStore().getKeys("IgnoreList")) {
            List<String> ignoreList = ConfigManager.getDataStore().getStringList("IgnoreList." + playerName);
            playerIgnoreListMap.put(playerName, ignoreList);
        }
    }

    public static List<String> getIgnoreList(String playerName) {
        return playerIgnoreListMap.getOrDefault(playerName, Collections.emptyList());
    }

    public static boolean isIgnoring(FPlayer player, FPlayer otherPlayer) {
        IgnoreSource ignoreSource = source;
        if (ignoreSource == null) {
            return isIgnoring(player.getName(), otherPlayer.getName());
        }
        try {
            return ignoreSource.isIgnoring(player, otherPlayer);
        } catch (Throwable t) { // a broken bridge must not swallow the whole chat line
            clearSource();
            FinalChat.get().getLog().warning("The installed ignore source failed and was dropped; "
                    + "falling back to FinalChat's own list until the next reload. {}", t);
            return isIgnoring(player.getName(), otherPlayer.getName());
        }
    }

    public static boolean isIgnoring(String playerName, String otherPlayerName) {
        for (String name : getIgnoreList(playerName)) {
            if (name.equalsIgnoreCase(otherPlayerName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasChannelPermission(FPlayer player, FancyChannel fancyChannel) {
        if (fancyChannel.getPermission().isEmpty()) {
            return true;
        }
        return player.hasPermission(fancyChannel.getPermission());
    }
}
