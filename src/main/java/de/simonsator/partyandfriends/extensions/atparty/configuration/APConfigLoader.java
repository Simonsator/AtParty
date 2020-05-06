package de.simonsator.partyandfriends.extensions.atparty.configuration;

import de.simonsator.partyandfriends.api.PAFExtension;
import de.simonsator.partyandfriends.utilities.ConfigurationCreator;

import java.io.File;
import java.io.IOException;

public class APConfigLoader extends ConfigurationCreator {
	public APConfigLoader(File file, PAFExtension pPlugin) throws IOException {
		super(file, pPlugin);
		readFile();
		loadDefaults();
		saveFile();
	}

	private void loadDefaults() {
		set("KeyWord", "@party ", "@p ");
	}
}
