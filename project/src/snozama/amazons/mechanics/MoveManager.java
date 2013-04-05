package snozama.amazons.mechanics;

import java.util.Random;

import snozama.amazons.global.GlobalFunctions;

/**
 * A high performance management library for board moves.
 * @author Graeme Douglas
 * @author Cody Clerke
 * 
 * Moves are encoded as 32-bit integers (shorts).  We divide the bits as follows:
 * 	Portion 0 - bits 0-3:	row of old amazon position.
 * 	Portion 1 - bits 4-7: 	column of old amazon position.
 * 	Portion 2 - bits 8-11:	row of new amazon position.
 *  Portion 3 - bits 12-15:	column of new amazon position.
 *  Portion 4 - bits 16-19:	row where new arrow placed.
 *  Portion 5 - bits 20-24:	column where new arrow placed.
 * 	Portion 6 - bits 25-28:	???
 * 	Portion 7 - bits 29-32:	???
 */
public class MoveManager
{
	/*** Portion Constants ****************************************************/
	/**
	 * Portion of move that is for the player's colour.
	 */
	public static final int PLAYER_COLOUR = 0;
	
	/**
	 * Portion of move that is for the index within the board's amazon array.
	 */
	public static final int AMAZON_ARRAY_INDEX = 1;
	
	/**
	 * Portion of move that is for row of the final position of amazon.
	 */
	public static final int AMAZON_ROW_FINISH = 2;
	
	/**
	 * Portion of move that is for column of final position of amazon.
	 */
	public static final int AMAZON_COLUMN_FINISH = 3;
	
	/**
	 * Portion of move that is for row of the arrow that will be placed.
	 */
	public static final int ARROW_ROW = 4;
	
	/**
	 * Portion of move that is for column of the arrow that will be placed.
	 */
	public static final int ARROW_COLUMN = 5;
	/**************************************************************************/
	
	/**
	 * Array storing move data.
	 */
	private int[]  moves;
	
	/**
	 * Position in moves where to add next move to.
	 */
	private int nextPos;
	
	/**
	 * State variable for the iterator.
	 */
	private int iteratorPosition;
	
	/**
	 * Constructor. Create a new MoveManager.
	 * 
	 * @param size		The number of elements we wish to make the array of
	 * 					moves initially.
	 */
	public MoveManager(int size)
	{
		moves = new int[size];
		nextPos = 0;
		iteratorPosition = 0;
	}
	
	/**
	 * Default constructor.
	 */
	public MoveManager()
	{
		moves = new int[2176];
		nextPos = 0;
		iteratorPosition = 0;
	}
	
	/**
	 * Get the current number of moves being managed.
	 * 
	 * @return	The current number of moves being managed.
	 */
	public int size()
	{
		return nextPos;
	}
	
	/**
	 * Get the maximum possible size of the move manager.
	 * 
	 * @return	The maximum number of moves this instance can managed.
	 */
	public int maxSize()
	{
		return moves.length;
	}
	
	/**
	 * Apply a move to a board.
	 * 
	 * @param board		The board to apply the move to.
	 * @param index		The index of the move to apply.
	 * @return			{@code true} if the move was applied successfully,
	 * 					{@code false} otherwise.
	 */
	public boolean applyMove(Board board, int index)
	{
		return board.move(getAmazonIndex(index), getFinishRow(index),
				getFinishColumn(index), getArrowRow(index),
				getArrowColumn(index), (byte)getColour(index));
	}
	
	/**
	 * Apply a move to a board.
	 * 
	 * @param board		The board to apply the move to.
	 * @param move		The move to apply.
	 * @return			{@code true} if the move was applied successfully,
	 * 					{@code false} otherwise.
	 */
	public static boolean applyUnmanagedMove(Board board, int move)
	{
		int index = staticDecodePortion(move, AMAZON_ARRAY_INDEX);
		int colour = staticDecodePortion(move, PLAYER_COLOUR);
		int row_s = Board.decodeAmazonRow(board.amazons[colour][index]);
		int col_s = Board.decodeAmazonColumn(board.amazons[colour][index]);
		return board.move(row_s, col_s,
				staticDecodePortion(move, AMAZON_ROW_FINISH),
				staticDecodePortion(move, AMAZON_COLUMN_FINISH),
				staticDecodePortion(move, ARROW_ROW),
				staticDecodePortion(move, ARROW_COLUMN), colour);
	}
	
	/**
	 * Undo a move from a board.
	 * 
	 * @param board		The board to undo the move for.
	 * @param index		The index of the move to undo.
	 * @param row_s		Row where amazon previously was.
	 * @param col_s		Column where amazon previously was.
	 * @return			{@code true} if the move was undone successfully,
	 * 					{@code false} otherwise.
	 */
	public boolean undoMove(Board board, int index, int row_s, int col_s)
	{
		board.board[getArrowRow(index)][getArrowColumn(index)] = Board.EMPTY;
		
		board.board[getFinishRow(index)][getFinishColumn(index)] = Board.EMPTY;
		
		board.board[row_s][col_s] = Board.OCCUPIED;
		
		board.amazons[getColour(index)][getAmazonIndex(index)]=
				Board.encodeAmazonPosition(row_s, col_s);
		
		return true;
	}
	
