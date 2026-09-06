package br.com.finalcraft.finalchat.common.util.messages;

import br.com.finalcraft.evernifecore.EverNifeCore;
import br.com.finalcraft.evernifecore.api.common.game.FLocation;
import br.com.finalcraft.evernifecore.api.common.player.FPlayer;
import br.com.finalcraft.evernifecore.fancytext.FancyFormatter;
import br.com.finalcraft.evernifecore.fancytext.FancyText;
import br.com.finalcraft.evernifecore.util.FCColorUtil;
import br.com.finalcraft.finalchat.common.FinalChatBootstrap;
import br.com.finalcraft.finalchat.common.PermissionNodes;
import br.com.finalcraft.finalchat.common.api.FinalChatSendChannelMessageEvent;
import br.com.finalcraft.finalchat.common.config.fancychat.FancyChannel;
import br.com.finalcraft.finalchat.common.config.fancychat.FancyTag;
import br.com.finalcraft.finalchat.common.messages.FChatMessages;
import br.com.finalcraft.finalchat.common.placeholders.PlaceHolderIntegration;
import br.com.finalcraft.finalchat.common.platform.ChatPlatformBridge;
import br.com.finalcraft.finalchat.common.util.FancyTextUtil;
import br.com.finalcraft.finalchat.common.util.IgnoreUtil;
import br.com.finalcraft.finalchat.common.util.MuteUtil;

import java.util.ArrayList;
import java.util.List;

public class PublicMessage {

    public static void sendPublicMessage(FPlayer player, FancyChannel channel, String msg) {

        if (MuteUtil.isMuted(player)) {
            FChatMessages.YOU_ARE_MUTED
                    .addPlaceholder("reason", MuteUtil.getMuteMessage(player))
                    .send(player);
            return;
        }

        if (player.hasPermission(PermissionNodes.CHAT_COLOR)) {
            msg = FCColorUtil.colorfy(msg);
        }

        //postIfListened does not even build the event where nothing subscribed - and this runs once
        //per chat line. A null answer means nobody listened, so there is nothing to read back.
        final String announcedMessage = msg;
        FinalChatSendChannelMessageEvent sendMessageEvent = EverNifeCore.getEventBus().postIfListened(
                FinalChatSendChannelMessageEvent.class,
                () -> new FinalChatSendChannelMessageEvent(player, channel, announcedMessage));

        if (sendMessageEvent != null) {
            if (sendMessageEvent.isCancelled()) {
                return;
            }
            msg = sendMessageEvent.getMessage();
        }

        int idOfMSGText = 0;
        int contador = 0;
        List<FancyText> textChatList = new ArrayList<FancyText>();
        for (FancyTag fancyTag : channel.getTagsFromThisBuilder()) {
            if (!fancyTag.getPermission().isEmpty() && !player.hasPermission(fancyTag.getPermission())) {
                continue;
            }
            if (!fancyTag.getPlaceholderCondition().isEmpty()) {
                String[] placeholderToCondition = fancyTag.getPlaceholderCondition().split("\\|", 2);
                if (placeholderToCondition.length != 2) {
                    FinalChatBootstrap.get().getLog().warning("Could not read the placeholderCondition [{}] of tag [{}] "
                            + "on channel [{}] - it must be two placeholders separated by a '|'.",
                            fancyTag.getPlaceholderCondition(), fancyTag.getName(), channel.getName());
                    continue;
                }
                if (!PlaceHolderIntegration.parsePlaceholder(placeholderToCondition[0], player).equalsIgnoreCase(PlaceHolderIntegration.parsePlaceholder(placeholderToCondition[1], player))) {
                    continue;
                }

            }
            FancyText fancyText = FancyTextUtil.parsePlaceholdersAndClone(fancyTag.getFancyText(), player);
            fancyText.setText(fancyText.getText().replace("{player}", player.getName()));
            String replacedText = fancyText.getText().replace("{msg}", msg);
            if (!replacedText.equals(fancyText.getText())) {
                fancyText.setText(replacedText);
                idOfMSGText = contador;
            }
            contador++;
            textChatList.add(fancyText);
        }

        final int finalIdOfMSGText = idOfMSGText;

        if (channel.getDistance() <= -1) {
            for (FPlayer onlinePlayerToSendMessage : channel.getPlayersOnThisChannel()) {
                if (!IgnoreUtil.isIgnoring(onlinePlayerToSendMessage, player)) {

                    doTheDeploy(textChatList, player, onlinePlayerToSendMessage, finalIdOfMSGText);

                }
            }
        } else {
            List<FPlayer> playerWhoHeardThis = new ArrayList<FPlayer>();

            for (FPlayer onlinePlayerToSendMessage : channel.getPlayersOnThisChannel()) {
                if (calcDistance(player, onlinePlayerToSendMessage) <= channel.getDistance()) {
                    if (!IgnoreUtil.isIgnoring(onlinePlayerToSendMessage, player)) {
                        playerWhoHeardThis.add(onlinePlayerToSendMessage);

                        doTheDeploy(textChatList, player, onlinePlayerToSendMessage, finalIdOfMSGText);
                    }
                }
            }

            if (playerWhoHeardThis.size() <= 1 && channel.getDistance() > -1) {
                FChatMessages.NOBODY_CLOSE_ENOUGH.send(player);
            }

            if (channel.getDistance() > -1) {
                SpyMessage.spyOnThis(textChatList, playerWhoHeardThis);
            }
        }

        FinalChatBootstrap.chatLog(FancyTextUtil.textOnly(textChatList));
    }

