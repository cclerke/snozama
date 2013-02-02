package snozama.ui.demos;

import javax.swing.SwingUtilities;

import snozama.amazons.mechanics.Board;
import snozama.ui.AmazonUI;

public class UIDemo {

	/**
	 * @param args
	 * 
	 * @author Alex Yakovlev
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		        AmazonUI ui = new AmazonUI( new Board() );
		        ui.setVisible(true);
		    }
		});
	}

}
