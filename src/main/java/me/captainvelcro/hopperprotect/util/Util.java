package me.captainvelcro.hopperprotect.util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;

public class Util {
	public static Block oppositeChest(Block block) {
		BlockState state = block.getState();
		if (state instanceof Chest) {
			Inventory inventory = ((Chest) state).getInventory();					
			if (inventory instanceof DoubleChestInventory) {
				DoubleChestInventory dcInventory = (DoubleChestInventory) inventory;
				
				Location original = block.getLocation();
				Location left = dcInventory.getLeftSide().getLocation();
				Location right = dcInventory.getRightSide().getLocation();
				
				if (original.equals(left)) {
					return right.getBlock();
				} else {
					return left.getBlock();
				}
			}
		}
		return null;
	}
}
