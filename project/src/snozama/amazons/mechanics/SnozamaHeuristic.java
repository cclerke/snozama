package snozama.amazons.mechanics;

public class SnozamaHeuristic {

	
	/*
	 * Heuristic strategy:
	 * 	Use linear combination of minimum stone ply (MSP) and min-mobility (and regions, if implemented)
	 * 	This is best in early stages of game (up to turn 30) when open space is more important
	 * 		0.2*Regions/Territory + 0.5*MSP + 0.3*Mobility (suggested weighting)
	 * 		-OR-
	 * 		0.6*MSP + 0.4*Mobility
	 * 	Use MSP during endgame (after turn 30)
	 * 	This is suggested later when owning squares becomes more important
	 */
	
	/**
	* Calculates closest player to each open square on the board
	* The closest player to a square owns that square
	* @param board	The current board state
	* @param activePlayer	The player (white or black) whose turn it is
	* @return	The difference between the number of squares the active player owns and the number of squares the inactive player owns
	*/
	public int MSP(Board board, byte activePlayer)
	{
		int whiteSquares = 0;
		int blackSquares = 0;
		
		// Loops through each empty square on board
		for (int row = 0; row < 10; row++)
		{
			for (int col = 0; col < 10; col++)
			{
				if (!board.isOccupied(row, col))
				{
					int whiteStonePly = minPliesToSquare(board, Board.WHITE);
					int blackStonePly = minPliesToSquare(board, Board.BLACK);

					if (whiteStonePly < blackStonePly) //white is closer to the square
						whiteSquares++;
					else if (blackStonePly < whiteStonePly) //black is closer to the square
						blackSquares++;
					//else both players are equal plies from the square -> square is neutral

					if (activePlayer == Board.WHITE)
						return whiteSquares-blackSquares; //returns white's advantage
					else // activePlayer is black
						return blackSquares-whiteSquares; //returns black's advantage
				}
			}
		}
	}
	
	
	/**
	* Calculates the number of moves available to the amazon of each colour with the minimum mobility
	* @param board	The current board state
	* @param activePlayer	The player (white or black) whose turn it is
	* @return	The difference between the minimum moves across all amazons of the active player and
	*  the minimum moves across all amazons of the inactive player
	*/
	public int minMobility(Board board, int activePlayer)
	{
		int whiteMoves = Integer.MAX_VALUE;
		int blackMoves = Integer.MAX_VALUE;
		
		//for each white amazon
		for (int i = 0; i < 4; i++)
		{
			int amazonMoves = getNumberAvailableMoves(board, board.amazons[Board.WHITE][i]); //calculates number of moves available to amazon
			if (amazonMoves < whiteMoves)
				whiteMoves = amazonMoves;
		}
		//for each black amazon
		for (int i = 0; i < 4; i++)
		{
			int amazonMoves = getNumberAvailableMoves(board, board.amazons[Board.BLACK][i]);
			if (amazonMoves < blackMoves)
				blackMoves = amazonMoves;
		}
		
		if (activePlayer == Board.WHITE)
			return whiteMoves-blackMoves;
		else //activePLayer is black
			return blackMoves-whiteMoves;
	}

	/**
	* Calculates the number of moves available to the amazon of each colour with the minimum mobility
	* @param board	The current board state
	* @param player	The player (white or black) we want to find the minimum distance to
	* @return	The minimum number of plies for player of given colour to reach square
	*/
	private int minPliesToSquare(Board board, byte player)
	{
		int min = Integer.MAX_VALUE;
		//TODO Create this function
		/*
		 * Given a square on the board, find the closest player of the given player
		 * Strategy:
		 * 	Expand vertically, horizontally, diagonally to find players one move away
		 * 	I don't know what to do after this yet for best efficiency
		 */
		for (int x = 0; x < 10; x++)
		{
			for (int y = 0; y < 10; y++)
			{
				if (board.isOccupied(x, y))
					continue;
				
				//TODO The smart stuff mentioned above
			}
		}
		return min;
	}
	
