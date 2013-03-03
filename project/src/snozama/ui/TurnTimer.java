package snozama.ui;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

import snozama.ui.eventListeners.CompleteListener;
import snozama.ui.exception.TimeNotSetException;

public class TurnTimer
{
	private Timer timer;
	private Integer seconds;
	
	public TurnTimer( int seconds )
	{
		timer = new Timer();
		this.seconds = seconds;
	}
	
	public void interval( final JLabel timerDisplay, final CompleteListener complete )
	throws TimeNotSetException
	{
		if( seconds == null )
		{
			throw new TimeNotSetException();
		}
		else if( seconds >= 0 )
		{
			timerDisplay.setText( "" + seconds );
			if( seconds < 10 )
			{
				timerDisplay.setForeground( new Color( 255, 0, 0 ) );
			}
			else
			{
				timerDisplay.setForeground( new Color( 255, 255, 255 ) );
			}
			seconds --;
			
			timer.schedule( new TimerTask(){

				@Override
				public void run() {
					try
					{
						interval( timerDisplay, complete);
					}
					catch( TimeNotSetException tmse )
					{
						System.out.println( tmse.getMessage() );
					}
					
				}		
			}, 1000 );
		}
		else
		{
			// callback
			complete.complete();
		}
	}
}
