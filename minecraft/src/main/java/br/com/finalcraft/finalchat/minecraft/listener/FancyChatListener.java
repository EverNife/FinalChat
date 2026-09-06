package br.com.finalcraft.finalchat.minecraft.listener;

import br.com.finalcraft.evernifecore.api.common.player.FPlayer;
import br.com.finalcraft.evernifecore.listeners.base.ECListener;
import br.com.finalcraft.evernifecore.minecraft.api.MinecraftFPlayer;
import br.com.finalcraft.evernifecore.playerdata.PlayerController;
import br.com.finalcraft.evernifecore.scheduler.FCScheduler;
import br.com.finalcraft.finalchat.common.config.data.FancyPlayerData;
import br.com.finalcraft.finalchat.common.config.fancychat.FancyChannel;
import br.com.finalcraft.finalchat.common.util.messages.PublicMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * The door into the plugin on Bukkit: the vanilla chat event is cancelled and the line is rebuilt
 * and delivered by FinalChat instead.
 */
public class FancyChatListener implements ECListener {

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onChat(AsyncPlayerChatEvent event) {
		event.setCancelled(true);

		Player player = event.getPlayer();
		FPlayer fPlayer = MinecraftFPlayer.of(player);

		//Chat is a hot path: read the section already in memory rather than awaiting a future. RESIDENT
		//keeps it there for anyone online, so null here means the player is on their way out.
		FancyPlayerData playerData = PlayerController.getLoadedSection(player.getUniqueId(), FancyPlayerData.class);
		if (playerData == null) {
			return;
		}

		FancyChannel fancyChannel = playerData.extractPriorityChannel();
		String message = event.getMessage();

		if (event.isAsynchronous()) {
			PublicMessage.sendPublicMessage(fPlayer, fancyChannel, message);
		} else {
			FCScheduler.runAsync(() -> PublicMessage.sendPublicMessage(fPlayer, fancyChannel, message));
		}
	}

}
