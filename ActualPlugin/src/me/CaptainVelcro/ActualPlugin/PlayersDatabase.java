package me.CaptainVelcro.ActualPlugin;

import java.util.ArrayList;

public class PlayersDatabase {
	ArrayList<PlayersData> players = new ArrayList<PlayersData>();
	public ArrayList<PlayersData> getPlayerData(){
		return players;
	}
	public void addPlayer(PlayersData player) {
		players.add(player);
	}
}
