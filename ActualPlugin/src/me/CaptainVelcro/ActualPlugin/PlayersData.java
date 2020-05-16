package me.CaptainVelcro.ActualPlugin;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class PlayersData {
	private UUID uuid;
	private ArrayList<Vector> vectors = new ArrayList<Vector>();
	public PlayersData(UUID uuid) {
		this.uuid = uuid;
	
	}


	public UUID getUUID() {
		return uuid;
	}
	public ArrayList<Vector> getVectors(){
		return vectors;
	}
	
	public void addVector(int x, int y, int z) {
		vectors.add(new Vector(x,y,z));
	}
	
	public void addVector(Location loc) {
		vectors.add(loc.toVector());
	}
}
