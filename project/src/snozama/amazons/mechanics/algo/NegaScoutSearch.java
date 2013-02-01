package snozama.amazons.mechanics.algo;

import java.util.Iterator;

import snozama.amazons.mechanics.Board;
import snozama.amazons.mechanics.SnozamaHeuristic;
import snozama.amazons.settings.Settings;
import ubco.ai.games.GameTimer;

/**
 * Class containing plain NegaScout Algorithm.
 * 
 * @author Graeme Douglas
 *
 */
public class NegaScoutSearch implements MoveChoiceAlgorithm
{
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
	 * NegaScout Search
	 * @param board
	 * @param colour
	 * @param alpha
	 * @param beta
	 * @param depth
	 * @param cutoff
	 * @return
	 */
	public static int recursiveNegaScout(Board board, int colour, int alpha, int beta, int depth, int cutoff)
	{
		Board next = null;
		int child = 0;
		
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
			
			int score = -1*recursiveNegaScout(next, flip(colour), -1*b, -1*alpha, depth+1, cutoff);
			
			// If we fail high.
			if (alpha < score && score < beta && child != 0)
			{
				score = -1*recursiveNegaScout(next, flip(colour), -1*beta, -1*alpha, depth+1, cutoff);
			}

			alpha = max(score, alpha);
			
			// Hit the beta cut-off, set new null window.
			if (alpha >= beta)
			{
				return alpha;
			}
			
			beta = alpha + 1;
			child++;
		}
		
		return alpha;
	}
	
	// TODO: Move the following to a better location.
	public static int flip(int input)
	{
		if (input == 0)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	
	public static int max(int a, int b)
	{
		if (b > a)
		{
			return b;
		}
		else
		{
			return a;
		}
	}
}