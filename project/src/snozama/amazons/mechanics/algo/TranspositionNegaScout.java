package snozama.amazons.mechanics.algo;

import java.util.Arrays;

import snozama.amazons.global.GlobalFunctions;
import snozama.amazons.mechanics.Board;
import snozama.amazons.mechanics.MoveManager;
import snozama.amazons.mechanics.SnozamaHeuristic;
import snozama.amazons.mechanics.transtable.ZobristTTable;

/**
 * Class containing NegaScout search as described in Qian Liang's paper.
 * 
 * @author Cody Clerke
 *
 */

public class TranspositionNegaScout {
	
	public static int POS_INFINITY = Integer.MAX_VALUE-2;
	public static int NEG_INFINITY = Integer.MIN_VALUE+2;
	
	public static int firstN = 500; //for possible use with N-best selection search
	
	// Statistical fields.
	public int nodes = 0;
	public int depthCompleted;
	
	int[] bestMoves = new int[20]; //FIXME hard-coded as 20 for testing
	int[] scores = new int[2176];
	
	ZobristTTable table;
	int zkey;
	
	long endTime;
	
	int currentRoot;
	
	boolean gotoEnd;
	
	public TranspositionNegaScout(long end, int tableSize, Board startBoard)
	{
		table = new ZobristTTable(tableSize);
		zkey = table.computeBoardHash(startBoard);
		
		Arrays.fill(scores, NEG_INFINITY);
		depthCompleted = 0;
		endTime = end;
		
		gotoEnd = false;
	}
	
	/**
	 * Chooses the best move for Snozama based on search algorithm.
	 * @param board		The current board position.
	 * @param colour	The player for whom to find a move for.
	 * @param turn		The current ply of the game.
	 * @return			Returns the best move found by the search algorihm.
	 */
	public int chooseMove(Board board, int colour, int turn)
	{
		int maxDepth = 2;
		//probably don't need to keep the score?
		//NegaScoutSearch(board, 0, maxDepth, NEG_INFINITY, POS_INFINITY, colour, turn);
		//return bestMoves[0];
		
		//Iterative deepening NegaScout (plays better than regular fixed depth search)
		return IDNegaScoutSearch(board, colour, turn);
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
		int origAlpha = alpha;
		int zrecord[];
		
		// TODO: Make sure the correct key is used.
			// I think this is right.
		// Check transposition table for previous board position.
		/// Transposition table code ///////////////////////////////////////////
		if ((zrecord = table.get(zkey))[ZobristTTable.DEPTH] != -1)
		{
			if(zrecord[ZobristTTable.DEPTH] >= maxDepth - depth)	// TODO: Do we need to check that the hashed board is this one somehow?
			{
				if (zrecord[ZobristTTable.UPPER] <= alpha || zrecord[ZobristTTable.UPPER] == zrecord[ZobristTTable.LOWER])
				{
					return zrecord[ZobristTTable.UPPER];
				}
				if (zrecord[ZobristTTable.LOWER] >= beta)
				{
					return zrecord[ZobristTTable.LOWER];
				}
				if (zrecord[ZobristTTable.LOWER] > alpha)
				{
					origAlpha = alpha = zrecord[ZobristTTable.LOWER];
				}
				if (zrecord[ZobristTTable.UPPER] < beta)
				{
					beta = zrecord[ZobristTTable.UPPER];
				}
			}
		}
		////////////////////////////////////////////////////////////////////////
		
		int next = 0;
		
		if (depth == maxDepth || board.isTerminal())
		{
			int value = SnozamaHeuristic.evaluateBoard(board, colour, turn);
			
			if (value > scores[currentRoot])
			{
				scores[currentRoot] = value;
			}
			return value;
		}
		
		/// Transposition table code ///////////////////////////////////////////
		int score = Integer.MIN_VALUE;
		int aindex = MoveManager.getAmazonIndexFromUnmanagedMove(zrecord[ZobristTTable.MOVE], board);
		int row_s = Board.decodeAmazonRow(board.amazons[colour][aindex]);
		int col_s = Board.decodeAmazonColumn(board.amazons[colour][aindex]);
		
		MoveManager.applyUnmanagedMove(board, zrecord[ZobristTTable.MOVE]);
		
		int current = -NegaScoutSearch(board, depth+1, maxDepth, -beta, -alpha, GlobalFunctions.flip(colour), turn+1);
		if (current > score)
		{
			score = current;
		}
		if (score > alpha)
		{
			alpha = score;
			bestMoves[depth] = zrecord[ZobristTTable.MOVE];
		}
		
		if (alpha >= beta)
		{
			gotoEnd = true;
		}
		else
		{
			scores[currentRoot] = score;
		}
		
		MoveManager.undoUnmanagedMove(board, zrecord[ZobristTTable.MOVE], row_s, col_s);
		////////////////////////////////////////////////////////////////////////
		
		int b = beta;
		MoveManager successors = board.getSuccessors(colour); //generate successors
		
		// Move ordering for iterative deepening.
		if (next == 0 && depth == 0 && maxDepth > 1)
		{
			successors.sort(scores);
		}

		while (!gotoEnd && successors.hasIterations() && System.currentTimeMillis() < endTime) //for each move or until turn time runs out
		{
			next = successors.nextIterableIndex();
			
			if (depth == 0)
			{
				currentRoot = next;
				scores[currentRoot] = alpha;
			}
			
			row_s = Board.decodeAmazonRow(board.amazons[colour][successors.getAmazonIndex(next)]);
			col_s = Board.decodeAmazonColumn(board.amazons[colour][successors.getAmazonIndex(next)]);
			successors.applyMove(board, next); //execute current move
			nodes++;
			
			current = -NegaScoutSearch(board, depth+1, maxDepth, -b, -alpha, GlobalFunctions.flip(colour), turn+1);
			
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
				gotoEnd = true; //cut off
			}
			
			// Update scores array.
			scores[currentRoot] = score;
			
			b = alpha + 1;
		}
		
