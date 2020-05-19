package me.captainvelcro.hopperprotect.event;

import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.captainvelcro.hopperprotect.data.LockDatabase;
import me.captainvelcro.hopperprotect.util.Util;

public class LockEventListener implements Listener {
	private Plugin plugin;
	
	public LockEventListener(Plugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void blockPlace(BlockPlaceEvent event) {
		BukkitRunnable runnable = new BukkitRunnable() {
			@Override
			public void run() {
				Block block = event.getBlock();
				Block opposite = Util.oppositeChest(block);
				if (opposite != null) {
					UUID who = LockDatabase.whoLocked(opposite.getLocation());
					if (who != null) {
						LockDatabase.lock(block.getLocation(), who);
					}
				}
			}
		};
		
		runnable.runTaskLater(plugin, 1);
	}
	
	@EventHandler
	public void blockBreak(BlockBreakEvent event) {
		LockDatabase.unlock(event.getBlock().getLocation());
	}

	@EventHandler
	public void inventoryMoveItem(InventoryMoveItemEvent event) {
		Inventory source = event.getSource();
		if (source instanceof DoubleChestInventory) {
			source = ((DoubleChestInventory) source).getLeftSide();
		}
		if (LockDatabase.isLocked(source.getLocation())) {
			event.setCancelled(true);
		}
	}

}
