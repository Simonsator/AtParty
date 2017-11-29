package de.simonsator.partyandfriends.extensions.atparty.configuration;

import de.simonsator.partyandfriends.utilities.ConfigurationCreator;

import java.io.File;
import java.io.IOException;

public class APConfigLoader extends ConfigurationCreator {
	public APConfigLoader(File file) throws IOException {
		super(file);
		readFile();
		loadDefaults();
		saveFile();
	}

	private void loadDefaults() {
		set("KeyWord", "@party ", "@p ");
	}

	@Override
	public void reloadConfiguration() throws IOException {
		configuration = (new APConfigLoader(FILE)).getCreatedConfiguration();
	}
}
