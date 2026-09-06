package br.com.finalcraft.finalchat.integration.builtin;

import br.com.finalcraft.finalchat.config.customtag.CustomTag;
import br.com.finalcraft.finalchat.integration.ThirdPartTagsParser;
import br.com.finalcraft.finalchat.placeholders.PlaceHolderIntegration;
import br.com.finalcraft.finalchat.util.CustomTagUtil;
import org.bukkit.entity.Player;

public class DefaultParser extends ThirdPartTagsParser {

    public static void initialize(){
        addThirdPartTagsParser(new DefaultParser());
    }

    @Override
    public String parseTags(String theMessage, Player sender, Player receiver) {
        if (sender != null){
            CustomTag customTag = CustomTagUtil.getActiveCustomTag(sender);
            String playerPrefix = customTag != null ? customTag.getTheTag() : PlaceHolderIntegration.parsePlaceholder("%vault_prefix%",sender);
            theMessage = theMessage
                    .replace("{player-prefix}"       , playerPrefix);

        }
        return theMessage;
    }
}
