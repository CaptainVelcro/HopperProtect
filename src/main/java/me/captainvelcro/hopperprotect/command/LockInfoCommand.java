package me.captainvelcro.hopperprotect.command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.captainvelcro.hopperprotect.HopperProtect;
import me.captainvelcro.hopperprotect.data.LockDatabase;
import me.captainvelcro.hopperprotect.data.Messages;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class LockInfoCommand implements CommandExecutor {
	private HopperProtect plugin;
	private Messages messages;
	private LockDatabase database;
	
	public LockInfoCommand(HopperProtect plugin) {
		this.plugin = plugin;
		messages = this.plugin.getMessages();
		database = this.plugin.getDatabase();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			Block target = player.getTargetBlock(null, 5);
			if (target.getState() instanceof Container) {
				UUID who = database.whoLocked(target.getLocation());
				if (who != null) {
					if (player.hasPermission("hopperprotect.info-all")) {
						Player whoPlayer = Bukkit.getPlayer(who);
						
						TextComponent hoverPlayer = new TextComponent(whoPlayer.getDisplayName());
						hoverPlayer.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(who.toString()).create()));
						
						messages.sendTo(sender, "locked-by", hoverPlayer);
					} else {
						messages.sendTo(sender, "locked");
					}
				} else {
					messages.sendTo(sender, "unlocked");
				}
			} else {
				messages.sendTo(sender, "error.no-inventory");
			}
		} else {
			messages.sendTo(sender, "error.console");
		}
		
		return true;
	}
}
