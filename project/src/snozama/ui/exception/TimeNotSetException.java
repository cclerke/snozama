package snozama.ui.exception;

public class TimeNotSetException extends AUIException
{
	public TimeNotSetException( String message )
	{
		super( message );
	}
	
	public TimeNotSetException()
	{
		super( "Time is not set for the timer." );
	}
}