    public static void doTheDeploy(final List<FancyText> textChatList, FPlayer player, FPlayer onlinePlayerToSendMessage, int finalIdOfMSGText) {
        FancyText fancyTextContainingMessage = textChatList.get(finalIdOfMSGText);

        //Splitting on the receiver's own name turns the mention into a piece of its own, so it can
        //carry a hover the rest of the line does not. Limited to 2 parts: an unlimited split drops
        //everything after the second occurrence.
        String[] array = fancyTextContainingMessage.getText().split("(?i)" + onlinePlayerToSendMessage.getName(), 2);
        if (array.length > 1) {
            FancyFormatter fancyFormatter = FancyFormatter.of();
            fancyFormatter.append(textChatList.subList(0, finalIdOfMSGText).toArray(new FancyText[0]));

            FancyText fancyTextPart1 = fancyTextContainingMessage.copy();
            fancyTextPart1.setText(array[0]);

            FancyText fancyTextPart2 = FancyText.of("§6 @" + onlinePlayerToSendMessage.getName() + FCColorUtil.getLastColors(array[0]))
                    .setHover("§aO jogador " + player.getName() + " marcou você!");

            FancyText fancyTextPart3 = fancyTextContainingMessage.copy();
            fancyTextPart3.setText(array[1]);

            fancyFormatter.append(fancyTextPart1);
            fancyFormatter.append(fancyTextPart2);
            fancyFormatter.append(fancyTextPart3);

            fancyFormatter.append(textChatList.subList(finalIdOfMSGText + 1, textChatList.size()).toArray(new FancyText[0]));

            ChatPlatformBridge.get().playMentionSound(onlinePlayerToSendMessage);
            fancyFormatter.send(onlinePlayerToSendMessage);
        } else {
            FancyFormatter.of()
                    .append(textChatList.toArray(new FancyText[0]))
                    .send(onlinePlayerToSendMessage);
        }
    }

    /** Blocks between the two players, or {@link Double#MAX_VALUE} when they are not in the same world. */
    public static double calcDistance(FPlayer player, FPlayer otherPlayer) {
        FLocation playerLocation = player.getLocation();
        FLocation otherPlayerLocation = otherPlayer.getLocation();
        if (playerLocation == null || otherPlayerLocation == null
                || !playerLocation.getWorldName().equals(otherPlayerLocation.getWorldName())) {
            return Double.MAX_VALUE;
        }
        return playerLocation.getLocPos().distance(otherPlayerLocation.getLocPos());
    }
}
