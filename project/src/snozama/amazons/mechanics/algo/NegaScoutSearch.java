package snozama.amazons.mechanics.algo;

import java.util.Iterator;

import snozama.amazons.mechanics.Board;
import snozama.amazons.mechanics.SnozamaHeuristic;
import snozama.amazons.settings.Settings;
import snozama.amazons.global.*;
import ubco.ai.games.GameTimer;


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
	 * @param timer		TODO	THIS DOESN"T WHAT I EXPECT.  WE NEED TO SORT THIS OUT.
	 * @return
	 */
	public static Board chooseMove(Board board, int colour, GameTimer timer)
	{
		Board best = null;
		Board next = null;
		int bestScore = 0;
		int currentScore = 0;
		Iterator<Board> successors = board.getSuccessors(colour).iterator();
		
		while (successors.hasNext()) // TODO: Still have time left, other constraints;
		{
			next = successors.next();
			
			// TODO: What are starting values for alpha and beta?
			currentScore = recursiveNegaScout(board, colour, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 7);
			
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
	 * @param alpha		
	 * @param beta
	 * @param depth		The depth of the current node from the initial search node.
	 * @param cutoff	The depth from the initial search node at which to go no deeper.
	 * @return
	 */
	public static int recursiveNegaScout(Board board, int colour, int alpha, int beta, int depth, int cutoff)
	{
		Board next = null;
		int processed = 0;
		
		// TODO: Can we not give an absolute score here if it is terminal? I mean, at terminal, you've either lost or won, no?
		if (board.isTerminal() || depth == cutoff)
		{
			int evalFunc =  SnozamaHeuristic.evaluateBoard(board, colour, depth);
			
			if (colour != Settings.teamColour)
			{
				evalFunc = -1*evalFunc;
			}
			
			return evalFunc;
		}
		
		int b = beta;
		
		Iterator<Board> successors = board.getSuccessors(colour).iterator();
		
		while (successors.hasNext())
		{
			next = successors.next();
			
			int score = -1*recursiveNegaScout(next, GlobalFunctions.flip(colour), -1*b, -1*alpha, depth+1, cutoff);
			
			// If we fail high and this is not the first child node processed
			if (alpha < score && score < beta && processed != 0)
			{
				score = -1*recursiveNegaScout(next, GlobalFunctions.flip(colour), -1*beta, -1*alpha, depth+1, cutoff);
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