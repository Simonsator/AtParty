package de.simonsator.partyandfriends.extensions.velocity.atparty.configuration;

import de.simonsator.partyandfriends.velocity.api.PAFExtension;
import de.simonsator.partyandfriends.velocity.utilities.ConfigurationCreator;

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
