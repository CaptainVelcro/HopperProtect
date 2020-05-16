package me.CaptainVelcro.ActualPlugin;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;

public class Coreprotect {
	private Server server;
	private CoreProtectAPI api;
	public Coreprotect(Server x) {
		this.server = x;
		 api = getCoreProtect();
		if (api != null) { // Ensure we have access to the API
			api.testAPI(); // Will print out "[CoreProtect] API Test Successful." in the console.
		}
	}
	private CoreProtectAPI getCoreProtect() {
		Plugin plugin = server.getPluginManager().getPlugin("CoreProtect");

		// Check that CoreProtect is loaded
		if (plugin == null || !(plugin instanceof CoreProtect)) {
			return null;
		}

		// Check that the API is enabled
		CoreProtectAPI CoreProtect = ((CoreProtect) plugin).getAPI();
		if (CoreProtect.isEnabled() == false) {
			return null;
		}

		// Check that a compatible version of the API is loaded
		if (CoreProtect.APIVersion() < 6) {
			return null;
		}

		return CoreProtect;
	}
	public CoreProtectAPI getAPI() {
		return api;
	}
}
