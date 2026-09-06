package br.com.finalcraft.finalchat.common.util.messages;

import br.com.finalcraft.evernifecore.api.common.player.FPlayer;
import br.com.finalcraft.evernifecore.fancytext.FancyFormatter;
import br.com.finalcraft.evernifecore.fancytext.FancyText;
import br.com.finalcraft.evernifecore.playerdata.PlayerController;
import br.com.finalcraft.evernifecore.util.FCColorUtil;
import br.com.finalcraft.finalchat.common.PermissionNodes;
import br.com.finalcraft.finalchat.common.config.data.FancyPlayerData;
import br.com.finalcraft.finalchat.common.config.fancychat.TellTag;
import br.com.finalcraft.finalchat.common.messages.FChatMessages;
import br.com.finalcraft.finalchat.common.util.FancyTextUtil;
import br.com.finalcraft.finalchat.common.util.MuteUtil;

import java.util.Arrays;

public class PrivateMessage {

    public static void sendTell(FPlayer sender, FPlayer target, String msg) {

        if (MuteUtil.isMuted(sender)) {
            FChatMessages.YOU_ARE_MUTED
                    .addPlaceholder("reason", MuteUtil.getMuteMessage(sender))
                    .send(sender);
            return;
        }

        //Both are online right now, so their sections are loaded and this is a memory read. Remembering
        //who to reply to is best-effort: a section that is somehow not there just costs the /reply.
        FancyPlayerData senderPlayerData = PlayerController.getLoadedSection(sender.getUniqueId(), FancyPlayerData.class);
        FancyPlayerData targetPlayerData = PlayerController.getLoadedSection(target.getUniqueId(), FancyPlayerData.class);

        if (senderPlayerData != null) senderPlayerData.setLastWhisperer(target.getUniqueId());
        if (targetPlayerData != null) targetPlayerData.setLastWhisperer(sender.getUniqueId());

        //Append message prefix
        FancyFormatter textToSender = FancyFormatter.of().append(FancyTextUtil.parsePlaceholdersAndClone(TellTag.TELL_TAG.getFancyTextSender(), sender));
        FancyFormatter textToTarget = FancyFormatter.of().append(FancyTextUtil.parsePlaceholdersAndClone(TellTag.TELL_TAG.getFancyTextReceiver(), sender));

        //Replace static variables
        textToSender.replace("{sender}", sender.getName()).replace("{receiver}", target.getName());
        textToTarget.replace("{sender}", sender.getName()).replace("{receiver}", target.getName());

        //Apply to the final message color
        if (sender.hasPermission(PermissionNodes.CHAT_COLOR)) {
            msg = FCColorUtil.colorfy(msg);
        }

        //Append message body
        FancyText messageBody = FancyText.of(msg);
        textToSender.append(messageBody);
        textToTarget.append(messageBody);

        //Send the message
        textToSender.send(sender);
        textToTarget.send(target);

        //Spy on it
        SpyMessage.spyOnThis(textToTarget.getFancyTextList(), Arrays.asList(sender, target));
    }

}
