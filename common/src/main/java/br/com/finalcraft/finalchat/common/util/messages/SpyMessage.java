package br.com.finalcraft.finalchat.util.messages;

import br.com.finalcraft.evernifecore.fancytext.FancyFormatter;
import br.com.finalcraft.evernifecore.fancytext.FancyText;
import br.com.finalcraft.finalchat.FinalChat;
import br.com.finalcraft.finalchat.util.FancyTextUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class SpyMessage {

    private static Map<Player,String> spyingPlayers = new HashMap<>(); //Player - Color

    public static void changeSpyState(Player player, String color, boolean state){
        spyingPlayers.remove(player);
        if (state == true){
            spyingPlayers.put(player, color);
        }
    }

    public static boolean isSpying(Player player){
        return spyingPlayers.containsKey(player);
    }

    public static void spyOnThis(List<FancyText> msg, List<Player> allPlayerWhoHeard){
        if (spyingPlayers.size() == 0){
            return;
        }

        HashSet<UUID> allPlayerWhoHeardUUIDs = new HashSet();

        StringBuilder allPlayerWhoHeardString = new StringBuilder("§7§oThe Eye is Watching Us");
        for (Player player : allPlayerWhoHeard){
            allPlayerWhoHeardString.append("\n§7  - §a" + player.getName());
            allPlayerWhoHeardUUIDs.add(player.getUniqueId());
        }

        msg.forEach(fancyText -> {
            fancyText.setText("§7" + ChatColor.stripColor(fancyText.getText()));
            fancyText.setHoverText("Jogadores que escutaram essa mensagem: \n " + allPlayerWhoHeardString);
        });

        String previousColor = "§7";
        for (FancyText fancyText : msg) {
            fancyText.setText(previousColor + ChatColor.stripColor(fancyText.getText()));
        }

        FinalChat.chatLog(FancyTextUtil.textOnly(msg));
        FancyFormatter formatter = FancyFormatter.of().append(msg.toArray(new FancyText[0]));
        for (Map.Entry<Player, String> entry : spyingPlayers.entrySet()) {
            Player staffSpyHearing = entry.getKey();
            String staffColor = entry.getValue();

            if (staffSpyHearing.isOnline() == false){
                continue;
            }

            if (allPlayerWhoHeardUUIDs.contains(staffSpyHearing.getUniqueId())){
                continue;
            }

            if (!previousColor.equalsIgnoreCase(staffColor)){
                for (FancyText fancyText : msg) {
                    fancyText.setText(staffColor + ChatColor.stripColor(fancyText.getText()));
                }
                previousColor = staffColor;
            }

            formatter.send(staffSpyHearing);
        }
    }
}
