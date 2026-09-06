package br.com.finalcraft.finalchat.minecraft.platform;

import br.com.finalcraft.evernifecore.api.common.player.FPlayer;
import br.com.finalcraft.evernifecore.minecraft.sound.FCSound;
import br.com.finalcraft.evernifecore.minecraft.version.MCVersion;
import br.com.finalcraft.evernifecore.util.FCColorUtil;
import br.com.finalcraft.finalchat.common.platform.ChatPlatformBridge;
import org.bukkit.entity.Player;

/** What chat delivery needs from Bukkit: vanish-aware visibility, the mention sound and a console
 *  that only prints colour codes from 1.8 onwards. */
public class BukkitChatPlatformBridge extends ChatPlatformBridge {

    @Override
    public boolean canSee(FPlayer viewer, FPlayer target) {
        if (target == null || !target.isOnline()) {
            return false;
        }
        Player bukkitViewer = viewer.adapter().getPlayer();
        Player bukkitTarget = target.adapter().getPlayer();
        return bukkitViewer == null || bukkitTarget == null || bukkitViewer.canSee(bukkitTarget);
    }

    @Override
    public void playMentionSound(FPlayer player) {
        Player bukkitPlayer = player.adapter().getPlayer();
        if (bukkitPlayer != null) {
            FCSound.EXPERIENCE_ORB.playSoundFor(bukkitPlayer);
        }
    }

    @Override
    public String forConsoleLog(String line) {
        return MCVersion.isLowerEquals(MCVersion.v1_7_10) ? FCColorUtil.stripColor(line) : line;
    }
}