	/**
	 * Undo an unmanaged move from a board.
	 * 
	 * @param board		The board to undo the move for.
	 * @param move		The move to undo.
	 * @param row_s		Row where amazon previously was.
	 * @param col_s		Column where amazon previously was.
	 * @return			{@code true} if the move was undone successfully,
	 *					{@code false} otherwise.
	 */
	public static boolean undoUnmanagedMove(Board board, int move, int row_s, int col_s)
	{
		int index = staticDecodePortion(move, AMAZON_ARRAY_INDEX);
		int colour = staticDecodePortion(move, PLAYER_COLOUR);
		
		board.board[staticDecodePortion(move, ARROW_ROW)][staticDecodePortion(move, ARROW_COLUMN)] = Board.EMPTY;
		
		board.board[staticDecodePortion(move, AMAZON_ROW_FINISH)][staticDecodePortion(move, AMAZON_COLUMN_FINISH)] = Board.EMPTY;
		
		board.board[row_s][col_s] = Board.OCCUPIED;
		
		board.amazons[colour][index]=
				Board.encodeAmazonPosition(row_s, col_s);
		
		return true;
	}
	
	/**
	 * Decode a portion of a move.
	 * 
	 * @param index		The index of the move to be decoded.
	 * @param portion	Which set of 4 bits to be decoded.		
	 * @return			The decoded portion of the move at index.
	 */
	private int decodePortion(int index, int portion)
	{
		return ((moves[index] & (0xf << (4*portion))) >> (4*portion));
	}
	
	/**
	 * Decode a portion of an arbitrary move.
	 * 
	 * @param move		The move to decode from.
	 * @param portion	Which set of 4 bits to be decoded.		
	 * @return			The decoded portion of the move at index.
	 */
	private static int staticDecodePortion(int move, int portion)
	{
		return ((move & (0xf << (4*portion))) >> (4*portion));
	}
	
	/**
	 * Get the moving amazon's index from an unmanaged move.
	 * 
	 * @param move		The move that is to be decoded.
	 * @param board		The board that is referenced.
	 * @return			The index of the moving amazon.
	 */
	public static int getAmazonIndexFromUnmanagedMove(int move, Board board)
	{
		return staticDecodePortion(move, AMAZON_ARRAY_INDEX);
	}
	
	/**
	 * Get the moving amazon's colour from an unmanaged move.
	 * 
	 * @param move		The move that is to be decoded.
	 * @param board		The board that is referenced.
	 * @return			The colour of the player.
	 */
	public static int getPlayerColourFromUnmanagedMove(int move, Board board)
	{
		return staticDecodePortion(move, PLAYER_COLOUR);
	}
	
	/**
	 * Encode information to a portion of a move at an index.
	 * 
	 * @param index		The index of the move to encode data into.
	 * @param portion	The portion of the m0e to encode the data to.
	 * @param toEncode	The data that is to be encoded. Necessary data should
	 * 					be in the lowest 4 bits.
	 * @return			{@code true} if the data was encoded, {@code false} otherwise.
	 */
	private boolean encodePortion(int index, int portion, int toEncode)
	{
		// First clear the necessary bits.
		moves[index] &= ~(0xf << (4*portion));
		
		// Now set them.
		moves[index] |= (toEncode << (4*portion));
		return true;
	}
	
	/**
	 * Decodes an encoded move and returns each part in an array.
	 * 
	 * Part 0: Colour of the moving amazon.
	 * Part 1: Index of the moving amazon.
	 * Part 2: Finishing row of the moving amazon.
	 * Part 3: Finishing column of the moving amazon.
	 * Part 4: Row of the arrow.
	 * Part 5: Column of the arrow.
	 * @param move		The move to be decoded.
	 * @return			An array of move parts.
	 */
	public static int[] decodeMove(int move)
	{
		int[] moveParts = new int[6];
		for (int i = 0; i <= 5; i++)
		{
			moveParts[i] = ((move & (0xf << (4*i))) >> (4*i));
		}
		return moveParts;
	}
	
	/**
	 * Add an encoded move to the set of moves.
	 * 
	 * @param move		The encoded move.
	 * @return			{@code false} if the move was not added, {@code true}
	 * 					otherwise.
	 */
	public boolean add(int move)
	{
		if (nextPos >= moves.length)
			return false;
		
		moves[nextPos++] = move;
		return true;
	}
	
