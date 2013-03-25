package snozama.runtime.demos;

import snozama.client.SnozamaPlayer;

public class SnozamaPlayerDemo
{
	private static final int OK = 0;
	private static final int KAL = 1;
	private static final int YELLOW = 2;
	private static final int BEAR = 3;
	private static final int BEAVER = 4;
	private static final int JACKPINE = 5;
	private static final int WOOD = 6;
	
	private static int room = BEAVER;
	
	public static void main(String args[])
	{
		SnozamaPlayer player = new SnozamaPlayer();
		player.joinRoom(room);
		//player.printRooms();
	}
}
