package snozama.ui.exception;

/**
 * UI exception implementation.
 * 
 * @author Alex Yakovlev
 * 
 */
public class AUIException extends Exception
{
	/**
	 * Default serialized UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construct with error message.
	 * 
	 * @param message
	 *            The message to deliver as part of the exception.
	 */
	public AUIException(String message)
	{
		super(message);
	}

	/**
	 * Default constructor.
	 */
	public AUIException()
	{
		super();
	}
}
