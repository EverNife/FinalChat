package br.com.finalcraft.finalchat.config.data;

import br.com.finalcraft.evernifecore.config.playerdata.PDSection;
import br.com.finalcraft.evernifecore.config.playerdata.PlayerData;
import br.com.finalcraft.finalchat.config.fancychat.FancyChannel;
import br.com.finalcraft.finalchat.config.fancychat.FancyChannelController;

import java.util.UUID;

public class FancyPlayerData extends PDSection {

    private FancyChannel lockChannel;
    private FancyChannel tempChannel;
    private UUID lastWhisperer;

    public FancyPlayerData(PlayerData playerData) {
        super(playerData);

        this.lockChannel = FancyChannelController.GLOBAL_CHANNEL;
    }

    @Override
    public void savePDSection() {

    }

    public FancyChannel getLockChannel() {
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

    public FancyChannel extractPriorityChannel() {
        if (getTempChannel() != null){
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
