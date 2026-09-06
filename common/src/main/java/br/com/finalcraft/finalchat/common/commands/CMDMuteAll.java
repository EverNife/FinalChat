package br.com.finalcraft.finalchat.common.commands;


import br.com.finalcraft.evernifecore.api.common.commandsender.FCommandSender;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.Arg;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.FinalCMD;
import br.com.finalcraft.evernifecore.locale.FCLocale;
import br.com.finalcraft.evernifecore.locale.LocaleMessage;
import br.com.finalcraft.evernifecore.locale.LocaleType;
import br.com.finalcraft.finalchat.common.PermissionNodes;
import br.com.finalcraft.finalchat.common.util.MuteUtil;

public class CMDMuteAll {

    @FCLocale(lang = LocaleType.PT_BR, text = "\u00a76\u00a7l \u25b6 \u00a7a\u00a7lGlobalMute Ativado!")
    @FCLocale(lang = LocaleType.EN_US, text = "\u00a76\u00a7l \u25b6 \u00a7a\u00a7lGlobalMute Enabled!")
    public static LocaleMessage GLOBAL_MUTE_ENABLED;

    @FCLocale(lang = LocaleType.PT_BR, text = "\u00a76\u00a7l \u25b6 \u00a7a\u00a7lGlobalMute Desativado!")
    @FCLocale(lang = LocaleType.EN_US, text = "\u00a76\u00a7l \u25b6 \u00a7a\u00a7lGlobalMute Disabled!")
    public static LocaleMessage GLOBAL_MUTE_DISABLED;

    @FinalCMD(
            aliases = "muteall",
            permission = PermissionNodes.COMMAND_MUTE_ALL
    )
    public void mute(FCommandSender sender, @Arg("<On|Off>") Boolean mute) {
        if (MuteUtil.toggleGlobalMute(mute)) {
            GLOBAL_MUTE_ENABLED.send(sender);
        } else {
            GLOBAL_MUTE_DISABLED.send(sender);
        }
    }

}
