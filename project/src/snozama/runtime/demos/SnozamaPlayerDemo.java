package snozama.runtime.demos;

import snozama.client.SnozamaPlayer;

public class SnozamaPlayerDemo
{
	public static void main(String args[])
	{
		SnozamaPlayer player = new SnozamaPlayer("snozama", "alex2cody7!graeme");
		player.joinRoom(0);
		//player.printRooms();
	}
}
