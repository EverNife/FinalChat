package br.com.finalcraft.finalchat.common.api;

import br.com.finalcraft.evernifecore.api.common.commandsender.FCommandSender;
import br.com.finalcraft.evernifecore.api.events.base.ECCancellable;
import br.com.finalcraft.evernifecore.api.events.base.ECEvent;
import br.com.finalcraft.evernifecore.api.events.base.IECEvent;
import br.com.finalcraft.finalchat.common.config.fancychat.FancyChannel;

/**
 * Fired once per public chat line, before it is rendered and delivered. Cancelling it drops the
 * message; rewriting it through {@link #setMessage(String)} changes what every recipient sees.
 *
 * <p>{@code extends ECEvent} is what makes it visible to the server - on Bukkit that base IS an
 * {@code org.bukkit.event.Event}, so an existing {@code @EventHandler} on this type keeps working -
 * and {@code implements IECEvent} is what lets it compile in the agnostic module.</p>
 */
public class FinalChatSendChannelMessageEvent extends ECEvent implements IECEvent, ECCancellable {

    /** Bukkit-only: this event's own native handler list, so listening to it does not gate the whole EC family. */
    public static Object getHandlerList() {
        return ECEvent.getHandlerListOf(FinalChatSendChannelMessageEvent.class);
    }

    private final FCommandSender sender;
    private String msg;
    private final FancyChannel channel;
    private boolean cancelled;

    public FinalChatSendChannelMessageEvent(FCommandSender sender, FancyChannel channel, String msg) {
        this.sender = sender;
        this.msg = msg;
        this.channel = channel;
    }

    public FancyChannel getChannel() {
        return this.channel;
    }

    public void setMessage(String newMsg) {
        this.msg = newMsg;
    }

    public String getMessage() {
        return this.msg;
    }

    public FCommandSender getSender() {
        return this.sender;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

}
