package me.captainvelcro.hopperprotect.command;

import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.captainvelcro.hopperprotect.data.LockDatabase;
import me.captainvelcro.hopperprotect.util.Util;

public class LockCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			Block target = player.getTargetBlock(null, 5);
			if (target.getState() instanceof Container) {
				UUID who = LockDatabase.whoLocked(target.getLocation());
				if (who == null) {
					LockDatabase.lock(target.getLocation(), player.getUniqueId());
					Block opposite = Util.oppositeChest(target);
					if (opposite != null) {
						LockDatabase.lock(opposite.getLocation(), player.getUniqueId());
					}
					player.sendMessage("Container locked!");
				} else if (who.equals(player.getUniqueId())) {
					LockDatabase.unlock(target.getLocation());
					Block opposite = Util.oppositeChest(target);
					if (opposite != null) {
						LockDatabase.unlock(opposite.getLocation());
					}
					player.sendMessage("Container unlocked!");
				} else {
					player.sendMessage("You cannot unlock containers locked by others.");
				}
			} else {
				player.sendMessage("You are not looking at a container.");
			}
		} else {
			sender.sendMessage("Only players can use this command.");
		}
		
		return true;
	}
}
