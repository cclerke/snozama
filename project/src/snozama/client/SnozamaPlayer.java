package snozama.client;

import java.util.ArrayList;
import java.util.Iterator;

import ubco.ai.GameRoom;
import ubco.ai.games.GameClient;
import ubco.ai.games.GameMessage;
import ubco.ai.games.GamePlayer;

/**
 * The player class for the snozama AI system.
 * @author Graeme Douglas
 * 
 * Note that this code is based off of Yong Gao's COSC322TestA.java
 */
public class SnozamaPlayer implements GamePlayer{
	private GameClient gameClient;
	
	/**
	 * Constructor where player name and password may be arbitrarily set.
	 * @param name		User name to be used on the game server.
	 * @param passwd	Password to be used on the game server.
	 */
	public SnozamaPlayer(String name, String passwd) {
		if (null == name) {
			name = "snozama";
		}
		if (null == passwd) {
			passwd = "alex2cody7!graeme";
		}
	    gameClient = new GameClient(name, passwd, this);
	}
	
	/**
	 * Constructor where player name and password may be arbitrarily set.
	 */
	public SnozamaPlayer() {  
	    gameClient = new GameClient("snozama", "alex2cody7!graeme", this);
	}
	
	/**
	 * Handle a game message in String form.
	 * @param arg0		String object containing message to handle.
	 * @return @value true if message handled successfully, @value false otherwise.
	 */
	@Override
	public boolean handleMessage(String arg0) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Handle a game message from GameMessage object.
	 * @param arg0		GameMessage object containing message to handle.
	 * @return @value true if message handled successfully, @value false otherwise.
	 */
	@Override
	public boolean handleMessage(GameMessage arg0) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * Print room names/IDs.
	 */
	public void printRooms() {
		ArrayList<GameRoom> list = gameClient.getRoomLists();
		Iterator<GameRoom> itr = list.iterator();
		while (itr.hasNext()) {
			GameRoom next = itr.next();
			System.out.println("Game Room " + next.roomID + ": " + next.roomName);
			System.out.println("\t-Number of users: " + next.userCount);
		}
	}
	
}
