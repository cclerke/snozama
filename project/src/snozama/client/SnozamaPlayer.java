package snozama.client;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import net.n3.nanoxml.IXMLElement;

import snozama.amazons.global.GlobalFunctions;
import snozama.amazons.mechanics.Board;
import snozama.amazons.mechanics.MoveManager;
import snozama.amazons.mechanics.SnozamaHeuristic;
import snozama.amazons.mechanics.algo.DummySearch;
import snozama.amazons.mechanics.algo.NegaScout;
import snozama.amazons.mechanics.algo.TranspositionNegaScout;
import snozama.amazons.settings.Settings;
import snozama.ui.api.AUI;
import snozama.ui.exception.AUIException;
import ubco.ai.GameRoom;
import ubco.ai.connection.ServerMessage;
import ubco.ai.games.GameClient;
import ubco.ai.games.GameMessage;
import ubco.ai.games.GamePlayer;

/**
 * The player class for the snozama AI system.
 * 
 * @author Cody Clerke
 * @author Graeme Douglas
 * 
 * Note that this code is based off of Yong Gao's COSC322TestA.java
 */
public class SnozamaPlayer implements GamePlayer
{
	public static final int OK = 0;
	public static final int KAL = 1;
	public static final int YELLOW = 2;
	public static final int BEAR = 3;
	public static final int BEAVER = 4;
	public static final int JACKPINE = 5;
	public static final int WOOD = 6;
	
	public static final int[] ROOMS = { OK, KAL, YELLOW, BEAR, BEAVER, JACKPINE, WOOD };
	public static final String[] ROOM_NAMES = { "OK", "KAL","YELLOW","BEAR","BEAVER","JACKPINE","WOOD"};
	
	private GameClient gameClient;
	private GameRoom room;
	
	private Board board;
	private int turn = 0;
	
	private static String teamName = "SnozamaCody";
	private String password = "alexcodygraeme";
	
	private String opponent;
	
	private String role = "";
	
	/**
	 * Constructor where player name and password may be arbitrarily set.
	 * 
	 * @param name		User name to be used on the game server.
	 * @param passwd	Password to be used on the game server.
	 */
	public SnozamaPlayer(String name, String passwd)
	{	
		if (name == null)
		{
			teamName = name;
		}
		if (passwd == null)
		{
			password = passwd;
		}
	    gameClient = new GameClient(teamName, password, this);
	}
	
	/**
	 * Constructor where player name and password may not be arbitrarily set.
	 */
	public SnozamaPlayer()
	{  
	    gameClient = new GameClient(teamName, password, this);
	}
	
	public static void setTeamName(String name)
	{
		teamName = name;
	}
	
	/**
	 * Handle a game message in String form.
	 * 
	 * @param msg		String object containing message to handle.
	 * @return @value	{@code true} if message handled successfully,
	 * 					{@code false} otherwise.
	 */
	@Override
	public boolean handleMessage(String msg) throws Exception
	{
		System.out.println("Time out: " + msg);
		AUI.post("The opponent doesn't know what move to make!");
		AUI.post("Snozama wins by default");
		AUI.endGame();
		return false;
	}

	/**
	 * Handle a game message from GameMessage object.
	 * 
	 * @param msg		GameMessage object containing message to handle.
	 * @return 			{@code true} if message handled successfully,
	 * 					{@code false} otherwise.
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
		else if (type.equals(GameMessage.ACTION_MOVE) && !role.equalsIgnoreCase("S"))
		{
			handleOpponentMove(xml);
		}
		else if (type.equals(GameMessage.ACTION_MOVE)) //for spectating
		{
			handleMove(xml);
		}
		else if (type.equals(GameMessage.MSG_CHAT))
		{
			handleChat(xml);
		}
		
		return true;
	}
	
	/**
	 * Necessary actions to perform on joining a game room.
	 * 
	 * @param xml		XML message received from server.
	 */
	private void onJoinRoom(IXMLElement xml)
	{
		IXMLElement users = xml.getFirstChildNamed("usrlist");
		int userCount = users.getAttribute("ucount", -1);
		
		Enumeration<?> children = users.enumerateChildren();
		while (children.hasMoreElements())
		{
			//IXMLElement user = (IXMLElement)children.nextElement();
			//int id = user.getAttribute("id", -1);
			//String name = user.getAttribute("name", teamName);
		}
		
		if (userCount > 2)
		{
			board = new Board();
			this.role = "S";
			AUI.getUI();
		}
	}
	
