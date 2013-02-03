package snozama.client;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import net.n3.nanoxml.IXMLElement;

import snozama.amazons.mechanics.Board;
import snozama.amazons.settings.Settings;
import ubco.ai.GameRoom;
import ubco.ai.connection.ServerMessage;
import ubco.ai.games.GameClient;
import ubco.ai.games.GameMessage;
import ubco.ai.games.GamePlayer;

/**
 * The player class for the snozama AI system.
 * @author Graeme Douglas
 * @author Cody Clerke
 * 
 * Note that this code is based off of Yong Gao's COSC322TestA.java
 */
public class SnozamaPlayer implements GamePlayer
{
	private GameClient gameClient;
	private GameRoom room;
	
	private Board board;
	
	/**
	 * Constructor where player name and password may be arbitrarily set.
	 * @param name		User name to be used on the game server.
	 * @param passwd	Password to be used on the game server.
	 */
	public SnozamaPlayer(String name, String passwd)
	{
		if (name == null)
		{
			name = "snozama";
		}
		if (passwd == null)
		{
			passwd = "alex2cody7!graeme";
		}
	    gameClient = new GameClient(name, passwd, this);
	}
	
	/**
	 * Constructor where player name and password may be arbitrarily set.
	 */
	public SnozamaPlayer()
	{  
	    gameClient = new GameClient("snozama", "alex2cody7!graeme", this);
	}
	
	/**
	 * Handle a game message in String form.
	 * @param msg		String object containing message to handle.
	 * @return @value true if message handled successfully, @value false otherwise.
	 */
	@Override
	public boolean handleMessage(String msg) throws Exception
	{
		System.out.println("Time out: " + msg);
		return false;
	}

	/**
	 * Handle a game message from GameMessage object.
	 * @param msg		GameMessage object containing message to handle.
	 * @return @value true if message handled successfully, @value false otherwise.
	 */
	@Override
	public boolean handleMessage(GameMessage msg) throws Exception
	{
		IXMLElement xml = ServerMessage.parseMessage(msg.msg);
		String type = xml.getAttribute("type", "ERROR!");
		System.out.println(msg);
		
		if (type.equals(GameMessage.ACTION_ROOM_JOINED))
		{
			onJoinRoom(xml);
		}
		else if (type.equals(GameMessage.ACTION_GAME_START))
		{
			onGameStart(xml);
		}
		else if (type.equals(GameMessage.ACTION_MOVE))
		{
			handleOpponentMove(xml);
		}
		
		return true;
	}
	
	//onJoinRoom function written by Yong
	//what does this do? these variable aren't used anywhere
	private void onJoinRoom(IXMLElement xml)
	{
		IXMLElement users = xml.getFirstChildNamed("usrlist");
		int userCount = users.getAttribute("ucount", -1);
		
		Enumeration<?> children = users.enumerateChildren();
		while (children.hasMoreElements())
		{
			IXMLElement user = (IXMLElement)children.nextElement();
			int id = user.getAttribute("id", -1);
			String name = user.getAttribute("name", "snozama");
		}
	}
	
	/**
	 * Starts Amazons game by assigning colour and initializing board
	 * @param xml	XML message received from the server
	 */
	private void onGameStart(IXMLElement xml)
	{
		IXMLElement users = xml.getFirstChildNamed("usrlist");
		int userCount = users.getAttribute("ucount", -1);
		
		Enumeration<?> children = users.enumerateChildren();
		while (children.hasMoreElements())
		{
			IXMLElement user = (IXMLElement)children.nextElement();
			int id = user.getAttribute("id", -1);
			String name = user.getAttribute("name", "snozama");
			
			// Loop continues until finds team name "snozama"
			if (!name.equalsIgnoreCase("snozama"))
				continue;
			
			// TODO: What if we are spectators? should default be different?
			String role = user.getAttribute("role", "W"); //default to first player
			if (role.equalsIgnoreCase("W"))
				Settings.teamColour = Board.WHITE;
			else if (role.equalsIgnoreCase("B"))
				Settings.teamColour = Board.BLACK;
			
			board = new Board();
		}
		
		System.out.println("The game has started!");
		if (Settings.teamColour == Board.WHITE)
		{
			System.out.println("Snozama moves first");
			//TODO Notify us that it's our turn
		}
		else
			System.out.println("The opponent moves first");
	}
	
