package snozama.amazons.mechanics.algo;

import snozama.amazons.global.GlobalFunctions;
import snozama.amazons.mechanics.Board;
import snozama.amazons.mechanics.MoveChoice;
import snozama.amazons.mechanics.MoveManager;
import snozama.amazons.mechanics.SnozamaHeuristic;

/**
 * Class containing NegaScout search as described in Qian Liang's paper.
 * 
 * @author Cody Clerke
 *
 */

public class NegaScout {
	
	public static int debug_limit = Integer.MAX_VALUE;
	
	public int nodes = 0;
	public int[] bestMoves = new int[5]; //FIXME hard-coded as 5 for testing
	long endTime;
	
	public NegaScout(long end)
	{
		endTime = end;
	}
	
	public MoveChoice chooseMove(Board board, int colour, int turn, int depth)
	{
		int best = -1;
		int next = 0;
		int bestScore = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		int currentScore = Integer.MIN_VALUE;
		MoveManager successors = board.getSuccessors(colour);
		//successors.shuffle();	// TODO: Why does this cause the value of negascout search to change?
		
		while (successors.hasIterations() && next < debug_limit) // TODO: Still have time left, other constraints;
		{
			next = successors.nextIterableIndex();
			
			int arr_s = Board.decodeAmazonRow(board.amazons[colour][successors.getAmazonIndex(next)]);
			int col_s = Board.decodeAmazonColumn(board.amazons[colour][successors.getAmazonIndex(next)]);
			
			successors.applyMove(board, next);
			
			currentScore = NegaScoutSearch(board, depth, currentScore, beta, colour, turn);
			
			if (currentScore > bestScore)
			{
				best = next;
				bestScore = currentScore;
				//beta = currentScore+1;
			}
			
			successors.undoMove(board, next, arr_s, col_s);
			System.out.println(next);
			System.out.println("+"+bestScore);
		}
		
		return new MoveChoice(successors, best, board);
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
		MoveManager successors = board.getSuccessors(colour); //generate successors
		
		//move ordering
		//evaluate all moves available at initial depth and sort descending
		if (depth == 2)
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
