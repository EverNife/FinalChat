package br.com.finalcraft.finalchat.commands;


import br.com.finalcraft.evernifecore.argumento.MultiArgumentos;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.Arg;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.FinalCMD;
import br.com.finalcraft.evernifecore.locale.FCLocale;
import br.com.finalcraft.evernifecore.locale.LocaleMessage;
import br.com.finalcraft.evernifecore.locale.LocaleType;
import br.com.finalcraft.evernifecore.util.FCMessageUtil;
import br.com.finalcraft.finalchat.config.data.FancyPlayerData;
import br.com.finalcraft.finalchat.util.messages.PrivateMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CMDTell {

    @FinalCMD(
            aliases = {"ftell","tell","whispper","t","w","m","msg","private"}
    )
    public void tell(Player player, MultiArgumentos argumentos, @Arg(name = "<Player>") Player target, @Arg(name = "<msg>") String message){
        message = argumentos.joinStringArgs(1);

        if (!player.canSee(target)){
            FCMessageUtil.playerNotOnline(player, argumentos.getStringArg(0));
            return;
        }

        PrivateMessage.sendTell(player, target, message);
    }

    @FCLocale(lang = LocaleType.PT_BR, text = "§4§l ▶ §cVocê não tem ninguem para responder nesse momento!")
    @FCLocale(lang = LocaleType.EN_US, text = "§4§l ▶ §cYou do not have anyone to answer right now!")
    public static LocaleMessage YOU_DO_NOT_HAVE_ANYONE_TO_ASNWER;

    @FinalCMD(
            aliases = {"reply","responder","r"}
    )
    public void reply(Player player, FancyPlayerData playerData, MultiArgumentos argumentos, @Arg(name = "<msg>") String message){

        Player target = Bukkit.getPlayer(playerData.getLastWhisperer());

        if (target == null || !player.canSee(target)){
            YOU_DO_NOT_HAVE_ANYONE_TO_ASNWER.send(player);
            return;
        }

        message = argumentos.joinStringArgs(0);
        PrivateMessage.sendTell(player, target, message);
    }
}
