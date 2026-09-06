package br.com.finalcraft.finalchat.common.util;

import br.com.finalcraft.evernifecore.api.common.player.FPlayer;
import br.com.finalcraft.finalchat.common.PermissionNodes;
import br.com.finalcraft.finalchat.common.messages.FChatMessages;

public class MuteUtil {

    public static boolean globalMute = false;

    public static boolean toggleGlobalMute(boolean value) {
        return (globalMute = value);
    }

    public static boolean isMuted(FPlayer player) {
        if (globalMute) {
            return !player.hasPermission(PermissionNodes.MUTE_BYPASS);
        }
        return false;
    }

    /** Why this player is muted, ready to fill the {@code ${reason}} of the muted message. */
    public static String getMuteMessage(FPlayer player) {
        if (globalMute && !player.hasPermission(PermissionNodes.MUTE_BYPASS)) {
            return FChatMessages.GLOBAL_MUTE_REASON.getFancyText(player).getText();
        }
        return "";
    }

}
