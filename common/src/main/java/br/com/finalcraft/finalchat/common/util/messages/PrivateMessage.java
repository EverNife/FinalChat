package br.com.finalcraft.finalchat.util.messages;

import br.com.finalcraft.evernifecore.config.playerdata.PlayerController;
import br.com.finalcraft.evernifecore.fancytext.FancyFormatter;
import br.com.finalcraft.evernifecore.fancytext.FancyText;
import br.com.finalcraft.evernifecore.util.FCColorUtil;
import br.com.finalcraft.finalchat.PermissionNodes;
import br.com.finalcraft.finalchat.config.data.FancyPlayerData;
import br.com.finalcraft.finalchat.config.fancychat.TellTag;
import br.com.finalcraft.finalchat.messages.FChatMessages;
import br.com.finalcraft.finalchat.util.FancyTextUtil;
import br.com.finalcraft.finalchat.util.MuteUtil;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class PrivateMessage {

    public static void sendTell(Player sender, Player target, String msg){

        if (MuteUtil.isMuted(sender)){
            FChatMessages.YOU_ARE_MUTED
                    .addPlaceholder("%reason%", MuteUtil.getMuteMessage(sender))
                    .send(sender);
            return;
        }

        FancyPlayerData senderPlayerData = PlayerController.getPDSection(sender, FancyPlayerData.class);
        FancyPlayerData targetPlayerData = PlayerController.getPDSection(target, FancyPlayerData.class);

        senderPlayerData.setLastWhisperer(targetPlayerData.getUniqueId());
        targetPlayerData.setLastWhisperer(senderPlayerData.getUniqueId());

        //Append message prefix
        FancyFormatter textToSender = FancyFormatter.of().append(FancyTextUtil.parsePlaceholdersAndClone(TellTag.TELL_TAG.getFancyTextSender(), sender));
        FancyFormatter textToTarget = FancyFormatter.of().append(FancyTextUtil.parsePlaceholdersAndClone(TellTag.TELL_TAG.getFancyTextReceiver(), sender));

        //Replace static variables
        textToSender.replace("{sender}",sender.getName()).replace("{receiver}",target.getName());
        textToTarget.replace("{sender}",sender.getName()).replace("{receiver}",target.getName());

        //Apply to the final message color
        if (sender.hasPermission(PermissionNodes.CHAT_COLOR)){
            msg = FCColorUtil.colorfy(msg);
        }

        //Append message body
        FancyText messageBody = new FancyText(msg);
        textToSender.append(messageBody);
        textToTarget.append(messageBody);

        //Send the message
        textToSender.send(sender);
        textToTarget.send(target);

        //Spy on it
        SpyMessage.spyOnThis(textToTarget.getFancyTextList(), Arrays.asList(sender,target));
    }

}
