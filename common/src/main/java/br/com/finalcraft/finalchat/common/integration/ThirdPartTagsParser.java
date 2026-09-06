package br.com.finalcraft.finalchat.integration;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class ThirdPartTagsParser {

    public static List <ThirdPartTagsParser> thirdPartTagsParserList = new ArrayList<ThirdPartTagsParser>();

    public static String parseThirdParts(String theText, Player sender, Player receiver){
        for (ThirdPartTagsParser thirdPartTagsParser : thirdPartTagsParserList) {
            theText = thirdPartTagsParser.parseTags(theText,sender,receiver);
        }
        return theText;
    }

    public static void addThirdPartTagsParser(ThirdPartTagsParser aTagParser){
        if (!thirdPartTagsParserList.contains(aTagParser)){
            thirdPartTagsParserList.add(aTagParser);
        }
    }

    public abstract String parseTags(String theMessage, Player sender, Player receiver);

}
