package me.CaptainVelcro.ActualPlugin;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import net.coreprotect.CoreProtectAPI;

public class Events implements Listener {
	private CoreProtectAPI api;

	public Events(CoreProtectAPI x) {
		api = x;
	}

	private boolean hopPlaced = false;
	private boolean hopPlacedM = false;
	private Player player = null;
	private boolean reported = false;

	@EventHandler
	public void onevent(BlockPlaceEvent event) {
		Player player = event.getPlayer();

		Block block = event.getBlock();// looking for hopper placement
		Material material = block.getType();
		// test to see if its a chest above so it doesnt always activate.
		Location temp = block.getLocation();// see if hopper is attaching to a chest
		temp.setY(temp.getY() + 1.0);
		Block ischest = temp.getBlock();
		Material fi = ischest.getType();// block above it

		Material bl = player.getInventory().getItemInMainHand().getType();// check to see if it is a hopper in their
																			// hand

		if (fi.equals(Material.CHEST) && material.equals(Material.HOPPER)) {

			player.sendMessage("hopper detected");
			hopPlaced = true;
			this.player = player;
			reported = false;
		}
		if (bl.equals(Material.HOPPER_MINECART)) {
			hopPlacedM = true;
			reported = false;
			player.sendMessage("hopper detected");

		}
	}

	@EventHandler
	public void playerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if (event.getAction().equals(event.getAction().RIGHT_CLICK_BLOCK)
				&& player.getInventory().getItemInMainHand().equals(Material.HOPPER_MINECART)) {
			player.sendMessage("NO");
		}
	}

	@EventHandler
	public void onInventoryMove(InventoryMoveItemEvent event) {
		event.setCancelled(true);
		if (hopPlaced == true && reported == false) {
			api.logContainerTransaction(player.getDisplayName(), player.getLocation());
			api.logInteraction(player.getDisplayName(), player.getLocation());
			reported = true;
			hopPlaced = false;
		}
		if (hopPlacedM == true && reported == false) {
			api.logContainerTransaction(player.getDisplayName(), player.getLocation());
			api.logInteraction(player.getDisplayName(), player.getLocation());
			reported = true;
			hopPlacedM = false;
		}

	}
}
