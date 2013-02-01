package snozama.amazons.mechanics;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;

/**
 * Game of the Amazons Board class.
 * @author Graeme Douglas
 * @author Cody Clerke
 *
 */
public class Board
{
	/**
	 * Size of a dimension of the board.xs
	 */
	public static final int SIZE = 10;
	
	/**
	 * The game board, represented as a two-dimensional array.  The first
	 * dimension is the row, the second the column.
	 * 
	 * Starting setup
	 *  -WHITE at [6][0], [9][3], [9][6], [6][9]
	 *  -BLACK at [3][0], [0][3], [0][6], [3][9]
	 *  -the rest will be empty
	 */
	byte board[][];
	
	/**
	 * The position of the amazons.  WHITE positions is first element, BLACK
	 * second.
	 */
	byte amazons[][];
	
	/**
	 * Constant byte value representing an empty position on the board.
	 */
	public static final byte EMPTY = 0;
	
	/**
	 * Constant byte value representing a non-empty position on the board.
	 */
	public static final byte OCCUPIED = 1;
	
	/**
	 * Constant integer value representing the white player.
	 */
	public static final int WHITE = 0;
	
	/**
	 * Constant integer value representing the black player.
	 */
	public static final int BLACK = 1;
	
	/**
	 * Basic constructor.  Will generate a board in game ready form.
	 */
	public Board()
	{
		board = new byte[SIZE][SIZE];
		amazons = new byte[2][4];
		
		// Initially setup the board.
		for (int i = 0; i < SIZE; i++)
		{
			Arrays.fill(board[i], EMPTY);
		}
		
		// WHITE initial setup.
		board[6][0] = OCCUPIED;
		board[9][3] = OCCUPIED;
		board[9][6] = OCCUPIED;
		board[6][9] = OCCUPIED;
		amazons[WHITE][0] = encodeAmazonPosition(6, 0);
		amazons[WHITE][1] = encodeAmazonPosition(9, 3);
		amazons[WHITE][2] = encodeAmazonPosition(9, 6);
		amazons[WHITE][3] = encodeAmazonPosition(6, 9);
		
		// BLACK initial setup.
		board[3][0] = OCCUPIED;
		board[0][3] = OCCUPIED;
		board[0][6] = OCCUPIED;
		board[3][9] = OCCUPIED;
		amazons[BLACK][0] = encodeAmazonPosition(3, 0);
		amazons[BLACK][1] = encodeAmazonPosition(0, 3);
		amazons[BLACK][2] = encodeAmazonPosition(0, 6);
		amazons[BLACK][3] = encodeAmazonPosition(3, 9);
	}
	
	/**
	 * Constructor which clones an already existing board.
	 * 
	 * @param toClone	The board which is to be cloned.
	 */
	public Board(Board toClone)
	{
		this.board = toClone.copy();
		amazons = new byte[2][4];
		
		for (int i = 0; i < amazons.length; i++)
		{
			for (int j = 0; j < amazons[i].length; j++)
			{
				this.amazons[i][j] = toClone.amazons[i][j];
			}
		}
	}
	
	/**
	 * Determine if a board position is currently occupied.
	 * @param row		The row of the position to check.
	 * @param col		The column of the position to check.
	 * @return	@value true if it is occupied, @value false otherwise.
	 */
	public boolean isOccupied(int row, int col)
	{
		return board[row][col] != EMPTY;
	}
	
