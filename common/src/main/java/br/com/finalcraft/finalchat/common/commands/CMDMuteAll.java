package br.com.finalcraft.finalchat.commands;


import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.Arg;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.FinalCMD;
import br.com.finalcraft.evernifecore.locale.FCLocale;
import br.com.finalcraft.evernifecore.locale.LocaleMessage;
import br.com.finalcraft.evernifecore.locale.LocaleType;
import br.com.finalcraft.finalchat.PermissionNodes;
import br.com.finalcraft.finalchat.util.MuteUtil;
import org.bukkit.command.CommandSender;

public class CMDMuteAll {

    @FCLocale(lang = LocaleType.PT_BR, text = "§6§l ▶ §a§lGlobalMute Ativado!")
    @FCLocale(lang = LocaleType.EN_US, text = "§6§l ▶ §a§lGlobalMute Enabled!")
    public static LocaleMessage GLOBAL_MUTE_ENABLED;

    @FCLocale(lang = LocaleType.PT_BR, text = "§6§l ▶ §a§lGlobalMute Desativado!")
    @FCLocale(lang = LocaleType.EN_US, text = "§6§l ▶ §a§lGlobalMute Disabled!")
    public static LocaleMessage GLOBAL_MUTE_DISABLED;

    @FinalCMD(
            aliases = "muteall",
            permission = PermissionNodes.COMMAND_MUTE_ALL
    )
    public void mute(CommandSender sender, @Arg(name = "<On|Off>") Boolean mute){
        if (MuteUtil.toggleGlobalMute(mute)){
            GLOBAL_MUTE_ENABLED.send(sender);
        }else {
            GLOBAL_MUTE_DISABLED.send(sender);
        }
    }

}
