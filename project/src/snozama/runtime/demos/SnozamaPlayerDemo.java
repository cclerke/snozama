package snozama.runtime.demos;

import snozama.client.SnozamaPlayer;
import snozama.ui.api.AUI;
import snozama.ui.eventListeners.UIReadyListener;

public class SnozamaPlayerDemo
{
	public static void main(String args[])
	{
		SnozamaPlayer player = new SnozamaPlayer("snozama", "alex2cody7!graeme");
		player.joinRoom(0);
		
		AUI.getUI();
		AUI.ready(new UIReadyListener(){
			@Override
			public void ready()
			{
				//Does anything need to go in here?
			}
		});
	}
}
