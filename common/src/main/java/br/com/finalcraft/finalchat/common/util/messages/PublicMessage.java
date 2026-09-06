package br.com.finalcraft.finalchat.util.messages;

import br.com.finalcraft.evernifecore.fancytext.FancyFormatter;
import br.com.finalcraft.evernifecore.fancytext.FancyText;
import br.com.finalcraft.evernifecore.sound.FCSound;
import br.com.finalcraft.evernifecore.util.FCColorUtil;
import br.com.finalcraft.finalchat.FinalChat;
import br.com.finalcraft.finalchat.PermissionNodes;
import br.com.finalcraft.finalchat.api.FinalChatSendChannelMessageEvent;
import br.com.finalcraft.finalchat.config.fancychat.FancyChannel;
import br.com.finalcraft.finalchat.config.fancychat.FancyTag;
import br.com.finalcraft.finalchat.messages.FChatMessages;
import br.com.finalcraft.finalchat.placeholders.PlaceHolderIntegration;
import br.com.finalcraft.finalchat.util.FancyTextUtil;
import br.com.finalcraft.finalchat.util.IgnoreUtil;
import br.com.finalcraft.finalchat.util.MuteUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PublicMessage {

    public static void sendPublicMessage(Player player, FancyChannel channel, String msg){

        if (MuteUtil.isMuted(player)){
            FChatMessages.YOU_ARE_MUTED
                    .addPlaceholder("%reason%", MuteUtil.getMuteMessage(player))
                    .send(player);
            return;
        }

        if (player.hasPermission(PermissionNodes.CHAT_COLOR)){
            msg = FCColorUtil.colorfy(msg);
        }

        FinalChatSendChannelMessageEvent sendMessageEvent = new FinalChatSendChannelMessageEvent(player, channel, msg);
        Bukkit.getServer().getPluginManager().callEvent(sendMessageEvent);
        if (sendMessageEvent.isCancelled()){
            return;
        }

        msg = sendMessageEvent.getMessage();

        int idOfMSGText = 0;
        int contador = 0;
        List<FancyText> textChatList = new ArrayList<FancyText>();
        for (FancyTag fancyTag : channel.getTagsFromThisBuilder()){
            if (!fancyTag.getPermission().isEmpty() && !player.hasPermission(fancyTag.getPermission())){
                continue;
            }
            if (!fancyTag.getPlaceholderCondition().isEmpty()){
                String[] placeholderToCondition = fancyTag.getPlaceholderCondition().split("|");
                if (placeholderToCondition.length != 2){
                    FinalChat.info("Error parsing placeholderToCondition [" + fancyTag.getPlaceholderCondition() + "] from tag [" + fancyTag.getName() + "] and channel [" + channel.getName() + "]" );
                    continue;
                }
                if (!PlaceHolderIntegration.parsePlaceholder(placeholderToCondition[0],player).equalsIgnoreCase(PlaceHolderIntegration.parsePlaceholder(placeholderToCondition[1],player))){
                    continue;
                }

            }
            FancyText fancyText = FancyTextUtil.parsePlaceholdersAndClone(fancyTag.getFancyText(),player);
            fancyText.setText(fancyText.getText().replace("{player}",player.getName()));
            String replacedText = fancyText.getText().replace("{msg}",msg);
            if (!replacedText.equals(fancyText.getText())){
                fancyText.setText(replacedText);
                idOfMSGText = contador;
            }
            contador++;
            textChatList.add(fancyText);
        }

        final int finalIdOfMSGText = idOfMSGText;

        if (channel.getDistance() <= -1){
            for (Player onlinePlayerToSendMessage : channel.getPlayersOnThisChannel()) {
                if (!IgnoreUtil.isIgnoring(onlinePlayerToSendMessage, player)){

                    //Entregando mensagem ao jogador!
                    doTheDeploy(textChatList,player,onlinePlayerToSendMessage,finalIdOfMSGText);

                }
            }
        }else {
            List<Player> playerWhoHeardThis = new ArrayList<Player>();

            for (Player onlinePlayerToSendMessage : channel.getPlayersOnThisChannel()) {
                if (calcDistance(player,onlinePlayerToSendMessage) <=  channel.getDistance()){
                    if (!IgnoreUtil.isIgnoring(onlinePlayerToSendMessage, player)){
                        playerWhoHeardThis.add(onlinePlayerToSendMessage);

                        //Entregando mensage ao jogador!
                        doTheDeploy(textChatList,player,onlinePlayerToSendMessage,finalIdOfMSGText);
                    }
                }
            }

            if (playerWhoHeardThis.size() <= 1 && channel.getDistance() > -1){
                player.sendMessage("§6§l ▶ §cNão tem ninguém perto de você para receber essa mensagem...");
            }

            if (channel.getDistance() > -1){
                SpyMessage.spyOnThis(textChatList, playerWhoHeardThis);
            }
        }

        FinalChat.chatLog(FancyTextUtil.textOnly(textChatList));
    }

    public static void doTheDeploy(final List<FancyText> textChatList, Player player, Player onlinePlayerToSendMessage, int finalIdOfMSGText){
        FancyText fancyTextContainingMessage = textChatList.get(finalIdOfMSGText);

        String[] array = fancyTextContainingMessage.getText().split("(?i)" + onlinePlayerToSendMessage.getName()); // splits case insensitive
        if (array.length > 1){
            FancyFormatter fancyFormatter = new FancyFormatter();
            fancyFormatter.append(textChatList.subList(0,finalIdOfMSGText).toArray(new FancyText[0]));

            FancyText fancyTextPart1 = fancyTextContainingMessage.clone();
            fancyTextPart1.setText(array[0]);

            FancyText fancyTextPart2 = new FancyText("§6 @" + onlinePlayerToSendMessage.getName() + ChatColor.getLastColors(array[0])).setHoverText("§aO jogador " + player.getName() + " marcou você!");

            FancyText fancyTextPart3 = fancyTextContainingMessage.clone();
            fancyTextPart3.setText(array[1]);

            fancyFormatter.append(fancyTextPart1);
            fancyFormatter.append(fancyTextPart2);
            fancyFormatter.append(fancyTextPart3);

            fancyFormatter.append(textChatList.subList(finalIdOfMSGText + 1,textChatList.size()).toArray(new FancyText[0]));

            FCSound.EXPERIENCE_ORB.playSoundFor(onlinePlayerToSendMessage);
            fancyFormatter.send(onlinePlayerToSendMessage);
        }else {
            FancyFormatter.of()
                    .append(textChatList.toArray(new FancyText[0]))
                    .send(onlinePlayerToSendMessage);
        }
    }

    public static double calcDistance(Player player, Player otherPlayer){
        Location playerLocation = player.getLocation();
        Location otherPlayerLocation = otherPlayer.getLocation();
        if (playerLocation.getWorld() == otherPlayerLocation.getWorld()){
            return playerLocation.distance(otherPlayerLocation);
        }
        return Double.MAX_VALUE;
    }
}
