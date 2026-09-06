package br.com.finalcraft.finalchat.common.commands;

import br.com.finalcraft.evernifecore.api.common.commandsender.FCommandSender;
import br.com.finalcraft.evernifecore.api.common.player.FPlayer;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.Arg;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.FinalCMD;
import br.com.finalcraft.evernifecore.ecplugin.ECPluginManager;
import br.com.finalcraft.evernifecore.locale.FCLocale;
import br.com.finalcraft.evernifecore.locale.LocaleMessage;
import br.com.finalcraft.evernifecore.locale.LocaleType;
import br.com.finalcraft.evernifecore.util.FCColorUtil;
import br.com.finalcraft.finalchat.common.FinalChat;
import br.com.finalcraft.finalchat.common.PermissionNodes;
import br.com.finalcraft.finalchat.common.util.messages.SpyMessage;

@FinalCMD(
        aliases = {"finalchat", "chat"},
        helpHeader = "§6(  §a§lFinalChat§e  §6)§m"
)
public class CoreCommand {

    @FCLocale(lang = LocaleType.EN_US, text = "§6§l ▶ §aChatSpy Status: ${spy_status}")
    public static LocaleMessage CHAT_SPY_STATUS;

    @FCLocale(lang = LocaleType.PT_BR, text = "§6§l ▶ §aChatSpy Ativado!")
    public static LocaleMessage CHAT_SPY_ENABLED;

    @FCLocale(lang = LocaleType.PT_BR, text = "§6§l ▶ §eChatSpy Desativado!")
    public static LocaleMessage CHAT_SPY_DISABLED;

    @FinalCMD.SubCMD(
            subcmd = "spy",
            permission = PermissionNodes.COMMAND_SPY
    )
    public void spy(FPlayer player, @Arg("[ON|OFF]") String operation, @Arg("[color]") String color) {

        if (operation == null) {
            CHAT_SPY_STATUS
                    .addPlaceholder("spy_status", SpyMessage.isSpying(player) ? "§aAtivado" : "§cDesativado")
                    .send(player);
            return;
        }

        if (color == null) {
            color = "§7";
        }

        color = FCColorUtil.colorfy(color);

        switch (operation.toLowerCase()) {
            case "on":
                CHAT_SPY_ENABLED.send(player);
                SpyMessage.changeSpyState(player, color, true);
                return;
            case "off":
                CHAT_SPY_DISABLED.send(player);
                SpyMessage.changeSpyState(player, color, false);
                return;
        }
    }


    @FinalCMD.SubCMD(
            subcmd = "reload",
            permission = PermissionNodes.COMMAND_RELOAD
    )
    public void reload(FCommandSender sender) {
        ECPluginManager.reloadPlugin(sender, FinalChat.get().getPluginData());
    }
}
