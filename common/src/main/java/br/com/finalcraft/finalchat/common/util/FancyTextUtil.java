package br.com.finalcraft.finalchat.util;

import br.com.finalcraft.evernifecore.fancytext.FancyText;
import br.com.finalcraft.finalchat.integration.ThirdPartTagsParser;
import br.com.finalcraft.finalchat.placeholders.PlaceHolderIntegration;
import org.bukkit.entity.Player;

import java.util.List;

public class FancyTextUtil {

    public static FancyText parsePlaceholdersAndClone(FancyText fancyText, Player player){
        FancyText cloneFancyText = fancyText.clone();

        if (cloneFancyText.getText() != null)               cloneFancyText.setText(parseThings(cloneFancyText.getText(),player));
        if (cloneFancyText.getHoverText() != null)          cloneFancyText.setHoverText(parseThings(cloneFancyText.getHoverText(), player));
        if (cloneFancyText.getClickActionText() != null)    cloneFancyText.setClickAction(parseThings(cloneFancyText.getClickActionText(), player), cloneFancyText.getClickActionType());

        return cloneFancyText;
    }

    private static String parseThings(String theText, Player sender){
        return ThirdPartTagsParser.parseThirdParts(PlaceHolderIntegration.parsePlaceholder(theText,sender),sender,null);
    }

    public static String textOnly(List<FancyText> fancyTextList){
        StringBuilder stringBuilder = new StringBuilder();
        for (FancyText fancyText : fancyTextList) {
            stringBuilder.append(fancyText.getText());
        }
        return stringBuilder.toString();
    }

}
