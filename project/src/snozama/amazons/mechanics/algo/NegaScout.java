package snozama.amazons.mechanics.algo;

import snozama.amazons.global.GlobalFunctions;
import snozama.amazons.mechanics.Board;
import snozama.amazons.mechanics.MoveManager;
import snozama.amazons.mechanics.SnozamaHeuristic;

/**
 * Class containing NegaScout search as described in Qian Liang's paper.
 * 
 * @author Cody Clerke
 *
 */

public class NegaScout {
	
	public static int POS_INFINITY = Integer.MAX_VALUE-2;
	public static int NEG_INFINITY = Integer.MIN_VALUE+2;
	
	public static int firstN = 500; //for possible use with N-best selection search
	
	public int nodes = 0;
	int[] bestMoves = new int[10]; //FIXME hard-coded as 10 for testing
	int maxDepth = 1;
	long endTime;
	
	public NegaScout(int maxDepth, long end)
	{
		this.maxDepth = maxDepth;
		endTime = end;
	}
	
	/**
	 * Chooses the best move for Snozama based on search algorithm.
	 * @param board		The current board position.
	 * @param colour	The player for whom to find a move for.
	 * @param turn		The current ply of the game.
	 * @return			Returns the best move found by the search algortihm.
	 */
	public int chooseMove(Board board, int colour, int turn)
	{
		//maxDepth here refers to this.maxDepth
		//probably don't need to keep the score?
		NegaScoutSearch(board, 0, maxDepth, NEG_INFINITY, POS_INFINITY, colour, turn);
		return bestMoves[0];
	}
	
	/**
	 * NegaScout search algorithm.
	 * @param board			The current board position.
	 * @param depth			The starting depth of the search. Should always start at <code>depth = 0</code>.
	 * @param maxDepth		The maximum depth to be searched.
	 * @param alpha			The lower bound of the search window.
	 * @param beta			The upper bound of the search window.
	 * @param colour		The active player's colour.
	 * @param turn			The current ply of the game.
	 * @return				Returns the score of the best move.
	 */
	public int NegaScoutSearch(Board board, int depth, int maxDepth, int alpha, int beta, int colour, int turn)
	{
		int next = 0;
		if (depth == maxDepth || board.isTerminal())
		{
			return SnozamaHeuristic.evaluateBoard(board, colour, turn);
		}
		
		int score = Integer.MIN_VALUE;
		int b = beta;
		MoveManager successors = board.getSuccessors(colour); //generate successors
		
		//move ordering
		//evaluate all moves available at initial depth and sort descending
		if (depth == 0)
		{
			int[] scores = new int[successors.size()];
			while (successors.hasIterations())
			{
				int index = successors.nextIterableIndex();
				int row_s = Board.decodeAmazonRow(board.amazons[colour][successors.getAmazonIndex(index)]);
				int col_s = Board.decodeAmazonColumn(board.amazons[colour][successors.getAmazonIndex(index)]);
				successors.applyMove(board, index);
				scores[index] = SnozamaHeuristic.evaluateBoard(board, colour, turn);
				successors.undoMove(board, index, row_s, col_s);
			}
			successors.sort(scores);
			successors.clearIteratorState();
		}
		//end move ordering

		while (successors.hasIterations())// && System.currentTimeMillis() < endTime) //for each move or until turn time runs out
		{
			next = successors.nextIterableIndex();
			int row_s = Board.decodeAmazonRow(board.amazons[colour][successors.getAmazonIndex(next)]);
			int col_s = Board.decodeAmazonColumn(board.amazons[colour][successors.getAmazonIndex(next)]);
			successors.applyMove(board, next); //execute current move
			nodes++;
			
			int current = -NegaScoutSearch(board, depth+1, maxDepth, -b, -alpha, GlobalFunctions.flip(colour), turn+1);
			
			if (current > score)
			{
				if (b == beta || maxDepth - depth <= 2)
					score = current;
				else
					score = -NegaScoutSearch(board, depth+1, maxDepth, -beta, -current, GlobalFunctions.flip(colour), turn+1); //re-search
			}
			
			if (score > alpha)
			{
				alpha = score; //adjust the search window
				bestMoves[depth] = successors.getMove(next);
			}			
			
			successors.undoMove(board, next, row_s, col_s); //retract current move
			
			if (alpha >= beta)
			{
				return alpha; //cut off
			}
			
			b = alpha + 1;
		}
		return score;
	}
}
