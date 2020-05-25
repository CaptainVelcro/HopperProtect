package me.captainvelcro.hopperprotect.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import me.captainvelcro.hopperprotect.HopperProtect;
import net.md_5.bungee.api.chat.TranslatableComponent;

public class Messages {
	private YamlConfiguration messages = new YamlConfiguration();
	private HopperProtect plugin;
	
	public Messages(HopperProtect plugin, String file) {
		this.plugin = plugin;
		
		try {
			InputStream stream = this.plugin.getResource(file);
			InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
			messages.load(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public void broadcast(String path, Object... format) {
		Bukkit.getServer().spigot().broadcast(getComponent(path, format));
	}
	
	public void sendTo(CommandSender sender, String path, Object... format) {
		sender.spigot().sendMessage(getComponent(path, format));
	}
	
	public String get(String path) {
		return messages.getString("prefix") + messages.getString(path);
	}
	
	public TranslatableComponent getComponent(String path, Object... format) {
		return new TranslatableComponent(get(path), format);
	}
}