	/**
	 * Determine if a board position is currently occupied by a white amazon.
	 * @param row		The row of the position to check.
	 * @param col		The column of the position to check.
	 * @return	@value true if it is occupied by a white amazon, @value false
	 * 			otherwise.
	 */
	public boolean isWhite(int row, int col)
	{
		for (int i = 0; i < amazons[WHITE].length; i++)
		{
			int arow = decodeAmazonRow((amazons[WHITE][i]));
			int acol = decodeAmazonColumn((amazons[WHITE][i]));
			
			if (arow == row && acol == col)
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Determine if a board position is currently occupied by a black amazon.
	 * @param row		The row of the position to check.
	 * @param col		The column of the position to check.
	 * @return	@value true if it is occupied by a black amazon, @value false
	 * 			otherwise.
	 */
	public boolean isBlack(int row, int col)
	{
		for (int i = 0; i < amazons[BLACK].length; i++)
		{
			int arow = decodeAmazonRow((amazons[BLACK][i]));
			int acol = decodeAmazonColumn((amazons[BLACK][i]));
			
			if (arow == row && acol == col)
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Determine if a board position is currently occupied by an arrow.
	 * 
	 * Note: If you can avoid using this method, do so.  Use isOccupied() instead.
	 * @param row		The row of the position to check.
	 * @param col		The column of the position to check.
	 * @return	@value true if it is occupied by an arrow, @value false
	 * 			otherwise.
	 */
	public boolean isArrow(int row, int col)
	{
		return isOccupied(row, col) && !isBlack(row, col) && !isWhite(row, col);
	}
	
	/**
	 * Makes sure a proposed move is allowed
	 * @param row_s		Row of starting position.
	 * @param col_s		Column of starting position.
	 * @param row_f		Row of finishing position.
	 * @param col_f		Column of finishing position.
	 * @return	@value true if the move is allowed, @value false otherwise.
	 */
	public boolean isValidMove(int row_s, int col_s, int row_f, int col_f)
	{
		// Move must be made horizontally, vertically, diagonally or anti-diagonally
		if (!(row_s == row_f ||
				col_s == col_f ||
				Math.abs(row_f - row_s) == Math.abs(col_f - col_s)))
		{
			return false;
		}
		
		// Move must be made within the board boundaries
		if (row_s < 0 || row_s > 9 ||
			row_f < 0 || row_f > 9 ||
			col_s < 0 || col_s > 9 ||
			col_f < 0 || col_f > 9)
		{
			return false;
		}
		
		// Make sure squares in between are clear
		// Move cannot start and end in same place
		if (row_s == row_f && col_s == col_f)
		{
			return false;
		}

		// Final square must be empty
		if (this.isOccupied(row_f, col_f))
		{
			return false;
		}
		
		// Check horizontally
		if (row_s == row_f)
		{
			int a = Math.min(col_s, col_f);
			int b = Math.max(col_s, col_f);
			for (a++; a < b; a++)
			{
				if (this.isOccupied(row_s, a))
				{
					return false;
				}
			}
		}
		// Check vertically
		else if (col_s == col_f)
		{
			int a = Math.min(row_s, row_f);
			int b = Math.max(row_s, row_f);
			for (a++; a < b; a++)
			{
				if (this.isOccupied(a, col_f))
				{
					return false;
				}
			}
		}
		// Check anti-diagonally
		else if (row_s - row_f > 0 != col_s - col_f > 0)
		{
			int a = Math.min(row_s, row_f);
			int b = Math.max(row_s, row_f);
			int c = Math.max(col_s, col_f) - 1;
			for (a++; a < b; a++, c--)
			{
				if (this.isOccupied(a, c))
				{
					return false;
				}
			}
		}
		else // Check diagonally
		{
			int a = Math.min(row_s, row_f);
			int b = Math.max(row_s, row_f);
			int c = Math.min(col_s, col_f) + 1;
			for (a++; a < b; a++, c++)
			{
				if (this.isOccupied(a, c))
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Move amazon from the desired location to the desired location.
	 * @param row_s		Row of the starting position of the amazon.
	 * @param col_s		Column of the starting position of the amazon.
	 * @param row_f		Row of the finishing position of the amazon.
	 * @param col_f		Column of the finishing position of the amazon.
	 * @param colour	Colour of the amazon.  Must be @value BLACK or
	 * 					@value WHITE.
	 * @return	@value true if the amazon was successfully moved,
	 * 			@value false otherwise.
	 */
	public boolean moveAmazon(int row_s, int col_s, int row_f, int col_f, int colour)
	{
		// Sanity check.
		if (!isValidMove(row_s, col_s, row_f, col_f) ||
				colour != WHITE && colour != BLACK)
		{
			return false;
		}
		
		board[row_s][col_s] = EMPTY;
		board[row_f][col_f] = OCCUPIED;
		
		//Update amazon's new position
		for (int i = 0; i < amazons[colour].length; i++)
		{
			int arow = decodeAmazonRow((amazons[colour][i]));
			int acol = decodeAmazonColumn((amazons[colour][i]));
			
			if (arow == row_s && acol == col_s)
			{
				amazons[colour][i] = encodeAmazonPosition(row_f, row_s);
				break;
			}
		}
		return true;
	}
	
	/**
	 * Place an arrow in the desired location.
	 * @param arow		Row of the position of amazon shooting arrow.
	 * @param acol		Column of the position of amazon shooting arrow.
	 * @param row_f		Row of the position where arrow is desired.
	 * @param col_f		Column of the position where arrow is desired.
	 * @return	@value true if arrow was placed, @value false otherwise.
	 */
	public boolean placeArrow(int arow, int acol, int row_f, int col_f)
	{
		// Sanity check.
		if (!isValidMove(arow, acol, row_f, col_f))
		{
			return false;
		}
		
		board[row_f][col_f] = OCCUPIED;
		return true;
	}
	
	/**
	 * Move an amazon and shoot an arrow
	 * @param row_s		Row of the starting position of the amazon
	 * @param col_s		Column of the starting position of the amazon
	 * @param row_f		Row of the finishing position of the amazon
	 * @param col_f		Column of the finishing position of the amazon
	 * @param arow		Row of the position where arrow is desired
	 * @param acol		Column of the position where arrow is desired
	 * @return	@value true if complete move was successful
	 * 			@value false otherwise
	 */
	public boolean move(int row_s, int col_s, int row_f, int col_f, int arow, int acol, byte colour)
	{
		return true;
	}
	
	/**
	 * Get the row of an amazon from its encoded position.
	 * @param encodedPosition	The encoded position of the amazon.
	 * @return	The row of the position of the amazon.
	 */
	public static int decodeAmazonRow(byte encodedPosition)
	{
		return (int)(encodedPosition % SIZE);
	}
	
	/**
	 * Get the column of an amazon from its encoded position.
	 * @param encodedPosition	The encoded position of the amazon.
	 * @return	The column of the position of the amazon.
	 */
	public static int decodeAmazonColumn(byte encodedPosition)
	{
		return (int)(encodedPosition / SIZE);
	}
	
	/**
	 * Generate an encoded position for an amazon from its (row, col) position.
	 * @param row		The row of the position of the amazon.
	 * @param col		The column of the position of the amazon.
	 * @return	The encoded position of the amazon.
	 */
	public static byte encodeAmazonPosition(int row, int col)
	{
		return (byte)(col*(SIZE) + row);
	}
	
	/**
	 * Generate all possible successors of the current board.
	 * 
	 * @return The list of successors.
	 */
	public Collection<Board> getSuccessors(int colour)
	{
		ArrayDeque<Board> successors = new ArrayDeque<Board>();
		
		for (int j = 0; j < this.amazons[colour].length; j++) //for each amazon of a colour (4)
		{
			int arow = Board.decodeAmazonRow(this.amazons[colour][j]);		//amazon's starting row position
			int acol = Board.decodeAmazonColumn(this.amazons[colour][j]);	//amazon's starting column position
			
			// TODO: Need to figure out how to get arrow placed.
			// The following comments assume [0][0] is considered top left
			// Find moves to the right
			for (int c = acol+1; c < Board.SIZE; c++)
			{
				if (this.isOccupied(arow, c))
				{
					break;
				}
				else // this is a legal move
				{
					Board newBoard = new Board(this);
					newBoard.moveAmazon(arow, acol, arow, c, colour);
				}
			}
			// Find moves to the left
			for (int c = acol-1; c > -1; c--)
			{
				if (this.isOccupied(arow, c))
				{
					break;
				}
				else // this is a legal move
				{
				}
			}
			// Find moves below
			for (int r = arow+1; r < Board.SIZE; r++)
			{
				if (this.isOccupied(r, acol))
				{
					break;
				}
				else // this is a legal move
				{
				}
			}
			// Find moves above
			for (int r = arow-1; r > -1; r--)
			{
				if (this.isOccupied(r, acol))
				{
					break;
				}
				else // this is a legal move
				{
				}
			}
			// Find moves diagonally (\) to the right
			for (int r = arow+1, c = acol+1; r < Board.SIZE && c < Board.SIZE; r++, c++)
			{
				if (this.isOccupied(r, c))
				{
					break;
				}
				else // this is a legal move
				{
				}
			}
			// Find moves diagonally (\) to the left
			for (int r = arow-1, c = acol-1; r > -1 && c > -1; r--, c--)
			{
				if (this.isOccupied(r, c))
				{
					break;
				}
				else // this is a legal move
				{
				}
			}
			// Find moves anti-diagonally (/) to the right
			for (int r = arow-1, c = acol+1; r > -1 && c < Board.SIZE; r--, c++)
			{
				if (this.isOccupied(r, c))
				{
					break;
				}
				else // this is a legal move
				{
				}
			}
			// Find moves anti-diagonally (/) to the left
			for (int r = arow+1, c = acol-1; r < Board.SIZE && c > -1; r++, c--)
			{
				if (this.isOccupied(r, c))
				{
					break;
				}
				else // this is a legal move
				{
				}
			}
		}
		return successors;
	}
	
	// TODO: This needs to be renamed.  Copy is deceiving.
	/**
	 * Makes a copy of the game board.
	 * @return	The copy of the game board.
	 */
	public byte[][] copy()
	{
		byte[][] original = board;
		byte[][] copy = new byte[SIZE][SIZE];
		for (int r = 0; r < SIZE; r++)
		{
			for (int c = 0; c < SIZE; c++)
			{
				copy[r][c] = original[r][c];
			}
		}
		return copy;
	}
	
	
	/**
	 * Determines if the board is a terminal state or not.
	 * 
	 * @return	{@value true} if the board is a terminal state board,
	 * 			{@value false} otherwise.
	 */
	public boolean isTerminal()
	{
		int whiteMoves = 0;
		int blackMoves = 0;
		for (int i = 0; i < amazons[WHITE].length; i++)
		{
			whiteMoves += SnozamaHeuristic.getNumberAvailableMoves(this, amazons[WHITE][i]);
			if (whiteMoves > 0)
			{
				break;
			}
		}
		for (int i = 0; i < amazons[BLACK].length; i++)
		{
			whiteMoves += SnozamaHeuristic.getNumberAvailableMoves(this, amazons[BLACK][i]);
			if (blackMoves > 0)
			{
				break;
			}
		}
		
		//if either colour cannot move the game is over
		if (whiteMoves == 0 || blackMoves == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
