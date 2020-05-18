package me.CaptainVelcro.ActualPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

//core protect
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;

public class Main extends JavaPlugin {
	private PlayersDatabase database = new PlayersDatabase();
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private CoreProtectAPI api;
	private String main = "C:\\Users\\ianhi\\Desktop\\server test\\plugins\\data\\main.json";// change to wherever .json
																								// // needs to be
																								// outputted
	private String backup = "C:\\Users\\ianhi\\Desktop\\server test\\plugins\\data\\backup.json";

	@Override
	public void onEnable() {
		// override backup.json
//		Object object;
//		String temp = "";
//		try {
//			if(new File(main).exists()) {
//				object = gson.fromJson(new FileReader(main), Object.class);
//				 try(FileWriter file = new FileWriter("output.json")) {
//					 file.write(temp);
//					 file.flush();
//				 } catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				temp = object+"";
//			}		
//		} catch (JsonSyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JsonIOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
		Object object;

		File mainF = new File(main);// backup function
		File backupF = new File(backup);
		if (mainF.exists()) {
			try {
				copyFileUsingStream(mainF, backupF);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				object = gson.fromJson(new FileReader(main), Object.class);
				String temp = object + "";
				database = new PlayersDatabase();
				database = gson.fromJson(temp, PlayersDatabase.class);
			} catch (JsonSyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (JsonIOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		Coreprotect api = new Coreprotect(this.getServer());
		this.api = api.getAPI();
		this.getServer().getPluginManager().registerEvents(new Events(this.api, database), this);

	}

	@Override
	public void onDisable() {

	}

	public boolean isDatabase(UUID x) {

		for (int i = 0; i < database.getPlayerData().size(); i++) {
			if (database.getPlayerData().get(i).getUUID().equals(x)) {
				return false;
			}
		}
		return true;
	}

	public int findPlayer(UUID x) {
		for (int i = 0; i < database.getPlayerData().size(); i++) {
			if (database.getPlayerData().get(i).getUUID().equals(x)) {
				return i;
			}
		}
		return -1;
	}

	public boolean vectorLocked(Vector x) {
//		if(database.getPlayerData().get(i)..equals(x)) {
//			
//		}
		for (int i = 0; i < database.getPlayerData().size(); i++) {
			for (int j = 0; j < database.getPlayerData().get(i).getVectors().size(); j++) {
				if (database.getPlayerData().get(i).getVectors().get(j).equals(x)) {

					return true;
				}
			}
		}
		return false;
	}
	
	public boolean command(boolean bol, Player player) { //bol false = lock, true = unlock
		System.out.println(bol);
		Block firstx = player.getTargetBlock(null, 100);// player.getTargetBlockExact(player.getEyeLocation().getBlockX()).getBlockData().getMaterial();;
		Material first = firstx.getType();
		int playIndex = findPlayer(player.getUniqueId());
		if (first.equals(Material.CHEST)) {
			Inventory inv = ((Chest) firstx.getState()).getInventory();
			if (vectorLocked(player.getTargetBlock(null, 100).getLocation().toVector()) == bol
					&& !(inv instanceof DoubleChestInventory)) {
				if(bol == true) {
					System.out.println("remove single");
					database.getPlayerData().get(playIndex).removeVector(firstx.getLocation().toVector());
				}
				if(bol == false) {
					System.out.println("add single");
					database.getPlayerData().get(playIndex).addVector(player.getTargetBlock(null, 100).getLocation());
				}

				
				
			} else if (inv instanceof DoubleChestInventory) {
				DoubleChestInventory doubleChestInv = (DoubleChestInventory) inv;
				if (vectorLocked(doubleChestInv.getLeftSide().getLocation().toVector()) == bol
						&& vectorLocked(doubleChestInv.getRightSide().getLocation().toVector()) == bol) {
					if(bol == true) {
						System.out.println("remove double");
						database.getPlayerData().get(playIndex).removeVector(doubleChestInv.getLeftSide().getLocation().toVector());
						database.getPlayerData().get(playIndex).removeVector(doubleChestInv.getRightSide().getLocation().toVector());	
					}
					if(bol == false) {
						System.out.println("add double");
					database.getPlayerData().get(playIndex).addVector(doubleChestInv.getLeftSide().getLocation());
					database.getPlayerData().get(playIndex).addVector(doubleChestInv.getRightSide().getLocation());
					}
				}else {
					System.out.println("error on double");
					player.sendMessage(ChatColor.RED + "Invalid. Please do not lock others chests.");	
				}
			} else {
				System.out.println("general error");
				player.sendMessage(ChatColor.RED + "Invalid. Please do not lock others chests.");
			}
		}else {
			player.sendMessage(ChatColor.RED + "Please only lock chests.");
		}
		return true;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if (label.equalsIgnoreCase("Lock")) {
//			Block firstx = player.getTargetBlock(null, 100);// player.getTargetBlockExact(player.getEyeLocation().getBlockX()).getBlockData().getMaterial();;
//			Material first = firstx.getType();
//			int playIndex = findPlayer(player.getUniqueId());
//			if (first.equals(Material.CHEST)) {
//				Inventory inv = ((Chest) firstx.getState()).getInventory();
//				if (vectorLocked(player.getTargetBlock(null, 100).getLocation().toVector()) != true
//						&& !(inv instanceof DoubleChestInventory)) {
//					database.getPlayerData().get(playIndex).addVector(player.getTargetBlock(null, 100).getLocation());
//				} else if (inv instanceof DoubleChestInventory) {
//					DoubleChestInventory doubleChestInv = (DoubleChestInventory) inv;
//					if (vectorLocked(doubleChestInv.getLeftSide().getLocation().toVector()) != true
//							&& vectorLocked(doubleChestInv.getRightSide().getLocation().toVector()) != true) {
//						database.getPlayerData().get(playIndex).addVector(doubleChestInv.getLeftSide().getLocation());
//						database.getPlayerData().get(playIndex).addVector(doubleChestInv.getRightSide().getLocation());
//						System.out.println("???");
//					}else {
//						player.sendMessage(ChatColor.RED + "Invalid. Please do not lock others chests.");	
//					}
//				} else {
//					player.sendMessage(ChatColor.RED + "Invalid. Please do not lock others chests.");
//				}
//			}
//			return true;
			return command(false, player);
	}else if (label.equalsIgnoreCase("Unlock")) {
			return command(true, player);	
		} 
		else if (label.equalsIgnoreCase("Databaseadd") || label.equalsIgnoreCase("dbadd")) {
			Player target = getServer().getPlayer(args[0]);

			try {
				UUID x = target.getUniqueId();
				if (isDatabase(x) == true) {
					target.sendMessage(target.getDisplayName() + " and has been added");
					database.getPlayerData().add(new PlayersData(x));
				}
			} catch (Exception e) {
				target.sendMessage(ChatColor.RED + "Failed to add to database. Player already added");
			}
			return true;
		} else if (label.equalsIgnoreCase("upData")) {

			String str = gson.toJson(database);
			try (FileWriter file = new FileWriter(main)) {
				file.write(str);
				file.flush();
				player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Success! File saved.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				player.sendMessage(ChatColor.RED + "Invalid. Failed to write most likely.");
			}
			return true;
		} else if (label.equalsIgnoreCase("databaseview") | label.equalsIgnoreCase("dtbv")) {
			player.sendMessage(gson.toJson(database));
		}

		return false;
	}

	private static void copyFileUsingStream(File source, File dest) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(source);
			os = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} finally {
			is.close();
			os.close();
		}
	}

}
