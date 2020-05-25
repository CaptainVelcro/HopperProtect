package me.captainvelcro.hopperprotect;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.captainvelcro.hopperprotect.command.LockCommand;
import me.captainvelcro.hopperprotect.command.LockInfoCommand;
import me.captainvelcro.hopperprotect.data.LockDatabase;
import me.captainvelcro.hopperprotect.data.Messages;
import me.captainvelcro.hopperprotect.event.LockEventListener;

public class HopperProtect extends JavaPlugin {
	private Messages messages;
	private LockDatabase database;
	
	@Override
	public void onLoad() {
	}
	
	@Override
	public void onEnable() {
		initData();
		initEvents();
		initCommands();
	}
	
	@Override
	public void onDisable() {
		database.close();
	}
	
	public Messages getMessages() {
		return messages;
	}
	
	public LockDatabase getDatabase() {
		return database;
	}
	
	// init
	private void initData() {
		saveDefaultConfig();
		
		messages = new Messages(this, "messages.yml");
		
		String dataPath = getDataFolder().getPath();
		String databasePath = dataPath+"\\lock.json";
		boolean prettyPrinting = getConfig().getBoolean("json-pretty-printing");
		database = new LockDatabase(databasePath, prettyPrinting);
	}
	
	private void initEvents() {
		registerEventListener(new LockEventListener(this));
	}
	
	private void initCommands() {
		registerCommand("lock", new LockCommand(this));
		registerCommand("lockinfo", new LockInfoCommand(this));
	}
	
	private void registerEventListener(Listener listener) {
		getServer().getPluginManager().registerEvents(listener, this);
	}
	
	private void registerCommand(String name, CommandExecutor executor) {
		getCommand(name).setExecutor(executor);
	}
}
