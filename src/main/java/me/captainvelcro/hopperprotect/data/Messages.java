package me.captainvelcro.hopperprotect.data;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import net.md_5.bungee.api.chat.TranslatableComponent;

public class Messages {
	private YamlConfiguration messages = new YamlConfiguration();
	
	public Messages(String messagesPath) {
		try {
			messages.load(messagesPath);
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
		return get(path, true);
	}
	
	public String get(String path, boolean prefix) {
		if (prefix && path != "prefix") {
			return messages.getString("prefix") + messages.getString(path);
		} else {
			return messages.getString(path);
		}
	}
	
	public TranslatableComponent getComponent(String path, Object... format) {
		return new TranslatableComponent(get(path), format);
	}
}
