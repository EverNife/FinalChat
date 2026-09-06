package br.com.finalcraft.finalchat.commands;

import br.com.finalcraft.evernifecore.argumento.MultiArgumentos;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.Arg;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.FinalCMD;
import br.com.finalcraft.evernifecore.ecplugin.ECPluginManager;
import br.com.finalcraft.evernifecore.locale.FCLocale;
import br.com.finalcraft.evernifecore.locale.LocaleMessage;
import br.com.finalcraft.evernifecore.locale.LocaleType;
import br.com.finalcraft.finalchat.FinalChat;
import br.com.finalcraft.finalchat.PermissionNodes;
import br.com.finalcraft.finalchat.util.messages.SpyMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@FinalCMD(
        aliases = {"finalchat","chat"},
        helpHeader = "§6(  §a§lFinalChat§e  §6)§m"
)
public class CoreCommand {

    @FCLocale(lang = LocaleType.EN_US, text = "§6§l ▶ §aChatSpy Status: %spy_status%")
    public static LocaleMessage CHAT_SPY_STATUS;

    @FinalCMD.SubCMD(
            subcmd = "spy",
            permission = PermissionNodes.COMMAND_SPY
    )
    public void spy(Player player, MultiArgumentos argumentos, @Arg(name = "[ON|OFF]") String operation, @Arg(name = "[color]") String color){

        if (operation == null){
            CHAT_SPY_STATUS
                    .addPlaceholder("%spy_status%", SpyMessage.isSpying(player) ? "§aAtivado" : "§cDesativado")
                    .send(player);
            return;
        }

        if (color == null){
            color = "§7";
        }

        color = ChatColor.translateAlternateColorCodes('&',color);

        switch (argumentos.get(1).toLowerCase()){
            case "on":
                player.sendMessage("§6§l ▶ §aChatSpy Ativado!");
                SpyMessage.changeSpyState(player,color,true);
                return;
            case "off":
                player.sendMessage("§6§l ▶ §eChatSpy Desativado!");
                SpyMessage.changeSpyState(player,color,false);
                return;
        }
    }


    @FinalCMD.SubCMD(
            subcmd = "reload",
            permission = PermissionNodes.COMMAND_RELOAD
    )
    public void reload(CommandSender sender){
        ECPluginManager.reloadPlugin(sender, FinalChat.instance);
    }
}