	/**
	 * Receives and makes opponent's move.
	 * Notifies us that our turn has begun.
	 * @param xml	XML message received from the server
	 */
	private void handleOpponentMove(IXMLElement xml)
	{
		/*
		 * Message for amazon move in format:
		 * 	<queen move='a7-b7'></queen>
		 */
		IXMLElement amazon = xml.getFirstChildNamed("queen");
		String move = amazon.getAttribute("move", "default");
		char x_s = move.charAt(0);
		int row_s = x_s-97;
		int col_s = move.charAt(1);
		char x_f= move.charAt(3);
		int row_f = x_f-97;
		int col_f= move.charAt(4);
		
		/*
		 * Message for arrow shot in format:
		 * 	<arrow move='c6'></arrow>
		 */
		IXMLElement arrow = xml.getFirstChildNamed("arrow");
		String shot = arrow.getAttribute("move", "default");
		char a = shot.charAt(0);
		int arow = a-97;
		int acol = shot.charAt(1);
		
		System.out.println("Opponent move: "+ x_s + col_s + "-" + x_f + col_f + 
				" (" + a + acol + ")");
		
		// Make opponent's move
		boolean validMove = board.moveAmazon(row_s, col_s, row_f, col_f, Math.abs(Settings.teamColour-1));
		boolean validArrow = board.placeArrow(row_f, col_f, arow, acol);
		
		if (validMove && validArrow)
			; //TODO Inform us that it is now our turn. Start 30 sec timer.
		else
			; //TODO Error if move fails (they're big fat cheaters!)
	}
	
	/**
	 * Sends Snozama's move to the server.
	 * @param row_s		The starting row of the moving amazon.
	 * @param col_s		The starting column of the moving amazon.
	 * @param row_f		The row where the amazon finishes her move.
	 * @param col_f		The column where the amazon finishes her move.
	 * @param arow		The row of the square the arrow is being placed.
	 * @param acol		The column of the square the arrow is being placed.
	 */
	public void sendToServer(int row_s, int col_s, int row_f, int col_f, int arow, int acol)
	{
		// Message part for action tag
		String message = "<action type='" + GameMessage.ACTION_MOVE + "'>";
		
		// Message part for amazon's start square
		char x_s = (char)(row_s + 97);
		message += "<queen move='" + x_s + String.valueOf(col_s) + "-";
		
		// Message part for amazon's finishing square
		char x_f = (char)(row_f + 97);
		message += x_f + String.valueOf(col_f) + "'></queen>";
		
		// Message part for arrow
		char a = (char)(arow + 97);
		message += "<arrow move='" + a + String.valueOf(acol) + "'></arrow>";
		
		// Message part for closing action tag
		message += "</action>";
		
		// Print message and send to server
		System.out.println("Snozama move: " + message);
		String toSend = ServerMessage.compileGameMessage(GameMessage.MSG_GAME, room.roomID, message);
		gameClient.sendToServer(toSend, true);
	}
	
	/**
	 * Print room names/IDs.
	 */
	public void printRooms()
	{
		ArrayList<GameRoom> list = gameClient.getRoomLists();
		Iterator<GameRoom> itr = list.iterator();
		while (itr.hasNext())
		{
			GameRoom next = itr.next();
			System.out.println("Game Room " + next.roomID + ": " + next.roomName);
			System.out.println("\t-Number of users: " + next.userCount);
		}
	}
	
	/**
	 * Request to join a room
	 * @param roomIndex	The index of the room the client will join (0-6)
	 */
	public void joinRoom(int roomId)
	{
		room = gameClient.roomList.get(roomId);
		gameClient.joinGameRoom(room.roomName);
		System.out.println("Joining "+room.roomName+"...");
	}
}
