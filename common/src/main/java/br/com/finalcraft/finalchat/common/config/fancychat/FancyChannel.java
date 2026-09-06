package br.com.finalcraft.finalchat.common.config.fancychat;

import br.com.finalcraft.evernifecore.api.common.player.FPlayer;
import br.com.finalcraft.finalchat.common.FinalChatBootstrap;
import br.com.finalcraft.finalchat.common.config.ConfigManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * One chat channel as declared under {@code ChannelFormats.<name>}: an alias, a radius, an optional
 * permission and the ordered list of tags that build each line.
 *
 * <p>Subclassed by sibling plugins to publish a channel that is not in config.yml at all (a clan
 * channel, a party channel): the {@code (String name)} constructor tolerates a name with no config
 * block, and {@link #getPlayersOnThisChannel()} is the single method such a subclass overrides.</p>
 */
public class FancyChannel {

    private final String name;
    private final String alias;
    private final String tag_builder;
    private final String permission;
    private final int distance;

    //Mutated on join/quit from the server thread and iterated during delivery from an async thread.
    private final List<FPlayer> playersOnThisChannel = new CopyOnWriteArrayList<>();

    private final List<FancyTag> tagsFromThisBuilder = new ArrayList<>();

    public FancyChannel(String name) {
        this.name           = name;
        this.alias          = ConfigManager.getMainConfig().getString("ChannelFormats." + name + ".alias", ("" + name.charAt(0)).toLowerCase());
        this.tag_builder    = ConfigManager.getMainConfig().getString("ChannelFormats." + name + ".tag-builder", "");
        this.distance       = ConfigManager.getMainConfig().getInt("ChannelFormats." + name + ".distance", -1);
        this.permission     = ConfigManager.getMainConfig().getString("ChannelFormats." + name + ".permission", "");

        for (String tagName : tag_builder.split(",")) {
            FancyTag fancyTag = FancyTag.mapOfFancyTags.getOrDefault(tagName, null);
            if (fancyTag == null) {
                FinalChatBootstrap.get().getLog().warning("Channel [{}] lists a FancyTag named [{}] that does not exist.", name, tagName);
            } else {
                tagsFromThisBuilder.add(fancyTag);
            }
        }

    }

    public void addMember(FPlayer player) {
        this.playersOnThisChannel.add(player);
    }

    public void removeMember(FPlayer player) {
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

    public List<FPlayer> getPlayersOnThisChannel() {
        return playersOnThisChannel;
    }

}
