package snozama.amazons.mechanics.algo;

import snozama.amazons.mechanics.Board;
import snozama.amazons.mechanics.MoveManager;

/**
 * A randomized search algorithm.
 * 
 * @author Graeme Douglas
 *
 */
public class DummySearch {
	
	/**
	 * The time limit of the search as a system milliseconds time.
	 */
	long endTime;
	
	/**
	 * The default constructor.
	 * 
	 * @param end		The end time of the search, a system milliseconds time.
	 */
	public DummySearch(long end)
	{
		endTime = end;
	}
	
	/**
	 * Choose a move to make randomly.
	 * 
	 * @param board		The current board state.
	 * @param colour	The current player's colour, either {@code BLACK} or
	 * 					{@code WHITE}.
	 * @param turn		Count of current turn (turn 1, turn 2, etc.)
	 * @return			An integer encoded move.
	 */
	public int chooseMove(Board board, int colour, int turn)
	{
		if (board.isTerminal())
		{
			return -1;
		}
		// Choose your algorithm here.
		
		//return fastRandomMove(board, colour);
		
		return timedRandomMove(board, colour);
	}
	
	/**
	 * Choose a move to make randomly, as fast as possible.
	 * 
	 * @param board		The current board state.
	 * @param colour	The current player's colour, either {@code BLACK} or
	 * 					{@code WHITE}.
	 * @return			An integer encoded move.
	 */
	private int fastRandomMove(Board board, int colour)
	{
		MoveManager successors = board.getSuccessors(colour);
		
		successors.shuffle();
		
		return successors.getMove(0);
	}
	
	/**
	 * Choose a move to make randomly, but wait until search time is up.
	 * 
	 * LOL Will.
	 * 
	 * @param board		The current board state.
	 * @param colour	The current player's colour, either {@code BLACK} or
	 * 					{@code WHITE}.
	 * @return			An integer encoded move.
	 */
	private int timedRandomMove(Board board, int colour)
	{
		// Look like we are making good decisions
		while (System.currentTimeMillis() < endTime){}
		
		return fastRandomMove(board, colour);
	}

}
