package br.com.finalcraft.finalchat.listener;

import br.com.finalcraft.evernifecore.api.events.ECFullyLoggedInEvent;
import br.com.finalcraft.evernifecore.config.playerdata.PlayerController;
import br.com.finalcraft.evernifecore.listeners.base.ECListener;
import br.com.finalcraft.evernifecore.scheduler.FCScheduler;
import br.com.finalcraft.finalchat.config.data.FancyPlayerData;
import br.com.finalcraft.finalchat.config.fancychat.FancyChannel;
import br.com.finalcraft.finalchat.util.ChannelManager;
import br.com.finalcraft.finalchat.util.messages.PublicMessage;
import br.com.finalcraft.finalchat.util.messages.SpyMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class FancyChatListener implements ECListener {

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onChat(AsyncPlayerChatEvent event){
		event.setCancelled(true);

		Player player = event.getPlayer();

		FancyPlayerData playerData = PlayerController.getPDSection(player, FancyPlayerData.class);

		FancyChannel fancyChannel = playerData.extractPriorityChannel();

		if (event.isAsynchronous()){
			PublicMessage.sendPublicMessage(player,fancyChannel,event.getMessage());
		}else {
			FCScheduler.runAsync(() -> {
				PublicMessage.sendPublicMessage(player,fancyChannel,event.getMessage());
			});
		}
	}

	@EventHandler
	public void onJoin(ECFullyLoggedInEvent e){
		ChannelManager.playerJoined(e.getPlayerData().getPlayer());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		ChannelManager.playerLeaved(e.getPlayer());
		SpyMessage.changeSpyState(e.getPlayer(), "", false);
	}

}
