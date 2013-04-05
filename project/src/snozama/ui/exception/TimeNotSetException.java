package snozama.ui.exception;

/**
 * An error signaling that time has not been set.
 * 
 * @author Alex Yakovlev
 *
 */
public class TimeNotSetException extends AUIException
{
	/**
	 * Constructor with string error message.
	 * 
	 * @param message		The error message.
	 */
	public TimeNotSetException(String message)
	{
		super(message);
	}
	
	/**
	 * The default constructor.
	 */
	public TimeNotSetException()
	{
		super("Time is not set for the timer.");
	}
}
