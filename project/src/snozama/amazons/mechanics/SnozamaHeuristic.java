package snozama.amazons.mechanics;

/**
 * Heuristic functions to be used to play the Game of the Amazons.
 * @author Cody Clerke
 *
 */

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
	 * Evaluates the board based on the heuristics MSP and min-mobility.
	 * @param board		The current board state.
	 * @param activePlayer	The player whose turn it is.
	 * @param turn		The current turn number.
	 * @return	The score for the active player of the given board position.
	 */
	public int evaluateBoard(Board board, int activePlayer, int turn)
	{
		if (turn <= 30)
		{
			return 3*MSP(board, activePlayer) + 2*minMobility(board, activePlayer);
		}
		else
		{
			return MSP(board, activePlayer);
		}
	}
	
	/**
	* Calculates closest player to each open square on the board.
	* The closest player to a square owns that square.
	* @param board	The current board state.
	* @param activePlayer	The player (white or black) whose turn it is.
	* @return	The difference between the number of squares the active player owns and the number of squares the inactive player owns.
	*/
	public int MSP(Board board, int activePlayer)
	{
		int whiteAdv = minPliesToSquare(board);
		if (activePlayer == Board.WHITE)
			return whiteAdv; //returns white's advantage
		else // activePlayer is black
			return -1*whiteAdv; //returns black's advantage
	}
	
	
	/**
	* Calculates the number of moves available to the amazon of each colour with the minimum mobility.
	* @param board	The current board state.
	* @param activePlayer	The player (white or black) whose turn it is.
	* @return	The difference between the minimum moves across all amazons of the active player and
	*  the minimum moves across all amazons of the inactive player.
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
	* Calculates the difference between squares white owns and squares black owns.
	* @param board	The current board state.
	* @return	The white player's MSP advantage.
	*/
	public int minPliesToSquare(Board board)
	{
		int whiteAdv = 0;
		byte[][] markedBoard = board.copy();
		/*
		 * Given a square on the board, find the closest player of the given player
		 * Strategy:
		 * 	Use a bi-directional approach
		 * 	Start with position of amazons and expand, marking each square they can reach in one move
		 * 	From unmarked squares expand vertically, horizontally, diagonally to find marked squares
		 * 		-these squares will be two moves away from an amazon
		 * 	Continue doing this with unmarked squares until there are no more unmarked squares
		 * 	In the case of an enclosed region, we may need a maximum number of iterations before declaring square neutral
		 */
		
		/*
		 * Step 1: Start with position of amazons and expand, marking each square they can reach in one move
		 */
		for (int i = 0; i < board.amazons.length; i++) //for each colour of amazon (2)
		{
			for (int j = 0; j < board.amazons[i].length; j++) //for each amazon of a colour (4)
			{
				int arow = Board.decodeAmazonRow(board.amazons[i][j]);		//amazon's starting row position
				int acol = Board.decodeAmazonColumn(board.amazons[i][j]);	//amazon's starting column position
				
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
						whiteAdv += markSquare(markedBoard, arow, c, i);
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
						whiteAdv += markSquare(markedBoard, arow, c, i);
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
						whiteAdv += markSquare(markedBoard, r, acol, i);;
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
						whiteAdv += markSquare(markedBoard, r, acol, i);
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
						whiteAdv += markSquare(markedBoard, r, c, i);
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
						whiteAdv += markSquare(markedBoard, r, c, i);
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
						whiteAdv += markSquare(markedBoard, r, c, i);
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
						whiteAdv += markSquare(markedBoard, r, c, i);
					}
				}
			}
		}// end of Step 1

			/*
			 * Step 2: For each unmarked square find a path to a marked square
			 * 			Repeat until all squares are marked or we run a set number of iterations
			 */
			int maxIterations = 8;
			for (int itr = 2; itr <= maxIterations; itr++)
			{
				for (int row = 0; row < Board.SIZE; row++)
				{
					for (int col = 0; col < Board.SIZE; col++)
					{
						if (markedBoard[row][col] == 0)
						{
							whiteAdv += findMarkedSquares(board, markedBoard, row, col, itr);
						}
					}
				}
			}
		return whiteAdv;
	} //end of Step 2
	
	/**
	 * Used for the first iteration of minPliesToSquare heuristic.
	 * Marks square with a colour (white=10, black=20) plus the number of moves for the closest player to reach that square.
	 * A square reached by a white player in one move will be marked 11, for example.
	 * Squares that can be reached in equal number of turns by both colours with be marked 'N'.
	 * @param markedBoard	The board maintaining the owners of each square.
	 * @param row	The row of the square being marked.
	 * @param col	The column of the square being marked.
	 * @param colour	The colour of the amazon able to reach this square.
	 * @return		The owner of the square. Will return 1 for white, -1 for black and 0 for neutral.
	 */
	private int markSquare(byte[][] markedBoard, int row, int col, int colour)
	{
		int whiteAdv = 0;
		byte mark = (byte)(colour*10+11); // white->11, black->21
		if (markedBoard[row][col] == 0) // if not already marked
		{
			markedBoard[row][col] = mark;
			if (colour == Board.WHITE)
				whiteAdv++; //white may own square
			else
				whiteAdv--; //black owns square
		}
		else if (mark > markedBoard[row][col])
		{
			markedBoard[row][col] = 'N'; //square is neutral
			whiteAdv--; 	//negates white's point given earlier
		}
		return whiteAdv;
	}
	
	/**
	 * Used for the second part of minPliesToSquare heuristic.
	 * From each unmarked square attempts to find a path to the nearest amazon(s).
	 * @param board		The current state of the entire board.
	 * @param markedBoard	The board maintaining the owners of each square.
	 * @param row		The row of the square being marked.
	 * @param col		The column of the square being marked.
	 * @param iteration	The minimum number of turns to reach an amazon from this square.
	 * @return		The owner of the square. Will return 1 for white, -1 for black, 0 for neutral.
	 */
	private int findMarkedSquares(Board board, byte[][] markedBoard, int row, int col, int iteration)
	{
		int whiteAdv = 0;
		// The following comments assume [0][0] is considered top left
		// Find moves to the right
		for (int c = col+1; c < Board.SIZE; c++)
		{
			if (board.isOccupied(row, c))
			{
				break;
			}
			else if (markedBoard[row][c] != 0)
			{
				if (markedBoard[row][c] == 'N') //neutral
				{
					markedBoard[row][col] = 'N';
					return 0;
				}
				else if (markedBoard[row][c] == 10+iteration-1) //marked by white in the previous iteration
				{
					if (markedBoard[row][col] == 0) //if starting square is unmarked
					{
						markedBoard[row][col] = (byte)(10+iteration);
						whiteAdv = 1;
					}
					else if (markedBoard[row][col] == 20+iteration) //if starting square was marked by black this iteration
					{
						markedBoard[row][col] = 'N';
						return 0;
					}
				}
				else if (markedBoard[row][c] == 20+iteration-1) //marked by black in the previous iteration
				{
					if (markedBoard[row][col] == 0) //if starting square is unmarked
					{
						markedBoard[row][col] = (byte)(20+iteration);
						whiteAdv = -1;
					}
					else if (markedBoard[row][col] == 10+iteration) //if starting square was marked by white this iteration
					{
						markedBoard[row][col] = 'N';
						return 0;
					}
				}
			}
		}
		// Find moves to the left
		for (int c = col-1; c > -1; c--)
		{
			if (board.isOccupied(row, c))
			{
				break;
			}
			else if (markedBoard[row][c] != 0)
			{
				if (markedBoard[row][c] == 'N') //neutral
				{
					markedBoard[row][col] = 'N';
					return 0;
				}
				else if (markedBoard[row][c] == 10+iteration-1) //marked by white in the previous iteration
				{
					if (markedBoard[row][col] == 0)
					{
						markedBoard[row][col] = (byte)(10+iteration);
						whiteAdv++;
					}
					else if (markedBoard[row][col] == 20+iteration)
					{
						markedBoard[row][col] = 'N';
						return 0;
					}
				}
				else if (markedBoard[row][c] == 20+iteration-1) //marked by black in the previous iteration
				{
					if (markedBoard[row][col] == 0)
					{
						markedBoard[row][col] = (byte)(20+iteration);
						return -1;
					}
					else if (markedBoard[row][col] == 10+iteration)
					{
						markedBoard[row][col] = 'N';
						return 0;
					}
				}
			}
		}
		// Find moves below
		for (int r = row+1; r < Board.SIZE; r++)
		{
			if (board.isOccupied(r, col))
			{
				break;
			}
			else if (markedBoard[r][col] != 0)
			{
				if (markedBoard[r][col] == 'N') //neutral
				{
					markedBoard[row][col] = 'N';
					return 0;
				}
				else if (markedBoard[r][col] == 10+iteration-1) //marked by white in the previous iteration
				{
					if (markedBoard[row][col] == 0)
					{
						markedBoard[row][col] = (byte)(10+iteration);
						whiteAdv++;
					}
					else if (markedBoard[row][col] == 20+iteration)
					{
						markedBoard[row][col] = 'N';
						return 0;
					}
				}
				else if (markedBoard[r][col] == 20+iteration-1) //marked by black in the previous iteration
				{
					if (markedBoard[row][col] == 0)
					{
						markedBoard[row][col] = (byte)(20+iteration);
						return -1;
					}
					else if (markedBoard[row][col] == 10+iteration)
					{
						markedBoard[row][col] = 'N';
						return 0;
					}
				}
			}
		}
		// Find moves above
		for (int r = row-1; r > -1; r--)
		{
			if (board.isOccupied(r, col))
			{
				break;
			}
			else if (markedBoard[r][col] != 0)
			{
				if (markedBoard[r][col] == 'N') //neutral
				{
					markedBoard[row][col] = 'N';
					return 0;
				}
				else if (markedBoard[r][col] == 10+iteration-1) //marked by white in the previous iteration
				{
					if (markedBoard[row][col] == 0)
					{
						markedBoard[row][col] = (byte)(10+iteration);
						whiteAdv++;
					}
					else if (markedBoard[row][col] == 20+iteration)
					{
						markedBoard[row][col] = 'N';
						return 0;
					}
				}
				else if (markedBoard[r][col] == 20+iteration-1) //marked by black in the previous iteration
				{
					if (markedBoard[row][col] == 0)
					{
						markedBoard[row][col] = (byte)(20+iteration);
						return -1;
					}
					else if (markedBoard[row][col] == 10+iteration)
					{
						markedBoard[row][col] = 'N';
						return 0;
					}
				}
			}
		}
		// Find moves diagonally (\) to the right
		for (int r = row+1, c = col+1; r < Board.SIZE && c < Board.SIZE; r++, c++)
		{
			if (board.isOccupied(r, c))
			{
				break;
			}
			else if (markedBoard[r][c] != 0)
			{
				if (markedBoard[r][c] == 'N') //neutral
				{
					markedBoard[row][col] = 'N';
					return 0;
				}
				else if (markedBoard[r][c] == 10+iteration-1) //marked by white in the previous iteration
				{
					if (markedBoard[row][col] == 0)
					{
						markedBoard[row][col] = (byte)(10+iteration);
						whiteAdv++;
					}
					else if (markedBoard[row][col] == 20+iteration)
					{
						markedBoard[row][col] = 'N';
						return 0;
					}
				}
				else if (markedBoard[r][c] == 20+iteration-1) //marked by black in the previous iteration
				{
					if (markedBoard[row][col] == 0)
					{
						markedBoard[row][col] = (byte)(20+iteration);
						return -1;
					}
					else if (markedBoard[row][col] == 10+iteration)
					{
						markedBoard[row][col] = 'N';
						return 0;
					}
				}
			}
		}
		// Find moves diagonally (\) to the left
		for (int r = row-1, c = col-1; r > -1 && c > -1; r--, c--)
		{
			if (board.isOccupied(r, c))
			{
				break;
			}
			else if (markedBoard[r][c] != 0)
			{
				if (markedBoard[r][c] == 'N') //neutral
				{
					markedBoard[row][col] = 'N';
					return 0;
				}
				else if (markedBoard[r][c] == 10+iteration-1) //marked by white in the previous iteration
				{
					if (markedBoard[row][col] == 0)
					{
						markedBoard[row][col] = (byte)(10+iteration);
						whiteAdv++;
					}
					else if (markedBoard[row][col] == 20+iteration)
					{
						markedBoard[row][col] = 'N';
						return 0;
					}
				}
				else if (markedBoard[r][c] == 20+iteration-1) //marked by black in the previous iteration
				{
					if (markedBoard[row][col] == 0)
					{
						markedBoard[row][col] = (byte)(20+iteration);
						return -1;
					}
					else if (markedBoard[row][col] == 10+iteration)
					{
						markedBoard[row][col] = 'N';
						return 0;
					}
				}
			}
		}
		// Find moves anti-diagonally (/) to the right
		for (int r = row-1, c = col+1; r > -1 && c < Board.SIZE; r--, c++)
		{
			if (board.isOccupied(r, c))
			{
				break;
			}
			else if (markedBoard[r][c] != 0)
			{
				if (markedBoard[r][c] == 'N') //neutral
				{
					markedBoard[row][col] = 'N';
					return 0;
				}
				else if (markedBoard[r][c] == 10+iteration-1) //marked by white in the previous iteration
				{
					if (markedBoard[row][col] == 0)
					{
						markedBoard[row][col] = (byte)(10+iteration);
						whiteAdv++;
					}
					else if (markedBoard[row][col] == 20+iteration)
					{
						markedBoard[row][col] = 'N';
						return 0;
					}
				}
				else if (markedBoard[r][c] == 20+iteration-1) //marked by black in the previous iteration
				{
					if (markedBoard[row][col] == 0)
					{
						markedBoard[row][col] = (byte)(20+iteration);
						return -1;
					}
					else if (markedBoard[row][col] == 10+iteration)
					{
						markedBoard[row][col] = 'N';
						return 0;
					}
				}
			}
		}
		// Find moves anti-diagonally (/) to the left
		for (int r = row+1, c = col-1; r < Board.SIZE && c > -1; r++, c--)
		{
			if (board.isOccupied(r, c))
			{
				break;
			}
			else if (markedBoard[r][c] != 0)
			{
				if (markedBoard[r][c] == 'N') //neutral
				{
					markedBoard[row][col] = 'N';
					return 0;
				}
				else if (markedBoard[r][c] == 10+iteration-1) //marked by white in the previous iteration
				{
					if (markedBoard[row][col] == 0)
					{
						markedBoard[row][col] = (byte)(10+iteration);
						whiteAdv++;
					}
					else if (markedBoard[row][col] == 20+iteration)
					{
						markedBoard[row][col] = 'N';
						return 0;
					}
				}
				else if (markedBoard[r][c] == 20+iteration-1) //marked by black in the previous iteration
				{
					if (markedBoard[row][col] == 0)
					{
						markedBoard[row][col] = (byte)(20+iteration);
						return -1;
					}
					else if (markedBoard[row][col] == 10+iteration)
					{
						markedBoard[row][col] = 'N';
						return 0;
					}
				}
			}
		}
		return whiteAdv;
	}
	
	/**
	* Calculates the number of moves available to the amazon.
	* @param board	The current board state.
	* @param amazon	An individual amazon to find possible moves for.
	* @return	The number of moves available to the amazon.
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
