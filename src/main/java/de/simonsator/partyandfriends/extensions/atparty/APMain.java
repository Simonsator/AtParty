package de.simonsator.partyandfriends.extensions.atparty;

import de.simonsator.partyandfriends.api.PAFExtension;
import de.simonsator.partyandfriends.api.pafplayers.OnlinePAFPlayer;
import de.simonsator.partyandfriends.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.extensions.atparty.configuration.APConfigLoader;
import de.simonsator.partyandfriends.main.Main;
import de.simonsator.partyandfriends.party.command.PartyCommand;
import de.simonsator.partyandfriends.party.subcommand.Chat;
import de.simonsator.partyandfriends.utilities.ConfigurationCreator;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Simonsator
 * @version 1.0.0 10.03.17
 */
public class APMain extends PAFExtension implements Listener {
	private Chat chatCommand;
	private ArrayList<String> keyWords;

	public void onEnable() {
		chatCommand = (Chat) PartyCommand.getInstance().getSubCommand(Chat.class);
		if (chatCommand == null) {
			printError("The party chat command needs to be enabled in the config of PAF in order to use this Extension.");
			return;
		}
		ConfigurationCreator config;
		try {
			config = new APConfigLoader(new File(getConfigFolder(), "config.yml"), this);
			List<String> configKeyWords = config.getStringList("KeyWord");
			keyWords = new ArrayList<>(configKeyWords.size());
			for (String keyWord : configKeyWords)
				keyWords.add(keyWord.toLowerCase());
			getAdapter().registerListener(this, this);
			registerAsExtension();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@EventHandler
	public void onMessage(ChatEvent pEvent) {
		if (!(pEvent.getSender() instanceof ProxiedPlayer))
			return;
		getAdapter().runAsync(Main.getInstance(), () -> {
			String keyWordUsed = getKeyWordUsed(pEvent.getMessage());
			if (keyWordUsed == null)
				return;
			OnlinePAFPlayer pPlayer = PAFPlayerManager.getInstance().getPlayer((ProxiedPlayer) pEvent.getSender());
			chatCommand.onCommand(pPlayer, pEvent.getMessage().substring(keyWordUsed.length()).split("\\s+"));
			pEvent.setCancelled(true);
		});
	}

	private String getKeyWordUsed(String pMessage) {
		pMessage = pMessage.toLowerCase();
		for (String keyWord : keyWords)
			if (pMessage.startsWith(keyWord))
				return keyWord;
		return null;
	}

	private void printError(String pMessage) {
		for (int i = 0; i < 20; i++)
			System.out.println(pMessage);
	}
}
