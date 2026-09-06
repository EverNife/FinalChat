package br.com.finalcraft.finalchat.common.integration;

import br.com.finalcraft.evernifecore.api.common.player.FPlayer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The extension point that lets any plugin put its own tokens into a chat line. A parser registers
 * itself once and is then asked to rewrite every piece of text FinalChat renders.
 *
 * <p>The registry is written from other plugins' enable/disable and read from the delivery thread,
 * which is why it is copy-on-write. Whoever registers a parser owns removing it again on shutdown -
 * a reload that re-registers without {@link #removeThirdPartTagsParser(ThirdPartTagsParser)} would
 * leave the same parser in the list twice.</p>
 */
public abstract class ThirdPartTagsParser {

    public static List<ThirdPartTagsParser> thirdPartTagsParserList = new CopyOnWriteArrayList<>();

    public static String parseThirdParts(String theText, FPlayer sender, FPlayer receiver) {
        for (ThirdPartTagsParser thirdPartTagsParser : thirdPartTagsParserList) {
            theText = thirdPartTagsParser.parseTags(theText, sender, receiver);
        }
        return theText;
    }

    public static void addThirdPartTagsParser(ThirdPartTagsParser aTagParser) {
        if (!thirdPartTagsParserList.contains(aTagParser)) {
            thirdPartTagsParserList.add(aTagParser);
        }
    }

    public static void removeThirdPartTagsParser(ThirdPartTagsParser aTagParser) {
        thirdPartTagsParserList.remove(aTagParser);
    }

    public abstract String parseTags(String theMessage, FPlayer sender, FPlayer receiver);

}
