package de.simonsator.partyandfriends.extensions.velocity.atparty;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import de.simonsator.partyandfriends.extensions.velocity.atparty.configuration.APConfigLoader;
import de.simonsator.partyandfriends.velocity.api.PAFExtension;
import de.simonsator.partyandfriends.velocity.api.pafplayers.OnlinePAFPlayer;
import de.simonsator.partyandfriends.velocity.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.velocity.main.Main;
import de.simonsator.partyandfriends.velocity.party.command.PartyCommand;
import de.simonsator.partyandfriends.velocity.party.subcommand.Chat;
import de.simonsator.partyandfriends.velocity.utilities.ConfigurationCreator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Simonsator
 * @version 1.0.0 10.03.17
 */
public class APMain extends PAFExtension {
	private Chat chatCommand;
	private ArrayList<String> keyWords;

	public APMain(Path folder) {
		super(folder);
	}

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
			registerAsExtension();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return "AtParty";
	}


	@Subscribe(order = PostOrder.NORMAL)
	public void onPlayerChat(PlayerChatEvent event) {

		String keyWordUsed = getKeyWordUsed(event.getMessage());
		if (keyWordUsed == null) return;

		event.setResult(PlayerChatEvent.ChatResult.denied());

		getAdapter().runAsync(Main.getInstance(), () -> {
			OnlinePAFPlayer pPlayer = PAFPlayerManager.getInstance().getPlayer(event.getPlayer());
			chatCommand.onCommand(pPlayer, event.getMessage().substring(keyWordUsed.length()).split("\\s+"));
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
