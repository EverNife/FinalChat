package br.com.finalcraft.finalchat.common.commands;


import br.com.finalcraft.evernifecore.api.common.player.FPlayer;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.Arg;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.FinalCMD;
import br.com.finalcraft.evernifecore.util.FCMessageUtil;
import br.com.finalcraft.finalchat.common.config.data.FancyPlayerData;
import br.com.finalcraft.finalchat.common.config.fancychat.FancyChannel;
import br.com.finalcraft.finalchat.common.messages.FChatMessages;

public class CMDChannelLock {

    @FinalCMD(
            aliases = {"channellock", "ch", "channel", "lock"}
    )
    public void lockChannel(FPlayer player, FancyPlayerData playerData, @Arg("<Channel>") FancyChannel fancyChannel) {

        if (!fancyChannel.getPermission().isEmpty() &&
                !FCMessageUtil.hasThePermission(player, fancyChannel.getPermission())) {
            return;
        }

        playerData.setLockChannel(fancyChannel);

        FChatMessages.CHANNEL_DEFINED_AS_YOUR_DEFAULT
                .addPlaceholder("channel_name", fancyChannel.getName())
                .send(player);
    }
}
