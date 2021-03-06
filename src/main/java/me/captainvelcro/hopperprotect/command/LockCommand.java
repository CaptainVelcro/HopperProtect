package me.captainvelcro.hopperprotect.command;

import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.captainvelcro.hopperprotect.HopperProtect;
import me.captainvelcro.hopperprotect.data.LockDatabase;
import me.captainvelcro.hopperprotect.data.Messages;
import me.captainvelcro.hopperprotect.util.ChestUtil;

public class LockCommand implements CommandExecutor {
	private HopperProtect plugin;
	private Messages messages;
	private LockDatabase database;
	
	public LockCommand(HopperProtect plugin) {
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
				if (who == null) {
					database.lock(target.getLocation(), player.getUniqueId());
					Block opposite = ChestUtil.oppositeChest(target);
					if (opposite != null) {
						database.lock(opposite.getLocation(), player.getUniqueId());
					}
					messages.sendTo(sender, "lock");
				} else if (who.equals(player.getUniqueId()) || player.hasPermission("hopperprotect.unlock-all")) {
					database.unlock(target.getLocation());
					Block opposite = ChestUtil.oppositeChest(target);
					if (opposite != null) {
						database.unlock(opposite.getLocation());
					}
					messages.sendTo(sender, "unlock");
				} else {
					messages.sendTo(sender, "deny.unlock");
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
