package br.com.finalcraft.finalchat.common.config.data;

import br.com.finalcraft.evernifecore.playerdata.PDSection;
import br.com.finalcraft.finalchat.common.config.fancychat.FancyChannel;
import br.com.finalcraft.finalchat.common.config.fancychat.FancyChannelController;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

/**
 * Which channel a player speaks in, and who whispered them last. All of it is SESSION state: the
 * three fields are runtime-only, so this section never turns dirty and its collection stays empty.
 * It is a {@link PDSection} anyway because that is what lets a command declare a
 * {@code FancyPlayerData} parameter and get the caller's own, and what sibling plugins reach for
 * to redirect a player's next message.
 *
 * <p>Registered as RESIDENT (see {@code FinalChatBootstrap}): a released cell would come back blank
 * and silently drop the channel the player locked mid-session.</p>
 */
public class FancyPlayerData extends PDSection {

    @JsonIgnore
    private transient FancyChannel lockChannel;
    @JsonIgnore
    private transient FancyChannel tempChannel;
    @JsonIgnore
    private transient UUID lastWhisperer;

    public FancyPlayerData() {
        //Required no-arg constructor (Jackson + transient default seeding)
    }

    /**
     * The channel this player speaks in by default, always an instance of the CURRENT channel set:
     * a reload rebuilds every channel, so the held object is looked up again by name and only its
     * disappearance from config.yml sends the player back to the global channel.
     */
    public FancyChannel getLockChannel() {
        if (lockChannel != null) {
            FancyChannel current = FancyChannelController.getFancyChannel(lockChannel.getName());
            if (current != null) {
                lockChannel = current;
                return lockChannel;
            }
        }
        lockChannel = FancyChannelController.GLOBAL_CHANNEL;
        return lockChannel;
    }

    public void setLockChannel(FancyChannel lockChannel) {
        this.lockChannel = lockChannel;
    }

    public FancyChannel getTempChannel() {
        return tempChannel;
    }

    public void setTempChannel(FancyChannel tempChannel) {
        this.tempChannel = tempChannel;
    }

    /** The channel the next message goes to, consuming the one-shot temporary channel if set. */
    public FancyChannel extractPriorityChannel() {
        if (getTempChannel() != null) {
            FancyChannel extractedChannel = getTempChannel();
            setTempChannel(null);
            return extractedChannel;
        }

        return getLockChannel();
    }

    public UUID getLastWhisperer() {
        return lastWhisperer;
    }

    public void setLastWhisperer(UUID lastWhisperer) {
        this.lastWhisperer = lastWhisperer;
    }
}
