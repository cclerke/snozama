package snozama.ui.demos;

import java.util.Scanner;

import snozama.ui.api.AUI;
import snozama.ui.eventListeners.UIReadyListener;
import snozama.ui.exception.AUIException;


public class UIDemo {

	/**
	 * @param args
	 * 
	 * @author Alex Yakovlev
	 */
	public static void main(String[] args) {
		
		AUI.getUI();
		
		AUI.ready( new UIReadyListener(){
			@Override
			public void ready()
			{
				try
				{
					AUI.moveAmazon(6, 0, 4, 2, 1, 2);
					AUI.moveAmazon(0, 3, 8, 3, 0, 3);
					AUI.moveAmazon(4, 2, 4, 9, 4, 8);
					
				}
				catch( AUIException e ) {}
			}
			
		});
			
		
	}

}