	/**
	 * Add a move to the set of possible moves.
	 * 
	 * @param colour	The player's/amazon's colour.
	 * @param arr_i		The index of the amazon in the board's amazon array.
	 * @param arow_f	The row the amazon ends in.
	 * @param acol_f	The column the amazon ends in.
	 * @param arrowrow	The row the arrow is placed.
	 * @param arrowcol	The row the arrow is placed.
	 * @return			{@code true} if the move was added, {@code false }
	 * 					otherwise.
	 */
	public boolean add(int colour, int arr_i, int arow_f, int acol_f, int arrowrow, int arrowcol)
	{
		if (nextPos >= moves.length)
			return false;
		
		// Encode all the data.
		encodePortion(nextPos, PLAYER_COLOUR, colour);
		encodePortion(nextPos, AMAZON_ARRAY_INDEX, arr_i);
		encodePortion(nextPos, AMAZON_ROW_FINISH, arow_f);
		encodePortion(nextPos, AMAZON_COLUMN_FINISH, acol_f);
		encodePortion(nextPos, ARROW_ROW, arrowrow);
		encodePortion(nextPos, ARROW_COLUMN, arrowcol);
		
		nextPos++;
		return true;
	}
	
	/**
	 * Get the colour of the amazon that is moved.
	 * 
	 * @param index		The index of the move to be decoded.
	 * @return			The colour of the amazon that will be moved.
	 */
	public int getColour(int index)
	{
		return decodePortion(index, PLAYER_COLOUR);
	}
	
	/**
	 * Get the index of the amazon.
	 * 
	 * @param index		The index of the move to be decoded.
	 * @return			The index within the board's amazons array.
	 */
	public int getAmazonIndex(int index)
	{
		return decodePortion(index, AMAZON_ARRAY_INDEX);
	}
	
	/**
	 * Get the row the amazon finishes in.
	 * 
	 * @param index		The index of the move to be decoded.
	 * @return			The row the amazon finish at.
	 */
	public int getFinishRow(int index)
	{
		return decodePortion(index, AMAZON_ROW_FINISH);
	}
	
	/**
	 * Get the column the amazon finishes in.
	 * 
	 * @param index		The index of the move to be decoded.
	 * @return			The column the amazon finishes at.
	 */
	public int getFinishColumn(int index)
	{
		return decodePortion(index, AMAZON_COLUMN_FINISH);
	}
	
	/**
	 * Get the row the arrow is placed in.
	 * 
	 * @param index		The index of the move to be decoded.
	 * @return			The row the arrow is placed.
	 */
	public int getArrowRow(int index)
	{
		return decodePortion(index, ARROW_ROW);
	}
	
	/**
	 * Get the column the arrow is placed at.
	 * 
	 * @param index		The index of the move to be decoded.
	 * @return			The column the arrow is placed.
	 */
	public int getArrowColumn(int index)
	{
		return decodePortion(index, ARROW_COLUMN);
	}
	
	/**
	 * Clears the state of internal iterator. After calling function,
	 * the next call to nextMove() will return first move added.
	 */
	public void clearIteratorState()
	{
		iteratorPosition = 0;
	}
	
	/**
	 * Check if there are any more moves to iterate over.
	 * 
	 * @return	{@code true} if there are more moves to iterate over,
	 * 			{@code false} otherwise.
	 */
	public boolean hasIterations()
	{
		return iteratorPosition < nextPos;
	}
	
	/**
	 * Return the next position to be iterated over.
	 * 
	 * @return	The index of the next move to be checked.
	 */
	public int nextIterableIndex()
	{
		return iteratorPosition++;
	}
	
	/**
	 * Create a new array to hold moves that is exactly the number of elements
	 * in the current move array.
	 * 
	 * @return	{@code true} if procedure completed, {@code false} otherwise.
	 */
	public boolean condense()
	{
		if (nextPos == moves.length)
			return true;
		
		int[] temp = new int[nextPos];
		for (int i = 0; i < nextPos; i++)
		{
			temp[i] = moves[i];
		}
		moves = temp;
		
		return true;
	}
	
	/**
	 * Set size of the move list.  Right now. this assumes what you put in is
	 * sane, so don't you dare mess with it!
	 * 
	 * @param size		The desired size of the move manager.
	 * @return			{@code true} if size set successfully, {@code false}
	 * 					otherwise.
	 */
	public boolean setSize(int size)
	{
		this.nextPos = size;
		
		return true;
	}
	
	/**
	 * Sort the moves according to some other array, in descending order.
	 * 
	 * @param sortBy	The array to the moves according to.  Likely going to be
	 * 					heuristic scores.
	 */
	public void sort(int[] sortBy)
	{
		GlobalFunctions.dualQuickSort(this.moves, sortBy, 0, GlobalFunctions.min(size(), sortBy.length - 1), (byte)(-1));
	}
	
	/**
	 * Shuffle the set of moves randomly.
	 */
	public void shuffle()
	{
		Random random = new Random();
		random.nextInt();
		for (int i = 0; i < nextPos; i++)
		{
			int j = i + random.nextInt(nextPos - i);
			GlobalFunctions.swap(moves, i, j);
		}
	}
	
	/**
	 * Get the move at a given index.
	 * 
	 * @param index		The index of the move to return.
	 * @return			The encoded move.
	 */
	public int getMove(int index)
	{
		return moves[index];
	}
}
