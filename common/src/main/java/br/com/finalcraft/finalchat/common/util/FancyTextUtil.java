package br.com.finalcraft.finalchat.common.util;

import br.com.finalcraft.evernifecore.api.common.player.FPlayer;
import br.com.finalcraft.evernifecore.fancytext.FancyText;
import br.com.finalcraft.finalchat.common.integration.ThirdPartTagsParser;
import br.com.finalcraft.finalchat.common.placeholders.PlaceHolderIntegration;

import java.util.List;

public class FancyTextUtil {

    /** A copy of the tag with every placeholder and third-party token already resolved for this player. */
    public static FancyText parsePlaceholdersAndClone(FancyText fancyText, FPlayer player) {
        FancyText copyFancyText = fancyText.copy();

        if (copyFancyText.getText() != null)            copyFancyText.setText(parseThings(copyFancyText.getText(), player));
        if (copyFancyText.getHoverText() != null)       copyFancyText.setHover(parseThings(copyFancyText.getHoverText(), player));
        if (copyFancyText.getClickActionText() != null) copyFancyText.setClick(parseThings(copyFancyText.getClickActionText(), player), copyFancyText.getClickActionType());

        return copyFancyText;
    }

    private static String parseThings(String theText, FPlayer sender) {
        return ThirdPartTagsParser.parseThirdParts(PlaceHolderIntegration.parsePlaceholder(theText, sender), sender, null);
    }

    public static String textOnly(List<FancyText> fancyTextList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (FancyText fancyText : fancyTextList) {
            stringBuilder.append(fancyText.getText());
        }
        return stringBuilder.toString();
    }

}
