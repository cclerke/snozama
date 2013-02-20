package snozama.amazons.mechanics;

/**
 * A move containing all information needed to complete a move in a unencoded
 * format.
 * 
 * @author Graeme Douglas
 *
 */
public class MoveChoice {
	/**
	 * The colour of the player that is making their move.
	 */
	int colour;
	
	/**
	 * The index of the amazon that is to be moved.
	 */
	int amazonIndex;
	
	/**
	 * The row of the starting position of the amazon.
	 */
	int row_s;
	
	/**
	 * The column of the starting position of the amazon.
	 */
	int col_s;
	
	/**
	 * The row of the final position of the amazon.
	 */
	int row_f;
	
	/**
	 * The column of the final position of the amazon.
	 */
	int col_f;
	
	/**
	 * The row of the arrow that is placed.
	 */
	int arrowRow;
	
	/**
	 * The column of the arrow that is placed.
	 */
	int arrowCol;
	
	/**
	 * Default constructor.
	 * 
	 * @param moveManager	MoveManager object that this move is encoded in.
	 * @param index			The index of the move from
	 * 						<code> moveManager </code>.
	 */
	public MoveChoice(MoveManager moveManager, int index, Board board)
	{
		colour = moveManager.getColour(index);
		amazonIndex = moveManager.getAmazonIndex(index);
		row_f = moveManager.getFinishRow(index);
		col_f = moveManager.getFinishColumn(index);
		arrowRow = moveManager.getArrowRow(index);
		arrowCol = moveManager.getArrowColumn(index);
		
		byte position = board.amazons[colour][amazonIndex];
		
		row_s = Board.decodeAmazonRow(position);
		col_s = Board.decodeAmazonColumn(position);
	}
	
	/**
	 * Constructor from an arbitrary move and the relevant board. 
	 * 
	 * @param move		An arbitrary move to be decoded.
	 * @param board		The board the move is applied to.
	 */
	public MoveChoice(int move, Board board)
	{
		int[] decoded = MoveManager.decodeMove(move);
		
		colour = decoded[MoveManager.PLAYER_COLOUR];
		amazonIndex = decoded[MoveManager.AMAZON_ARRAY_INDEX];
		row_f = decoded[MoveManager.AMAZON_ROW_FINISH];
		col_f = decoded[MoveManager.AMAZON_COLUMN_FINISH];
		arrowRow = decoded[MoveManager.ARROW_ROW];
		arrowCol = decoded[MoveManager.ARROW_COLUMN];
		
		byte position = board.amazons[colour][amazonIndex];
		
		row_s = Board.decodeAmazonRow(position);
		col_s = Board.decodeAmazonColumn(position);
	}
	
	/**
	 * Return a string representation of the move.
	 */
	public String toString()
	{
		return "(" + row_s + ", " + col_s + ") moves to (" + row_f + ", "+
					col_f + ") and then shoots to ("+ arrowRow + ", " +
					arrowCol+ ")";
	}
}
