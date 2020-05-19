package me.captainvelcro.hopperprotect;

import org.bukkit.plugin.java.JavaPlugin;

public class HopperProtect extends JavaPlugin {
	private PluginInitializer initializer = new PluginInitializer(this);
	
	@Override
	public void onLoad() {}
	
	@Override
	public void onEnable() {
		initializer.autoInit();
	}
	
	@Override
	public void onDisable() {}
}
