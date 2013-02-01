package snozama.amazons.mechanics.algo;

import java.util.Iterator;

import snozama.amazons.mechanics.Board;
import snozama.amazons.mechanics.SnozamaHeuristic;
import snozama.amazons.settings.Settings;
import snozama.amazons.global.*;
import ubco.ai.games.GameTimer;

// TODO: Change successor stuff to moves, not boards.  SUPER IMPORTANT!!!

/**
 * Class containing plain NegaScout Algorithm.
 * 
 * @author Graeme Douglas
 *
 */
public class NegaScoutSearch implements MoveChoiceAlgorithm
{
	/**
	 * Choose next move using NegaScout algorithm.
	 * @param board		The board from which the decision is to be made.
	 * @param colour	The colour of the player whose turn it is.
	 * @param turn		The number of moves made so far in the game.
	 * @param timer		TODO	THIS DOESN'T DO WHAT I EXPECT.  WE NEED TO SORT THIS OUT.
	 * @return
	 */
	public static Board chooseMove(Board board, int colour, int turn, GameTimer timer)
	{
		Board best = null;
		Board next = null;
		int bestScore = 0;
		int currentScore = 0;
		Iterator<Board> successors = board.getSuccessors(colour).iterator();
		
		while (successors.hasNext()) // TODO: Still have time left, other constraints;
		{
			next = successors.next();
			
			currentScore = recursiveNegaScout(board, colour, turn, Integer.MIN_VALUE, Integer.MAX_VALUE, 1, 3);
			
			if (currentScore > bestScore)
			{
				best = next;
			}
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
	 * @return
	 */
	public static int recursiveNegaScout(Board board, int colour, int turn, int alpha, int beta, int depth, int cutoff)
	{
		Board next = null;
		int processed = 0;
		
		if (board.isTerminal() || depth == cutoff)
		{
			int evalFunc =  SnozamaHeuristic.evaluateBoard(board, colour, turn+depth);
			
			return evalFunc;
		}
		
		int b = beta;
		
		Iterator<Board> successors = board.getSuccessors(colour).iterator();
		
		while (successors.hasNext())
		{
			next = successors.next();
			
			int score = -1*recursiveNegaScout(next, GlobalFunctions.flip(colour), turn+1, -1*b, -1*alpha, depth+1, cutoff);
			
			// If we fail high and this is not the first child node processed
			if (alpha < score && score < beta && processed != 0)
			{
				// Full research.
				score = -1*recursiveNegaScout(next, GlobalFunctions.flip(colour), turn+1, -1*beta, -1*alpha, depth+1, cutoff);
			}

			alpha = GlobalFunctions.max(score, alpha);
			
			// Hit the beta cut-off, set new null window.
			if (alpha >= beta)
			{
				return alpha;
			}
			
			beta = alpha + 1;
			processed++;
		}
		
		return alpha;
	}
}