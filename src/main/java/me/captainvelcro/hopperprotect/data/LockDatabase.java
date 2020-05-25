package me.captainvelcro.hopperprotect.data;

import java.io.File;
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
	private static Type databaseType = new TypeToken<List<LockData>>(){}.getType();

	private Gson gson;
	private String databasePath;
	private List<LockData> database = new ArrayList<LockData>();
	
	public LockDatabase(String databasePath, boolean prettyPrinting) {
		this.databasePath = databasePath;
		
		GsonBuilder builder = new GsonBuilder();
		if (prettyPrinting) {
			builder.setPrettyPrinting();
		}
		gson = builder.create();
		
		load();
	}
	
	private void load() {
		File jsonFile = new File(databasePath);
		if (!jsonFile.exists()) {
			try {
				jsonFile.createNewFile();
				save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				database = gson.fromJson(new FileReader(databasePath), databaseType);
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			} catch (JsonIOException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void save() {
		String json = gson.toJson(database);
		try {
			FileWriter writer = new FileWriter(databasePath);
			writer.write(json);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		save();
	}
	
	public void lock(Location location, UUID player) {
		LockData data = new LockData();
		data.vector = location.toVector();
		data.world = location.getWorld().getUID();
		data.player = player;
		
		database.add(data);
		
		save();
	}
	
	public void unlock(Location location) {
		Vector vector = location.toVector();
		UUID world = location.getWorld().getUID();
		
		int index = 0;
		for (LockData data : database) {
			if (data.vector.equals(vector) && data.world.equals(world)) {
				database.remove(index);
				break;
			}
			index++;
		}
		
		save();
	}
	
	public UUID whoLocked(Location location) {
		Vector vector = location.toVector();
		UUID world = location.getWorld().getUID();
		
		for (LockData data : database) {
			if (data.vector.equals(vector) && data.world.equals(world)) {
				return data.player;
			}
		}
		
		return null;
	}
	
	public boolean isLocked(Location location) {
		return whoLocked(location) != null;
	}
	
	
	private static class LockData {
		public Vector vector;
		public UUID world;
		public UUID player;
	}
}