	/**
	 * Starts Amazons game by assigning colour and initializing board
	 * @param xml	XML message received from the server
	 */
	private void onGameStart(IXMLElement xml)
	{
		IXMLElement users = xml.getFirstChildNamed("usrlist");
		//int userCount = users.getAttribute("ucount", -1);
		
		Enumeration<?> children = users.enumerateChildren();
		while (children.hasMoreElements())
		{
			IXMLElement user = (IXMLElement)children.nextElement();
			//int id = user.getAttribute("id", -1);
			String name = user.getAttribute("name", teamName);
			
			// Loop continues until finds team name "Snozama"
			if (!name.equalsIgnoreCase(teamName))
			{
				opponent = name;
				continue;
			}
			
			String role = user.getAttribute("role", "W"); //default to first player
			if (role.equalsIgnoreCase("W"))
				Settings.teamColour = Board.WHITE;
			else if (role.equalsIgnoreCase("B"))
				Settings.teamColour = Board.BLACK;
			else if (role.equalsIgnoreCase("S"))
			{
				// Spectate
				this.role = "S";
			}
			
			board = new Board();
			AUI.setColors();
			//AUI.getUI();
		}
		
		System.out.println("The game has started!");
		turn = 1;
		if (Settings.teamColour == Board.WHITE)
		{
			System.out.println("Snozama moves first");
			//Notify us that it's our turn and make first move
			makeMove();
		}
		else
		{
			System.out.println("The opponent moves first");
			AUI.startTurn(GlobalFunctions.flip(Settings.teamColour), Settings.turnTime);
		}
		
		// Set the default filename
	}
	
	/**
	 * Receives and makes opponent's move.
	 * 
	 * Notifies us that our turn has begun.
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
		int row_s = decodeMove(move.charAt(0));
		int col_s = Integer.parseInt(""+move.charAt(1));
		int row_f = decodeMove(move.charAt(3));
		int col_f= Integer.parseInt(""+move.charAt(4));
		
		/*
		 * Message for arrow shot in format:
		 * 	<arrow move='c6'></arrow>
		 */
		IXMLElement arrow = xml.getFirstChildNamed("arrow");
		String shot = arrow.getAttribute("move", "default");
		int arow = decodeMove(shot.charAt(0));
		int acol = Integer.parseInt(""+shot.charAt(1));
		
		System.out.println("Opponent move: "+ move.charAt(0) + col_s + "-" + move.charAt(3) + col_f + 
				" (" + shot.charAt(0) + acol + ")");
		
		// Make opponent's move
		boolean validMove = board.moveAmazon(row_s, col_s, row_f, col_f, Math.abs(Settings.teamColour-1));
		boolean validArrow = board.placeArrow(row_f, col_f, arow, acol);
		