		/// Transposition table code ///////////////////////////////////////////
		if (gotoEnd)
		{
			zrecord[ZobristTTable.UPPER] = POS_INFINITY;
			zrecord[ZobristTTable.LOWER] = NEG_INFINITY;
			if (score < origAlpha)
			{
				zrecord[ZobristTTable.UPPER] = score;
			}
			else if (score > origAlpha && score < beta)
			{
				zrecord[ZobristTTable.UPPER] = zrecord[ZobristTTable.LOWER] = score;
			}
			else if (score >= beta)
			{
				zrecord[ZobristTTable.LOWER] = score;
			}
			table.put(zkey, zrecord);
		}
		////////////////////////////////////////////////////////////////////////
		
		return score;
	}
	
	/**
	 * Iterative deepening NegaScout search.
	 * @param board		The current board position.
	 * @param colour	The active player's colour.
	 * @param turn		The current ply of the game.
	 * @return		Returns the best move found for the current turn from the deepest fully searched depth.
	 */
	public int IDNegaScoutSearch(Board board, int colour, int turn)
	{
		int depth = 1;
		int[] bestScore = new int[20];	// Really an array of best moves at a given depth.  TODO: Rename.
		while (depth <= 20 && System.currentTimeMillis() < endTime)
		{
			NegaScoutSearch(board, 0, depth, NEG_INFINITY, POS_INFINITY, colour, turn);
			bestScore[depth-1] = bestMoves[0]; //store best move for each depth
			depthCompleted = depth;
			depth++;
		}
		boolean found = false;
		for (int i = bestScore.length-1; i >= 0; i--)
		{
			if (found)
				return bestScore[i]; //return best move for deepest complete search
			else if (bestScore[i] != 0) //first non-zero result will be best move for deepest partially complete search
				found = true;
		}
		return -1; //no move found, ERROR!
	}
}
