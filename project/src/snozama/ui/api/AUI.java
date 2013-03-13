package snozama.ui.api;

import javax.swing.SwingUtilities;

import snozama.ui.AmazonUI;
import snozama.ui.eventListeners.ReadyListener;
import snozama.ui.exception.AUIException;

public final class AUI
{
	private static AmazonUI ui = AmazonUI.getInstance();
	
	/**
	 * Get the main UI Window with default board configuration
	 */
	public static void getUI()
	{
		SwingUtilities.invokeLater(new Runnable() {
		    public void run()
		    {
		        ui.setVisible(true);
		        ui.ready();
		    }
		});
	}
	
	/**
	 * 
	 * @param row_s Amazon Starting Row
	 * @param col_s Amazon Starting Column
	 * @param row_f Amazon Finishing Row
	 * @param col_f Amazon Finishing Row
	 * @param row_a Arrow Row
	 * @param col_a Arrow Column
	 * @return @value true if the move was successful, @value false if the move was unsuccessful
	 * @throws AUIException
	 */
	public static boolean moveAmazon(int row_s, int col_s, int row_f, int col_f, int row_a, int col_a)
		throws AUIException
	{
		return ui.moveAmazon(row_s, col_s, row_f, col_f, row_a, col_a);
	}
	
	/**
	 * Post a message to the onscreen log.
	 * @param message - message to post
	 */
	public static void post(String message)
	{
		ui.post(message);
	}
	
	/**
	 * Start the game when both players are reading and the timer should be running.
	 * 
	 * @param whoseTurn - Board.BLACK, or BLACK.WHITE
	 * @param seconds - number of seconds allotted for this turn
	 */
	public static void startTurn( int whoseTurn, int seconds )
	{
		ui.startTurn( whoseTurn, seconds );
	}
	
	/**
	 * Final cleanup after the game is finished.
	 */
	public static void endGame()
	{
		ui.endGame();
	}
	
	/**
	 * Code to run when the UI is ready
	 * @param rl - ReadyListner that contains the code to run
	 */
	public static void ready( ReadyListener rl )
	{
		ui.addReadyListener( rl );
	}
}
