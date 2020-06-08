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
import org.bukkit.scheduler.BukkitRunnable;

import me.captainvelcro.hopperprotect.HopperProtect;
import me.captainvelcro.hopperprotect.data.LockDatabase;
import me.captainvelcro.hopperprotect.util.ChestUtil;

public class LockEventListener implements Listener {
	private HopperProtect plugin;
	private LockDatabase database;
	
	public LockEventListener(HopperProtect plugin) {
		this.plugin = plugin;
		database = this.plugin.getDatabase();
	}
	
	@EventHandler
	public void blockPlace(BlockPlaceEvent event) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Block block = event.getBlock();
				Block opposite = ChestUtil.oppositeChest(block);
				if (opposite != null) {
					UUID who = database.whoLocked(opposite.getLocation());
					if (who != null) {
						database.lock(block.getLocation(), who);
					}
				}
			}
		}.runTask(plugin);
	}
	
	@EventHandler
	public void blockBreak(BlockBreakEvent event) {
		database.unlock(event.getBlock().getLocation());
	}

	@EventHandler
	public void inventoryMoveItem(InventoryMoveItemEvent event) {
		Inventory source = event.getSource();
		if (source instanceof DoubleChestInventory) {
			source = ((DoubleChestInventory) source).getLeftSide();
		}
		if (database.isLocked(source.getLocation())) {
			event.setCancelled(true);
		}
	}

}
