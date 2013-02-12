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
	
	public int nodes = 0;
	long endTime;
	
	public NegaScout(long end)
	{
		endTime = end;
	}
	
	public int NegaScoutSearch(Board board, int depth, int alpha, int beta, int colour, int turn)
	{
		int next;
		if (depth == 0 || board.isTerminal())
		{
			return SnozamaHeuristic.evaluateBoard(board, colour, turn);
		}
		
		int score = Integer.MIN_VALUE;
		int b = beta;
		MoveManager successors = board.getSuccessors(colour);
		
		while (successors.hasIterations())// && System.currentTimeMillis() < endTime)
		{
			next = successors.nextIterableIndex();
			int row_s = Board.decodeAmazonRow(board.amazons[colour][successors.getAmazonIndex(next)]);
			int col_s = Board.decodeAmazonColumn(board.amazons[colour][successors.getAmazonIndex(next)]);
			successors.applyMove(board, next);
			nodes++;
			
			int current = -NegaScoutSearch(board, depth-1, -b, -alpha, GlobalFunctions.flip(colour), turn+1);
			
			if (current > score)
			{
				if (b == beta || depth <= 2)
					score = current;
				else
					score = -NegaScoutSearch(board, depth-1, -beta, -current, GlobalFunctions.flip(colour), turn+1); //re-search
			}
			
			if (score > alpha)
			{
				alpha = score; //adjust the search window
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
