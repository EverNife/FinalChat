package br.com.finalcraft.finalchat.common.commands.argparser;

import br.com.finalcraft.evernifecore.commands.finalcmd.argument.ArgInfo;
import br.com.finalcraft.evernifecore.commands.finalcmd.argument.ArgParser;
import br.com.finalcraft.evernifecore.commands.finalcmd.argument.ParseCall;
import br.com.finalcraft.evernifecore.commands.finalcmd.argument.ParseResult;
import br.com.finalcraft.evernifecore.locale.FCLocale;
import br.com.finalcraft.evernifecore.locale.LocaleMessage;
import br.com.finalcraft.evernifecore.locale.LocaleType;
import br.com.finalcraft.finalchat.common.config.fancychat.FancyChannel;
import br.com.finalcraft.finalchat.common.config.fancychat.FancyChannelController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArgParserFancyChannel extends ArgParser<FancyChannel> {

    public ArgParserFancyChannel(ArgInfo argInfo) {
        super(argInfo);
    }

    @FCLocale(lang = LocaleType.PT_BR, text = "§4§l ▶ §cCanal §e${channel}§c não encontrado!")
    @FCLocale(lang = LocaleType.EN_US, text = "§4§l ▶ §cChannel §e${channel}§c not found!")
    public static LocaleMessage CHANNEL_NOT_FOUND;

    //The parser only says WHAT happened; the framework decides what an UNRECOGNIZED result costs on
    //a required argument versus an optional one.
    @Override
    public ParseResult<FancyChannel> parse(ParseCall call) {

        FancyChannel theChannel = FancyChannelController.getAllChannels().stream()
                .filter(fancyChannel -> call.getArgumento().equalsIgnoreCase(fancyChannel.getAlias(), fancyChannel.getName()))
                .filter(fancyChannel -> fancyChannel.getPermission().isEmpty() || call.getSender().hasPermission(fancyChannel.getPermission()))
                .findFirst()
                .orElse(null);

        return theChannel != null
                ? ParseResult.of(theChannel)
                : unrecognized(CHANNEL_NOT_FOUND.addPlaceholder("channel", call.getArgumento()));
    }

    @Override
    public List<String> tabComplete(TabContext context) {

        List<String> matched = new ArrayList<>();

        String lastWord = context.getLastWord();
        for (FancyChannel fancyChannel : FancyChannelController.getAllChannels()) {
            if ((fancyChannel.getPermission().isEmpty() || context.getSender().hasPermission(fancyChannel.getPermission()))
                    && fancyChannel.getName().regionMatches(true, 0, lastWord, 0, lastWord.length())) {
                matched.add(fancyChannel.getName());
            }
        }

        Collections.sort(matched, String.CASE_INSENSITIVE_ORDER);
        return matched;
    }
}
