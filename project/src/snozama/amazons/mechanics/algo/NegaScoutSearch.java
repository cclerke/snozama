package snozama.amazons.mechanics.algo;

import snozama.amazons.mechanics.Board;
import snozama.amazons.mechanics.MoveManager;
import snozama.amazons.mechanics.SnozamaHeuristic;
import snozama.amazons.global.*;
import ubco.ai.games.GameTimer;

/**
 * Class containing plain NegaScout Algorithm.
 * 
 * @author Graeme Douglas
 * @author Cody Clerke
 *
 */
public class NegaScoutSearch implements MoveChoiceAlgorithm
{
	static final int debug_limit = Integer.MAX_VALUE;
	//static final int debug_limit = 5;
	/**
	 * Choose next move using NegaScout algorithm.
	 * @param board		The board from which the decision is to be made.
	 * @param colour	The colour of the player whose turn it is.
	 * @param turn		The number of moves made so far in the game.
	 * @param timer		TODO	THIS DOESN'T DO WHAT I EXPECT.  WE NEED TO SORT THIS OUT.
	 * 					//can we use long endTurn = System.currentMiilis() + turnTime*1000 (turnTime = 30)
	 * @return
	 */
	public static int chooseMove(Board board, int colour, int turn, GameTimer timer)
	{
		int best = -1;
		int next = 0;
		int bestScore = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		int currentScore = Integer.MIN_VALUE;
		MoveManager successors = board.getSuccessors(colour, turn);
		
		while (successors.hasIterations() && next < debug_limit) // TODO: Still have time left, other constraints;
		{
			next = successors.nextIterableIndex();
			
			int arr_s = Board.decodeAmazonRow(board.amazons[colour][successors.getAmazonIndex(next)]);
			int col_s = Board.decodeAmazonColumn(board.amazons[colour][successors.getAmazonIndex(next)]);
			
			successors.applyMove(board, next);
			
			currentScore = 1*recursiveNegaScout(board, colour, turn, currentScore, beta, 1, 3);
			
			if (currentScore > bestScore)
			{
				best = next;
				bestScore = currentScore;
				beta = currentScore+1;
			}
			
			successors.undoMove(board, next, arr_s, col_s);
			System.out.println(next);
			System.out.println("+"+bestScore);
		}
		
		return best;
	}
	
	/**
	 * A recursive implementation of the NegaScout search algorithm.
	 * 
	 * @param board		The board whose payoff value is to be estimated.
	 * @param colour	The colour of the player whose turn it is.
	 * @param turn		The number of moves made so far in the game.
	 * @param alpha		The lower bound of the expected value of the subtree.
	 * @param beta		The upper bound of the expected value of the subtree.x
	 * @param depth		The depth of the current node from the initial search node.
	 * @param cutoff	The depth from the initial search node at which to go no deeper.
	 * @return	Return estimated score for a given board.
	 */
	public static int recursiveNegaScout(Board board, int colour, int turn, int alpha, int beta, int depth, int cutoff)
	{
		int next = 0;
		
		if (board.isTerminal() || depth == cutoff)
		{
			int evalFunc =  SnozamaHeuristic.evaluateBoard(board, colour, turn+depth);
			
			return evalFunc;
		}
		
		int b = beta;
		
		colour = GlobalFunctions.flip(colour);
		
		MoveManager successors = board.getSuccessors(colour, turn);
		
		while (successors.hasIterations() && next < debug_limit)
		{
			next = successors.nextIterableIndex();
			
			int arr_s = Board.decodeAmazonRow(board.amazons[colour][successors.getAmazonIndex(next)]);
			int col_s = Board.decodeAmazonColumn(board.amazons[colour][successors.getAmazonIndex(next)]);
			
			successors.applyMove(board, next);
			
			int score = -1*recursiveNegaScout(board, colour, turn+1, -1*b, -1*alpha, depth+1, cutoff);
			
			// If we fail high and this is not the first child node processed
			if (alpha < score && score < beta && next != 0)
			{
				// Full re-search.
				score = -1*recursiveNegaScout(board, colour, turn+1, -1*beta, -1*alpha, depth+1, cutoff);
			}
			
			alpha = GlobalFunctions.max(score, alpha);
			
			successors.undoMove(board, next, arr_s, col_s);
			
			// Hit the beta cut-off, set new null window.
			if (alpha >= beta)
			{
				return alpha;
			}
			
			b = alpha + 1;
		}
		
		return alpha;
	}
}