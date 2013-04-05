package snozama.client;

import ubco.ai.connection.ServerMessage;
import ubco.ai.games.GameClient;
import ubco.ai.games.GameMessage;
import ubco.ai.games.GamePlayer; 

/**
 * A testing game client. Implement the GamePlayer interface.  
 * Your Amazons player should also implement the GamePlayer interface.
 *  
 * @author gdouggie
 *
 */
public class COSC322TestA implements GamePlayer{

	GameClient gameClient = null; 
	
	
	public COSC322TestA(String name, String passwd) {
		
		//Three arguments: user name (any), passwd (any), this (delegate)   
	    gameClient = new GameClient(name, passwd, this);
	    
	    
	    //a list of available game rooms  is stored in a public variable in the class GameClient.
	    //See the GameClient class API. 
	}
	  
	/**
	 * An example showing how to compile a game message 
	 */
	public void sendToServer(String msgType, int roomID){
		String actionMsg = "<action type='" +  GameMessage.MSG_GAME + "'>";
		
		//this is not the correct message format for the Amazons game!!
		actionMsg = actionMsg + "<X value='" + 10 + "'>" + "</X>";
		actionMsg = actionMsg + "<Y value='" + 20 + "'>" + "</Y>";
		
		 
		actionMsg = actionMsg + "</action>";

		String msg = ServerMessage.compileGameMessage(msgType, roomID, actionMsg);
	   	
	    gameClient.sendToServer(msg);	
	}
	

	/**
	 * When a message from the server arrives, this method will be invloked
	 */
	public boolean handleMessage(GameMessage arg0) throws Exception {
		System.out.println("[COSC322TestA: Server Said =]  " + arg0.toString());
		return true;
	}
 
	
	public boolean handleMessage(String arg0) throws Exception {
		 
		return true;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//COSC322TestA client = new COSC322TestA(args[0], args[1]); 
		COSC322TestA client = new COSC322TestA("snozama", "alex2cody7!graeme"); 
		  
	}
	
}
