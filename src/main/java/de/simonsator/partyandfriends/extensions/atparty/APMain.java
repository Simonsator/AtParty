package de.simonsator.partyandfriends.extensions.atparty;

import de.simonsator.partyandfriends.api.PAFExtension;
import de.simonsator.partyandfriends.api.pafplayers.OnlinePAFPlayer;
import de.simonsator.partyandfriends.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.main.Main;
import de.simonsator.partyandfriends.party.command.PartyCommand;
import de.simonsator.partyandfriends.party.subcommand.Chat;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * @author Simonsator
 * @version 1.0.0 10.03.17
 */
public class APMain extends PAFExtension implements Listener {
	private Chat chatCommand;

	public void onEnable() {
		Main.getInstance().registerExtension(this);
		chatCommand = (Chat) PartyCommand.getInstance().getSubCommand(Chat.class);
		if (chatCommand == null) {
			printError("The party chat command needs to be enabled in the config of PAF in order to use this Extension.");
			return;
		}
		getProxy().getPluginManager().registerListener(this, this);
	}

	@EventHandler
	public void onMessage(ChatEvent pEvent) {
		if (!(pEvent.getSender() instanceof ProxiedPlayer))
			return;
		if (!pEvent.getMessage().toLowerCase().startsWith("@party "))
			return;
		OnlinePAFPlayer pPlayer = PAFPlayerManager.getInstance().getPlayer((ProxiedPlayer) pEvent.getSender());
		chatCommand.onCommand(pPlayer, pEvent.getMessage().substring(7).split("\\s+"));
		pEvent.setCancelled(true);
	}

	private void printError(String pMessage) {
		for (int i = 0; i < 20; i++)
			System.out.println(pMessage);
	}

	@Override
	public void reload() {

	}
}
