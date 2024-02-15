package de.simonsator.partyandfriends.extensions.velocity.atparty;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import de.simonsator.partyandfriends.velocity.VelocityExtensionLoadingInfo;
import de.simonsator.partyandfriends.velocity.main.PAFPlugin;

import java.nio.file.Path;

@Plugin(id = "at-party-loader", name = "At Party Loader", version = "1.0.3",
		description = "Loads At Party for Party and Friends", authors = {"Simonsator"}, dependencies = {@Dependency(id = "partyandfriends")})
public class APMainLoader {
	private final Path folder;
	private final ProxyServer server;

	@Inject
	public APMainLoader(ProxyServer server, @DataDirectory final Path folder) {
		this.server = server;
		this.folder = folder;
	}

	@Subscribe
	public void onProxyInitialization(ProxyInitializeEvent ignored) {

		APMain instance = new APMain(folder);

		PAFPlugin.loadExtension(new VelocityExtensionLoadingInfo(instance,
				"at-party-for-party-and-friends", "Loads At Party for Party and Friends", "1.0.3", "Simonsator"));

		server.getEventManager().register(this, instance);
	}

}