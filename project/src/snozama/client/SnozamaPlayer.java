package snozama.client;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import net.n3.nanoxml.IXMLElement;

import snozama.amazons.mechanics.Board;
import ubco.ai.GameRoom;
import ubco.ai.connection.ServerMessage;
import ubco.ai.games.GameClient;
import ubco.ai.games.GameMessage;
import ubco.ai.games.GamePlayer;

/**
 * The player class for the snozama AI system.
 * @author Graeme Douglas
 * 
 * Note that this code is based off of Yong Gao's COSC322TestA.java
 */
public class SnozamaPlayer implements GamePlayer
{
	private GameClient gameClient;
	private GameRoom room;
	
	private Board board;
	private boolean isWhite = false;
	
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
		// TODO Auto-generated method stub
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
			//TODO Handle MoveAction message
			handleOpponentMove(xml);
		}
		
		return false;
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
			String name = user.getAttribute("name", "snozama"); //second value is default, maybe an error message instead
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
			if (!name.equalsIgnoreCase("snozama")) //FIXME global name variable?
				continue;
			
			String role = user.getAttribute("role", "W"); //default to first player
			if (role.equalsIgnoreCase("W"))
				isWhite = true;
			else
				isWhite = false;
			
			board.initialize(isWhite);
		}
		
		System.out.println("The game has started!");
		if (isWhite)
			System.out.println("Snozama moves first");
		else
			System.out.println("The opponent moves first");
	}
	
	/**
	 * 
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
		char sX = move.charAt(0);
		int amazonStartX = sX-97;
		int amazonStartY = move.charAt(1);
		char fX= move.charAt(3);
		int amazonFinalX = fX-97;
		int amazonFinalY= move.charAt(4);
		
		/*
		 * Message for arrow shot in format:
		 * 	<arrow move='c6'></arrow>
		 */
		IXMLElement arrow = xml.getFirstChildNamed("arrow");
		String shot = arrow.getAttribute("move", "default");
		char aX = shot.charAt(0);
		int arrowX = aX-97;
		int arrowY = shot.charAt(1);
		
		System.out.println("Opponent move: "+ sX + amazonStartY + "-" + fX + amazonFinalY + 
				" (" + aX + arrowY + ")");
		
		//TODO Create a function that does this
		board.move(amazonStartX, amazonStartY, amazonFinalX, amazonFinalY, arrowX, arrowY);
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
