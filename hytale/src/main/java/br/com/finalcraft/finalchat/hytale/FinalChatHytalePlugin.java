package br.com.finalcraft.finalchat.hytale;

import br.com.finalcraft.evernifecore.ecplugin.annotations.ECPlugin;
import br.com.finalcraft.evernifecore.hytale.ecplugin.ECHytalePlugin;
import br.com.finalcraft.finalchat.common.FinalChatBootstrap;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

/**
 * Hytale entry point. Everything it runs comes from {@link FinalChatBootstrap}: the configurable
 * private message ({@code /ftell}, {@code /reply}) with its TellTag format, hover and staff spy,
 * plus the channel commands, all of which speak only the agnostic API.
 *
 * <p>What this platform does NOT have is the door into PUBLIC chat: there is no portable "player
 * spoke" event, so the vanilla chat line is untouched here and only messages sent through a channel
 * command are formatted by FinalChat. {@code /muteall} guards the same public path and is therefore
 * inert until that door exists.</p>
 */
@ECPlugin
public class FinalChatHytalePlugin extends ECHytalePlugin implements FinalChatBootstrap {

    public FinalChatHytalePlugin(JavaPluginInit init) {
        super(init);
    }
}