	/**
	* Calculates the number of moves available to the amazon
	* @param board	The current board state
	* @param amazon	An individual amazon
	* @return	The number of moves available to the amazon
	*/
	private int getNumberAvailableMoves(Board board, byte amazon)
	{
		int moves = 0;
		/*
		 * Given an amazon, calculate the number of moves available to it
		 * Strategy: (can be improved I'm sure)
		 * 	Create list of all possible moves for this amazon (will likely have a similar function somewhere else)
		 * 	Return number of elements in the list
		 */
		int arow = Board.decodeAmazonRow(amazon);		//amazon's starting row position
		int acol = Board.decodeAmazonColumn(amazon);	//amazon's starting column position
		
		// The following comments assume [0][0] is considered top left
		// Find moves to the right
		for (int c = acol+1; c < Board.SIZE; c++)
		{
			if (board.isOccupied(arow, c))
			{
				break;
			}
			else // this is a legal move
			{
				moves += findAvailableArrowPlacements(board, arow, c);
			}
		}
		// Find moves to the left
		for (int c = acol-1; c > -1; c--)
		{
			if (board.isOccupied(arow, c))
			{
				break;
			}
			else // this is a legal move
			{
				moves += findAvailableArrowPlacements(board, arow, c);
			}
		}
		// Find moves below
		for (int r = arow+1; r < Board.SIZE; r++)
		{
			if (board.isOccupied(r, acol))
			{
				break;
			}
			else // this is a legal move
			{
				moves += findAvailableArrowPlacements(board, r, acol);
			}
		}
		// Find moves above
		for (int r = arow-1; r > -1; r--)
		{
			if (board.isOccupied(r, acol))
			{
				break;
			}
			else // this is a legal move
			{
				moves += findAvailableArrowPlacements(board, r, acol);
			}
		}
		// Find moves diagonally (\) to the right
		for (int r = arow+1, c = acol+1; r < Board.SIZE && c < Board.SIZE; r++, c++)
		{
			if (board.isOccupied(r, c))
			{
				break;
			}
			else // this is a legal move
			{
				moves += findAvailableArrowPlacements(board, r, c);
			}
		}
		// Find moves diagonally (\) to the left
		for (int r = arow-1, c = acol-1; r > -1 && c > -1; r--, c--)
		{
			if (board.isOccupied(r, c))
			{
				break;
			}
			else // this is a legal move
			{
				moves += findAvailableArrowPlacements(board, r, c);
			}
		}
		// Find moves anti-diagonally (/) to the right
		for (int r = arow-1, c = acol+1; r > -1 && c < Board.SIZE; r--, c++)
		{
			if (board.isOccupied(r, c))
			{
				break;
			}
			else // this is a legal move
			{
				moves += findAvailableArrowPlacements(board, r, c);
			}
		}
		// Find moves anti-diagonally (/) to the left
		for (int r = arow+1, c = acol-1; r < Board.SIZE && c > -1; r++, c--)
		{
			if (board.isOccupied(r, c))
			{
				break;
			}
			else // this is a legal move
			{
				moves += findAvailableArrowPlacements(board, r, c);
			}
		}

		return moves;
	}

	/**
	 * Finds and returns the number of places an arrow can be placed from a specified square.
	 * @param board		The current state of the board.
	 * @param arow		The row the amazon is in.
	 * @param acol		The column the amazon is in.
	 * @return		The number of places an arrow can be placed from specified square.
	 */
	public int findAvailableArrowPlacements(Board board, int arow, int acol)
	{
		int arrows = 0;
		// The following comments assume [0][0] is considered top left
		// Find moves to the right
		for (int c = acol+1; c < Board.SIZE; c++)
		{
			if (board.isOccupied(arow, c))
			{
				break;
			}
			else // this is a legal move
			{
				arrows++;
			}
		}
		// Find moves to the left
		for (int c = acol-1; c > -1; c--)
		{
			if (board.isOccupied(arow, c))
			{
				break;
			}
			else // this is a legal move
			{
				arrows++;
			}
		}
		// Find moves below
		for (int r = arow+1; r < Board.SIZE; r++)
		{
			if (board.isOccupied(r, acol))
			{
				break;
			}
			else // this is a legal move
			{
				arrows++;
			}
		}
		// Find moves above
		for (int r = arow-1; r > -1; r--)
		{
			if (board.isOccupied(r, acol))
			{
				break;
			}
			else // this is a legal move
			{
				arrows++;
			}
		}
		// Find moves diagonally (\) to the right
		for (int r = arow+1, c = acol+1; r < Board.SIZE && c < Board.SIZE; r++, c++)
		{
			if (board.isOccupied(r, c))
			{
				break;
			}
			else // this is a legal move
			{
				arrows++;
			}
		}
		// Find moves diagonally (\) to the left
		for (int r = arow-1, c = acol-1; r > -1 && c > -1; r--, c--)
		{
			if (board.isOccupied(r, c))
			{
				break;
			}
			else // this is a legal move
			{
				arrows++;
			}
		}
		// Find moves anti-diagonally (/) to the right
		for (int r = arow-1, c = acol+1; r > -1 && c < Board.SIZE; r--, c++)
		{
			if (board.isOccupied(r, c))
			{
				break;
			}
			else // this is a legal move
			{
				arrows++;
			}
		}
		// Find moves anti-diagonally (/) to the left
		for (int r = arow+1, c = acol-1; r < Board.SIZE && c > -1; r++, c--)
		{
			if (board.isOccupied(r, c))
			{
				break;
			}
			else // this is a legal move
			{
				arrows++;
			}
		}
		return arrows;
	}
}
