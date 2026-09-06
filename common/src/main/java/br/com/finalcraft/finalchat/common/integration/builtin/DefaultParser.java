package br.com.finalcraft.finalchat.common.integration.builtin;

import br.com.finalcraft.evernifecore.api.common.player.FPlayer;
import br.com.finalcraft.finalchat.common.integration.ThirdPartTagsParser;
import br.com.finalcraft.finalchat.common.placeholders.PlaceHolderIntegration;

/** Resolves the tokens FinalChat itself defines, for servers with no other parser installed. */
public class DefaultParser extends ThirdPartTagsParser {

    private static final DefaultParser INSTANCE = new DefaultParser();

    public static void initialize() {
        addThirdPartTagsParser(INSTANCE);
    }

    public static void shutdown() {
        removeThirdPartTagsParser(INSTANCE);
    }

    @Override
    public String parseTags(String theMessage, FPlayer sender, FPlayer receiver) {
        if (sender != null) {
            //%vault_prefix% is a PlaceholderAPI identifier, not a message placeholder: it stays %-delimited.
            String playerPrefix = PlaceHolderIntegration.parsePlaceholder("%vault_prefix%", sender);
            theMessage = theMessage
                    .replace("{player-prefix}", playerPrefix);

        }
        return theMessage;
    }
}
