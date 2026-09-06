package br.com.finalcraft.finalchat.placeholders;

import br.com.finalcraft.evernifecore.integration.placeholders.PAPIIntegration;
import org.bukkit.entity.Player;

public class PlaceHolderIntegration{

    public static boolean hasPlaceholderApi = false;
    public static String parsePlaceholder(String text, Player player){
        text = text.replace("{player}",player.getName()).replace("{playername}",player.getName());
        return PAPIIntegration.parse(player,text);
    }

    public static void initialize(){
        hasPlaceholderApi = true;
    }

}
