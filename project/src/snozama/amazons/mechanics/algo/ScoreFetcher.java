package snozama.amazons.mechanics.algo;

/**
 * Class for retrieving highest actual score found during a search,
 * rather than the negascout value.
 * 
 * @author Graeme Douglas
 *
 */
public class ScoreFetcher {
	/**
	 * The score that is / will be retrieved.
	 */
	int score;
	
	/**
	 * If this is false, don't set the score.
	 */
	boolean setable;
	
	/**
	 * Default constructor.
	 */
	public ScoreFetcher()
	{
		score=Integer.MAX_VALUE;
		setable=true;
	}
	
	/**
	 * Reset the object to be used again.
	 */
	public void reset()
	{
		score=Integer.MAX_VALUE;
		setable=true;
	}
}
