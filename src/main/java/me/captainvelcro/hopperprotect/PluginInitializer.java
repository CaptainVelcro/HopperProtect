package me.captainvelcro.hopperprotect;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.captainvelcro.hopperprotect.command.LockCommand;
import me.captainvelcro.hopperprotect.data.LockDatabase;
import me.captainvelcro.hopperprotect.event.LockEventListener;

public class PluginInitializer {
	private JavaPlugin plugin;
	
	public PluginInitializer(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	public void autoInit() {
		initData();
		initEvents();
		initCommands();
	}
	
	public void initData() {
		plugin.saveDefaultConfig();
		String dataPath = plugin.getDataFolder().getPath();
		String jsonPath = dataPath+"\\lock.json";
		File jsonFile = new File(jsonPath);
		if (!jsonFile.exists()) {
			try {
				jsonFile.createNewFile();
				FileWriter writer = new FileWriter(jsonPath);
				writer.write("[]");
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LockDatabase.loadDatabase(jsonPath);
	}
	
	public void initEvents() {
		registerEventListener(new LockEventListener(plugin));
	}
	
	public void initCommands() {
		registerCommand("lock", new LockCommand());
	}
	
	private void registerEventListener(Listener listener) {
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
	}
	
	private void registerCommand(String name, CommandExecutor executor) {
		plugin.getCommand(name).setExecutor(executor);
	}
}
