package br.com.finalcraft.finalchat.commands;


import br.com.finalcraft.evernifecore.argumento.MultiArgumentos;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.Arg;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.FinalCMD;
import br.com.finalcraft.evernifecore.commands.finalcmd.custom.ICustomFinalCMD;
import br.com.finalcraft.evernifecore.commands.finalcmd.custom.contexts.CustomizeContext;
import br.com.finalcraft.evernifecore.scheduler.FCScheduler;
import br.com.finalcraft.finalchat.config.data.FancyPlayerData;
import br.com.finalcraft.finalchat.config.fancychat.FancyChannel;
import br.com.finalcraft.finalchat.messages.FChatMessages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class CMDInChannel implements ICustomFinalCMD {

    private final FancyChannel fancyChannel;

    public CMDInChannel(FancyChannel fancyChannel) {
        this.fancyChannel = fancyChannel;
    }

    @Override
    public void customize(@NotNull CustomizeContext context) {
        context.getFinalCMDData()
                .setPermission(fancyChannel.getPermission())
                .setLabels(
                        new String[]{fancyChannel.getName(), fancyChannel.getAlias()}
                );
    }


    @FinalCMD(
            aliases = ""
    )
    public void inChannel(Player player, FancyPlayerData playerData, @Arg(name = "[msg]") String message, MultiArgumentos argumentos){

        if (message == null){
            playerData.setLockChannel(fancyChannel);
            FChatMessages.CHANNEL_DEFINED_AS_YOUR_DEFAULT
                    .addPlaceholder("%channel_name%", fancyChannel.getName())
                    .send(player);
            return;
        }

        playerData.setTempChannel(fancyChannel);

        Set<Player> onlinePlayer = new HashSet(Bukkit.getOnlinePlayers());
        AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(true, player, argumentos.joinStringArgs(), onlinePlayer);
        FCScheduler.runAsync(() -> Bukkit.getServer().getPluginManager().callEvent(event));
    }
}
