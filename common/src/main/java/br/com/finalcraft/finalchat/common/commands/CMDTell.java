package br.com.finalcraft.finalchat.common.commands;


import br.com.finalcraft.evernifecore.EverNifeCore;
import br.com.finalcraft.evernifecore.api.common.player.FPlayer;
import br.com.finalcraft.evernifecore.argumento.MultiArgumentos;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.Arg;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.FinalCMD;
import br.com.finalcraft.evernifecore.locale.FCLocale;
import br.com.finalcraft.evernifecore.locale.LocaleMessage;
import br.com.finalcraft.evernifecore.locale.LocaleType;
import br.com.finalcraft.evernifecore.util.FCMessageUtil;
import br.com.finalcraft.finalchat.common.config.data.FancyPlayerData;
import br.com.finalcraft.finalchat.common.platform.ChatPlatformBridge;
import br.com.finalcraft.finalchat.common.util.messages.PrivateMessage;

public class CMDTell {

    @FinalCMD(
            aliases = {"ftell", "tell", "whispper", "t", "w", "m", "msg", "private"}
    )
    public void tell(FPlayer player, MultiArgumentos argumentos, @Arg("<Player>") FPlayer target, @Arg("<msg>") String message) {
        message = argumentos.joinStringArgs(1);

        if (!ChatPlatformBridge.get().canSee(player, target)) {
            FCMessageUtil.playerNotOnline(player, argumentos.getStringArg(0));
            return;
        }

        PrivateMessage.sendTell(player, target, message);
    }

    @FCLocale(lang = LocaleType.PT_BR, text = "§4§l ▶ §cVocê não tem ninguem para responder nesse momento!")
    @FCLocale(lang = LocaleType.EN_US, text = "§4§l ▶ §cYou do not have anyone to answer right now!")
    public static LocaleMessage YOU_DO_NOT_HAVE_ANYONE_TO_ASNWER;

    @FinalCMD(
            aliases = {"reply", "responder", "r"}
    )
    public void reply(FPlayer player, FancyPlayerData playerData, MultiArgumentos argumentos, @Arg("<msg>") String message) {

        FPlayer target = playerData.getLastWhisperer() == null
                ? null
                : EverNifeCore.getPlatform().getPlayer(playerData.getLastWhisperer());

        if (target == null || !ChatPlatformBridge.get().canSee(player, target)) {
            YOU_DO_NOT_HAVE_ANYONE_TO_ASNWER.send(player);
            return;
        }

        message = argumentos.joinStringArgs(0);
        PrivateMessage.sendTell(player, target, message);
    }
}
