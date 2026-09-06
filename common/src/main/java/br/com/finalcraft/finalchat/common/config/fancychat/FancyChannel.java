package br.com.finalcraft.finalchat.config.fancychat;

import br.com.finalcraft.finalchat.FinalChat;
import br.com.finalcraft.finalchat.config.ConfigManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FancyChannel {

    private final String name;
    private final String alias;
    private final String tag_builder;
    private final String permission;
    private final int distance;

    public List<Player> playersOnThisChannel = new ArrayList<Player>();

    public List<FancyTag> tagsFromThisBuilder = new ArrayList<FancyTag>();

    public FancyChannel(String name){
        this.name           = name;
        this.alias          = ConfigManager.getMainConfig().getString("ChannelFormats." + name + ".alias",("" + name.charAt(0)).toLowerCase());
        this.tag_builder    = ConfigManager.getMainConfig().getString("ChannelFormats." + name + ".tag-builder","");
        this.distance       = ConfigManager.getMainConfig().getInt("ChannelFormats." + name + ".distance",-1);
        this.permission     = ConfigManager.getMainConfig().getString("ChannelFormats." + name + ".permission","");

        for (String tagName : tag_builder.split(",")){
            FancyTag fancyTag = FancyTag.mapOfFancyTags.getOrDefault(tagName,null);
            if (fancyTag == null){
                FinalChat.info("I was building the channels and found out that there is no \"" + tagName + "\" FancyTag.");
            }else {
                tagsFromThisBuilder.add(fancyTag);
            }
        }

    }

    public void addMember(Player player){
        this.playersOnThisChannel.add(player);
    }

    public void removeMember(Player player){
        this.playersOnThisChannel.remove(player);
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public String getTag_builder() {
        return tag_builder;
    }

    public String getPermission() {
        return permission;
    }

    public int getDistance() {
        return distance;
    }

    public List<FancyTag> getTagsFromThisBuilder() {
        return tagsFromThisBuilder;
    }

    public List<Player> getPlayersOnThisChannel() {
        return playersOnThisChannel;
    }

}