		if (validMove && validArrow)
		{
			//Make opponent's move on the user interface
			try {
				AUI.moveAmazon(row_s, col_s, row_f, col_f, arow, acol);
			} catch (AUIException e) {
				e.printStackTrace();
			}
			
			turn++;
			//Inform us that it is now our turn. Start 30 sec timer.
			makeMove();
		}
		else
		{
			System.out.println("CHEATERS!!!");
			AUI.post(move+ " is an illegal move.");
			String opponent;
			if (Settings.teamColour == Board.WHITE)
				opponent = "Black";
			else opponent = "White";
			AUI.post(opponent+" are big fat cheaters!");
			AUI.post("Snozama wins by default.");
			AUI.endGame();
		}
	}
	
	/**
	 * Generalized move handling for spectating.
	 * 
	 * @param xml	XML message received from the server.
	 */
	private void handleMove(IXMLElement xml)
	{
		/*
		 * Message for amazon move in format:
		 * 	<queen move='a7-b7'></queen>
		 */
		IXMLElement amazon = xml.getFirstChildNamed("queen");
		String move = amazon.getAttribute("move", "default");
		int row_s = decodeMove(move.charAt(0));
		int col_s = Integer.parseInt(""+move.charAt(1));
		int row_f = decodeMove(move.charAt(3));
		int col_f= Integer.parseInt(""+move.charAt(4));

		/*
		 * Message for arrow shot in format:
		 * 	<arrow move='c6'></arrow>
		 */
		IXMLElement arrow = xml.getFirstChildNamed("arrow");
		String shot = arrow.getAttribute("move", "default");
		int arow = decodeMove(shot.charAt(0));
		int acol = Integer.parseInt(""+shot.charAt(1));

		System.out.println("Move: "+ move.charAt(0) + col_s + "-" + move.charAt(3) + col_f + 
				" (" + shot.charAt(0) + acol + ")");

		//Make opponent's move on the user interface
		try {
			AUI.moveAmazon(row_s, col_s, row_f, col_f, arow, acol);
		} catch (AUIException e) {
			e.printStackTrace();
		}
		
		sendRandomChat();
	}
	
	/**
	 * Sends Snozama's move to the server.
	 * 
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
		message += "<queen move='" + encodeMove(row_s) + String.valueOf(col_s) + "-";
		
		// Message part for amazon's finishing square
		message += encodeMove(row_f) + String.valueOf(col_f) + "'></queen>";
		
		// Message part for arrow
		message += "<arrow move='" + encodeMove(arow) + String.valueOf(acol) + "'></arrow>";
		
		// Message part for closing action tag
		message += "</action>";
		
		// Print message and send to server
		System.out.println("Snozama move: " + message);
		String toSend = ServerMessage.compileGameMessage(GameMessage.MSG_GAME, room.roomID, message);
		gameClient.sendToServer(toSend, true);
	}
	
	/**
	 * Sends chat message to server when spectating to maintain connection.
	 */
	public void sendRandomChat()
	{
		// Message part for action tag
		String message = "<action type='" + GameMessage.MSG_CHAT + "'>";

		// Message part for amazon's start square
		message += "<chat='The spectating life is the life for me.'></chat>";

		// Message part for closing action tag
		message += "</action>";

		// Print message and send to server
		System.out.println("Chat message: " + message);
		String toSend = ServerMessage.compileGameMessage(GameMessage.MSG_GAME, room.roomID, message);
		gameClient.sendToServer(toSend, true);
	}
	
	/**
	 * Handle a chat message.
	 * 
	 * @param xml	XML message received from the server.
	 */
	public void handleChat(IXMLElement xml)
	{
		IXMLElement chat = xml.getFirstChildNamed("chat");
		String message = chat.getContent();
		AUI.post(message);
	}
	
	/**
	 * Converts opponent move's row value to the indexing used by Snozama board.
	 * 
	 * @param row	Row index as a char from the XML message.
	 * @return		Row index as an int.
	 */
	public int decodeMove(char row)
	{
		return Math.abs(row - 106); //j-a -> 0-9
	}
	
	/**
	 * Encodes Snozama move's row value to the indexing required for server messages.
	 * 
	 * @param row	Row index as an int from Snozama's move selection.
	 * @return		Row index a char to be sent to server.
	 */
	public char encodeMove(int row)
	{
		return (char)(106 - row); //0-9 -> j-a
	}
	
	/**
	 * Completes the process of making Snozama's move. This process includes
	 * searching the game tree for a move, decoding the move into its component
	 * parts, making the move on the program's internal board, moving the
	 * piece on the program's user interface and sending the move to the server.
	 * 
	 * @return	{@code true}
	 */
	public boolean makeMove()
	{
		AUI.startTurn(Settings.teamColour, Settings.turnTime);
		long endTime = System.currentTimeMillis()+Settings.decisionTime; //starts turn timer
		NegaScout search = new NegaScout(endTime);
		//TranspositionNegaScout search = new TranspositionNegaScout(endTime, 2000000, board);
		//DummySearch search = new DummySearch(endTime);
		int encodedMove = search.chooseMove(board, Settings.teamColour, turn);
		
		//Handle end of game situations
		if (encodedMove == -1)
		{
			//No more moves available
			int score = SnozamaHeuristic.evaluateBoard(board, Settings.teamColour, turn);
			if (score > 0)
			{
				MoveManager successors = board.getSuccessors(Settings.teamColour);
				encodedMove = findLastMove(successors); //make last move
				System.out.println("Snozama wins by " + (score-1) +" points!");
				AUI.post("Snozama wins by " + (score-1) + " points!");
				AUI.endGame();
			}
			else // Unreachable code :P
			{
				System.out.println("The game is over.");
				AUI.post("The game is over.");
				AUI.endGame();
				return false;
			}
		}
		
		//Decode move using MoveManager
		int[] decodedMove = MoveManager.decodeMove(encodedMove);
		byte amazon_start = board.amazons[decodedMove[0]][decodedMove[1]];
		int row_s = Board.decodeAmazonRow(amazon_start);
		int col_s = Board.decodeAmazonColumn(amazon_start);
		int row_f = decodedMove[2];
		int col_f = decodedMove[3];
		int arow = decodedMove[4];
		int acol = decodedMove[5];
		
		//Make move on our internal board
		board.move(row_s, col_s, row_f, col_f, arow, acol, Settings.teamColour);
		turn++;
		
		//Make move on UI
		try {
			AUI.moveAmazon(row_s, col_s, row_f, col_f, arow, acol);
		} catch (AUIException e) {
			e.printStackTrace();
		}
		
		//Send move to the server
		sendToServer(row_s, col_s, row_f, col_f, arow, acol);
		
		//Start opponent's turn timer
		AUI.startTurn(GlobalFunctions.flip(Settings.teamColour), Settings.turnTime);
		
		return true;
	}
	
	/**
	 * Finds Snozama's last move of the game after it is known Snozama has won.
	 * 
	 * This is required because search algorithms won't return a move if the
	 * board is terminal.
	 * 
	 * @param successors	The available moves to Snozama on the last turn.
	 * @return				The best move of the available moves as determined
	 * 						by the evaluation function.
	 */
	public int findLastMove(MoveManager successors)
	{
		int[] scores = new int[successors.size()];
		while (successors.hasIterations())
		{
			int index = successors.nextIterableIndex();
			int row_s = Board.decodeAmazonRow(board.amazons[Settings.teamColour][successors.getAmazonIndex(index)]);
			int col_s = Board.decodeAmazonColumn(board.amazons[Settings.teamColour][successors.getAmazonIndex(index)]);
			successors.applyMove(board, index);
			scores[index] = SnozamaHeuristic.evaluateBoard(board, Settings.teamColour, turn);
			successors.undoMove(board, index, row_s, col_s);
		}
		successors.sort(scores);
		successors.clearIteratorState();
		return successors.getMove(0);
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
	 * Request to join a room.
	 * 
	 * @param roomIndex	The index of the room the client will join (0-6).
	 */
	public void joinRoom(int roomId)
	{
		room = gameClient.roomList.get(roomId);
		gameClient.joinGameRoom(room.roomName);
		System.out.println("Joining "+room.roomName+"...");
	}
}
