package br.com.finalcraft.finalchat.common.commands;


import br.com.finalcraft.evernifecore.api.common.player.FPlayer;
import br.com.finalcraft.evernifecore.argumento.MultiArgumentos;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.Arg;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.FinalCMD;
import br.com.finalcraft.evernifecore.commands.finalcmd.custom.ICustomFinalCMD;
import br.com.finalcraft.evernifecore.commands.finalcmd.custom.contexts.CustomizeContext;
import br.com.finalcraft.evernifecore.scheduler.FCScheduler;
import br.com.finalcraft.finalchat.common.config.data.FancyPlayerData;
import br.com.finalcraft.finalchat.common.config.fancychat.FancyChannel;
import br.com.finalcraft.finalchat.common.messages.FChatMessages;
import br.com.finalcraft.finalchat.common.util.messages.PublicMessage;

/**
 * One instance of this is registered per channel, which is how {@code /global}, {@code /g},
 * {@code /local} and {@code /l} come out of config.yml: the command declares no label of its own and
 * {@link #customize} writes the channel's name, alias and permission into it at registration.
 */
public class CMDInChannel implements ICustomFinalCMD {

    private final FancyChannel fancyChannel;

    public CMDInChannel(FancyChannel fancyChannel) {
        this.fancyChannel = fancyChannel;
    }

    @Override
    public void customize(CustomizeContext context) {
        context.getFinalCMDData()
                .setPermission(fancyChannel.getPermission())
                .setLabels(
                        new String[]{fancyChannel.getName(), fancyChannel.getAlias()}
                );
    }


    @FinalCMD(
            aliases = ""
    )
    public void inChannel(FPlayer player, FancyPlayerData playerData, @Arg("[msg]") String message, MultiArgumentos argumentos) {

        if (message == null) {
            playerData.setLockChannel(fancyChannel);
            FChatMessages.CHANNEL_DEFINED_AS_YOUR_DEFAULT
                    .addPlaceholder("channel_name", fancyChannel.getName())
                    .send(player);
            return;
        }

        //Delivered straight to this channel instead of round-tripping through a synthetic chat event:
        //the channel is known here, so nothing has to be stashed on the player and read back by a
        //listener - which is also what made two quick messages able to swap channels.
        String finalMessage = argumentos.joinStringArgs();
        FCScheduler.runAsync(() -> PublicMessage.sendPublicMessage(player, fancyChannel, finalMessage));
    }
}
