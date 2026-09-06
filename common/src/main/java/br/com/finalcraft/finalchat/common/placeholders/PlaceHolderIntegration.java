package br.com.finalcraft.finalchat.common.placeholders;

import br.com.finalcraft.evernifecore.api.common.player.FPlayer;
import br.com.finalcraft.evernifecore.integration.placeholders.PAPIIntegration;

public class PlaceHolderIntegration {

    /**
     * Resolves the two name tokens FinalChat owns and then hands the text to PlaceholderAPI.
     * Degrades on its own where PlaceholderAPI is absent - the text comes back untouched.
     */
    public static String parsePlaceholder(String text, FPlayer player) {
        text = text.replace("{player}", player.getName()).replace("{playername}", player.getName());
        return PAPIIntegration.parse(player, text);
    }

}
