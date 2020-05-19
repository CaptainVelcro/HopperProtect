package me.captainvelcro.hopperprotect.data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class LockDatabase {
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static List<LockData> database = new ArrayList<LockData>();
	private static Type databaseType = new TypeToken<List<LockData>>(){}.getType();
	private static String jsonPath;
	
	public static void loadDatabase(String jsonPath1) {
		jsonPath = jsonPath1;
		try {
			database = gson.fromJson(new FileReader(jsonPath), databaseType);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveDatabase() {
		String json = gson.toJson(database);
		try {
			FileWriter writer = new FileWriter(jsonPath);
			writer.write(json);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void lock(Location location, UUID player) {
		LockData data = new LockData();
		data.vector = location.toVector();
		data.world = location.getWorld().getUID();
		data.player = player;
		
		database.add(data);
		
		saveDatabase();
	}
	
	public static void unlock(Location location) {
		Vector vector = location.toVector();
		UUID world = location.getWorld().getUID();
		
		for (LockData data : database) {
			if (data.vector.equals(vector) && data.world.equals(world)) {
				database.remove(data);
				break;
			}
		}
		
		saveDatabase();
	}
	
	public static UUID whoLocked(Location location) {
		Vector vector = location.toVector();
		UUID world = location.getWorld().getUID();
		
		for (LockData data : database) {
			if (data.vector.equals(vector) && data.world.equals(world)) {
				return data.player;
			}
		}
		
		return null;
	}
	
	public static boolean isLocked(Location location) {
		return whoLocked(location) != null;
	}
}
