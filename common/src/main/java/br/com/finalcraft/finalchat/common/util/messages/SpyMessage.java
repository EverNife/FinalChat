package br.com.finalcraft.finalchat.common.util.messages;

import br.com.finalcraft.evernifecore.EverNifeCore;
import br.com.finalcraft.evernifecore.api.common.player.FPlayer;
import br.com.finalcraft.evernifecore.fancytext.FancyFormatter;
import br.com.finalcraft.evernifecore.fancytext.FancyText;
import br.com.finalcraft.evernifecore.util.FCColorUtil;
import br.com.finalcraft.finalchat.common.FinalChatBootstrap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Staff copies of conversations they were not part of, recoloured per staff member. */
public class SpyMessage {

    private static final String DEFAULT_COLOR = "§7";

    //uuid -> the colour that staff member asked for. Keyed by uuid so a player who quits without a
    //quit event does not keep a live reference around.
    private static final Map<UUID, String> spyingPlayers = new ConcurrentHashMap<>();

    public static void changeSpyState(FPlayer player, String color, boolean state) {
        if (state) {
            spyingPlayers.put(player.getUniqueId(), color == null || color.isEmpty() ? DEFAULT_COLOR : color);
        } else {
            spyingPlayers.remove(player.getUniqueId());
        }
    }

    public static boolean isSpying(FPlayer player) {
        return spyingPlayers.containsKey(player.getUniqueId());
    }

    public static void spyOnThis(List<FancyText> msg, List<FPlayer> allPlayerWhoHeard) {
        if (spyingPlayers.isEmpty()) {
            return;
        }

        Set<UUID> allPlayerWhoHeardUUIDs = new HashSet<>();

        StringBuilder allPlayerWhoHeardString = new StringBuilder("§7§oThe Eye is Watching Us");
        for (FPlayer player : allPlayerWhoHeard) {
            allPlayerWhoHeardString.append("\n§7  - §a" + player.getName());
            allPlayerWhoHeardUUIDs.add(player.getUniqueId());
        }
        String hover = "Jogadores que escutaram essa mensagem: \n " + allPlayerWhoHeardString;

        //The colour is what changes per staff member, so the plain text is stripped once and each
        //recipient gets a formatter built from it. A single shared formatter cannot work: append
        //COPIES the piece it takes, so recolouring the originals afterwards reaches nothing.
        List<String> plainPieces = new ArrayList<>(msg.size());
        for (FancyText fancyText : msg) {
            plainPieces.add(FCColorUtil.stripColor(fancyText.getText()));
        }

        FinalChatBootstrap.chatLog(String.join("", plainPieces));

        for (Map.Entry<UUID, String> entry : spyingPlayers.entrySet()) {
            if (allPlayerWhoHeardUUIDs.contains(entry.getKey())) {
                continue;
            }

            FPlayer staffSpyHearing = EverNifeCore.getPlatform().getPlayer(entry.getKey());
            if (staffSpyHearing == null || !staffSpyHearing.isOnline()) {
                continue;
            }

            String staffColor = entry.getValue();

            FancyFormatter formatter = FancyFormatter.of();
            for (int i = 0; i < msg.size(); i++) {
                formatter.append(msg.get(i).copy()
                        .setText(staffColor + plainPieces.get(i))
                        .setHover(hover));
            }
            formatter.send(staffSpyHearing);
        }
    }
}
