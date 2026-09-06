package br.com.finalcraft.finalchat.minecraft.integration;

import br.com.finalcraft.evernifecore.EverNifeCore;
import br.com.finalcraft.evernifecore.ecplugin.ECPluginData;
import br.com.finalcraft.finalchat.common.util.IgnoreUtil;
import com.earth2me.essentials.api.ESAPIUtil;
import org.bukkit.entity.Player;

/**
 * Hands FinalChat over to Essentials' ignore list where the server has one. Only this class ever
 * runs a frame naming the Essentials API, so a server without it never tries to resolve anything -
 * and only some builds of Essentials carry {@code ESAPIUtil} at all, which is why presence of the
 * plugin is not enough and the class itself is probed.
 */
public final class EssentialsIgnoreIntegration {

    private static final String TARGET_PLUGIN = "Essentials";

    private EssentialsIgnoreIntegration() {
    }

    public static boolean isPresent() {
        return EverNifeCore.getPlatform().isPluginLoaded(TARGET_PLUGIN);
    }

    /** Idempotent: called at enable and again on every reload. */
    public static void install(ECPluginData pd) {
        uninstall();

        if (!isPresent()) {
            return;
        }

        try {
            Class.forName("com.earth2me.essentials.api.ESAPIUtil");
        } catch (Throwable t) { // NoClassDefFoundError is an Error, not an Exception
            pd.getLog().info("{} is installed but exposes no ignore API - using FinalChat's own list.", TARGET_PLUGIN);
            return;
        }

        IgnoreUtil.useSource((player, otherPlayer) -> {
            Player bukkitPlayer = player.adapter().getPlayer();
            Player bukkitOther = otherPlayer.adapter().getPlayer();
            return bukkitPlayer != null && bukkitOther != null && ESAPIUtil.isIgnoring(bukkitPlayer, bukkitOther);
        });
        pd.getLog().info("{} found - using its ignore list.", TARGET_PLUGIN);
    }

    public static void uninstall() {
        IgnoreUtil.clearSource();
    }
}
