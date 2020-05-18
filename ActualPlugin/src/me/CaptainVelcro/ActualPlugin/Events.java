package me.CaptainVelcro.ActualPlugin;

import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.coreprotect.CoreProtectAPI;

public class Events implements Listener {
	private CoreProtectAPI api;
	private PlayersDatabase database;

	public Events(CoreProtectAPI x, PlayersDatabase database) {
		api = x;
		this.database = database;
		Gson lol = new GsonBuilder().setPrettyPrinting().create();
		System.out.println(lol.toJson(database));
	}

	private Player player = null;
	private Location hopperPlacerLoc;
	private Location hopperMPlacerLoc;
	private boolean isahopper = false;
	private boolean isaminecartHopper = false;

	public int findPlayer(UUID x) {
		for (int i = 0; i < database.getPlayerData().size(); i++) {
			if (database.getPlayerData().get(i).getUUID().equals(x)) {
				return i;
			}
		}
		return -1;
	}

	public boolean vectorLocked(Vector x) {
		if (database != null) {
			for (int i = 0; i < database.getPlayerData().size(); i++) {
				for (int j = 0; j < database.getPlayerData().get(i).getVectors().size(); j++) {
					if (database.getPlayerData().get(i).getVectors().get(j).equals(x)) {

						return true;
					}
				}
			}
		}
		return false;
	}
	@EventHandler
	public void onevent(BlockPlaceEvent event) {//does not work
		player = event.getPlayer();
		int playIndex = findPlayer(player.getUniqueId());
		Block firstx = event.getBlock();// looking for hopper placement
		
		if (firstx.getState() instanceof Chest) {
			
			Inventory inv = ((Chest) firstx.getState()).getInventory();
			if (inv instanceof DoubleChestInventory) {
				System.out.println("f");
				DoubleChestInventory doubleChestInv = (DoubleChestInventory) inv;
				if(vectorLocked(doubleChestInv.getRightSide().getLocation().toVector()) == true) {//find which side is locked
					database.getPlayerData().get(playIndex).addVector(doubleChestInv.getLeftSide().getLocation());	
					System.out.println("?");
				}else {
					database.getPlayerData().get(playIndex).addVector(doubleChestInv.getRightSide().getLocation());
					System.out.println("x");
				}

			}	
		}
	}
	@EventHandler
	public void onblockBreak(BlockBreakEvent event) {//does nto work
		Block temp = event.getBlock();
		Inventory inv = ((Chest) temp.getState()).getInventory();
		int playIndex = findPlayer(player.getUniqueId());
		if(inv instanceof Chest) {
			if(inv instanceof DoubleChestInventory) {
				DoubleChestInventory doubleChestInv = (DoubleChestInventory) inv;
				database.getPlayerData().get(playIndex).removeVector(doubleChestInv.getLeftSide().getLocation().toVector());
				database.getPlayerData().get(playIndex).removeVector(doubleChestInv.getRightSide().getLocation().toVector());	
			}else {
				//single chest
				database.getPlayerData().get(playIndex).removeVector(temp.getLocation().toVector());	
			}
		}
	}

	@EventHandler
	public void onInventoryMove(InventoryMoveItemEvent event) {
		Location temp = event.getSource().getLocation();
		if (vectorLocked(temp.toVector()) == true) {
			event.setCancelled(true);
		}
	}

}
