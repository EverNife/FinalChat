package br.com.finalcraft.finalchat.common.messages;

import br.com.finalcraft.evernifecore.locale.FCLocale;
import br.com.finalcraft.evernifecore.locale.LocaleMessage;
import br.com.finalcraft.evernifecore.locale.LocaleType;

public class FChatMessages {

    @FCLocale(lang = LocaleType.PT_BR, text = "§4§l ▶ §cVocê está mutado!\n${reason}")
    @FCLocale(lang = LocaleType.EN_US, text = "§4§l ▶ §cYou are muted!\n${reason}")
    public static LocaleMessage YOU_ARE_MUTED;

    @FCLocale(lang = LocaleType.PT_BR, text = "§6§l ▶ §aCanal §e[${channel_name}]§a definido como padrão!")
    @FCLocale(lang = LocaleType.EN_US, text = "§6§l ▶ §aChannel §e[${channel_name}]§a defined as your default.")
    public static LocaleMessage CHANNEL_DEFINED_AS_YOUR_DEFAULT;

    //Was a hardcoded Portuguese string inside MuteUtil; the text is the operator's, so it moves as-is.
    @FCLocale(lang = LocaleType.PT_BR, text = "§c  §l(GlobalMute está ativado!)")
    public static LocaleMessage GLOBAL_MUTE_REASON;

    //Was a hardcoded Portuguese string inside the public-message delivery.
    @FCLocale(lang = LocaleType.PT_BR, text = "§6§l ▶ §cNão tem ninguém perto de você para receber essa mensagem...")
    public static LocaleMessage NOBODY_CLOSE_ENOUGH;

}
