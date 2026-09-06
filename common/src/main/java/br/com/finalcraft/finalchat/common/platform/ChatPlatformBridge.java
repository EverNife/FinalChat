package br.com.finalcraft.finalchat.common.platform;

import br.com.finalcraft.evernifecore.api.common.player.FPlayer;

/**
 * The two things chat delivery needs that the agnostic API does not offer: whether one player is
 * allowed to know another is online, and the little sound that tells someone they were mentioned.
 *
 * <p>The base class answers both in the only way an agnostic module can, so a platform that has
 * neither concept still works; a platform that does have them installs a subclass at enable.</p>
 */
public class ChatPlatformBridge {

    private static volatile ChatPlatformBridge current = new ChatPlatformBridge();

    public static ChatPlatformBridge get() {
        return current;
    }

    /** Installs a platform's bridge, or restores the agnostic one when given {@code null}. */
    public static void use(ChatPlatformBridge bridge) {
        current = bridge != null ? bridge : new ChatPlatformBridge();
    }

    /**
     * Whether {@code viewer} may see {@code target} at all - the guard behind "player is not
     * online" for someone hidden or vanished. Without a platform notion of visibility, being
     * online is all there is to check.
     */
    public boolean canSee(FPlayer viewer, FPlayer target) {
        return target != null && target.isOnline();
    }

    /** Plays the mention cue for the player whose name appeared in a message. Silent by default. */
    public void playMentionSound(FPlayer player) {
    }

    /** The chat line as this platform's console can render it. */
    public String forConsoleLog(String line) {
        return line;
    }
}
