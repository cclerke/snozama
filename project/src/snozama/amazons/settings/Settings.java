package snozama.amazons.settings;

public class Settings {
	
	/**
	 * The team colour setting.  Can be referenced throughout code.
	 * THIS SHOULD ONLY BE SET ONCE!
	 */
	public static int teamColour = 0;
	
	/**
	 * The turn time in seconds.
	 */
	public static int turnTime = 30;
	
	/**
	 * The decision time for Snozama to make a move in milliseconds.
	 */
	public static int decisionTime = turnTime*1000 - 2000;
}
