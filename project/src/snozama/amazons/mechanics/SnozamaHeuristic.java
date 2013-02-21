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
	 * @param board			The current board state.
	 * @param activePlayer	The player whose turn it is.
	 * @param turn			The current turn number.
	 * @return	The score for the active player of the given board position.
	 */
	public static int evaluateBoard(Board board, int activePlayer, int turn)
	{
		byte[][] markedBoard = colourBoard(board);

		if (turn <= 30)
		{
			return 3*MSP(markedBoard, activePlayer) + 2*minMobility(board, activePlayer);
		}
		else
		{
			return MSP(markedBoard, activePlayer);
		}
	}

	/**
	 * Calculates closest player to each open square on the board.
	 * The closest player to a square owns that square.
	 * @param board			The current board state.
	 * @param activePlayer	The player (white or black) whose turn it is.
	 * @return	The difference between the number of squares the active player owns and the number of squares the inactive player owns.
	 */
	public static int MSP(byte[][] markedBoard, int activePlayer)
	{
		int whiteAdv = 0;
		for (int row = 0; row < markedBoard.length; row++)
		{
			for (int col = 0; col < markedBoard[row].length; col ++)
			{
				// Will result in 0 if square is empty/occupied,
				// 1 if can be reached faster by white,
				// 2 if can be reached faster by black
				int mark = markedBoard[row][col]/10;

				if (mark == 1) //if square belongs to white
					whiteAdv++;
				else if (mark == 2) //if square belongs to black
					whiteAdv--;
			}
		}

		if (activePlayer == Board.WHITE)
			return whiteAdv; //returns white's advantage
		else // activePlayer is black
			return -whiteAdv; //returns black's advantage
	}

	/**
	 * Calculates the number of moves available to the amazon of each colour with the minimum mobility.
	 * @param board	The current board state.
	 * @param activePlayer	The player (white or black) whose turn it is.
	 * @return	The difference between the minimum moves across all amazons of the active player and
	 *  the minimum moves across all amazons of the inactive player.
	 */
	public static int minMobility(Board board, int activePlayer)
	{
		int whiteMoves = Integer.MAX_VALUE;
		int blackMoves = Integer.MAX_VALUE;

		int totalWhiteMoves = 0; //used for total team mobility
		int totalBlackMoves = 0; //used for total team mobility

		//for each white amazon
		for (int i = 0; i < 4; i++)
		{
			int amazonMoves = getNumberAvailableMoves(board, board.amazons[Board.WHITE][i]); //calculates number of moves available to amazon
			if (amazonMoves < whiteMoves)
				whiteMoves = amazonMoves;
			totalWhiteMoves += amazonMoves;
		}
		//for each black amazon
		for (int i = 0; i < 4; i++)
		{
			int amazonMoves = getNumberAvailableMoves(board, board.amazons[Board.BLACK][i]);
			if (amazonMoves < blackMoves)
				blackMoves = amazonMoves;
			totalBlackMoves += amazonMoves;
		}

		if (activePlayer == Board.WHITE)
			return whiteMoves-blackMoves;
		else //activePLayer is black
			return blackMoves-whiteMoves;
	}

	/**
	 * Heuristic designed to distribute amazons evenly across the board.
	 * The heuristic awards points for amazons all being in separate quadrants and evenly distributed in board halves.
	 * The heuristic deducts points for too many amazons being in the same quadrant or same half of the board.
	 * @param board			The current state of the board.
	 * @param activePlayer	The colour of the active player.
	 * @return		A score based on how evenly distributed the amazons of the given colour are.
	 */
	public static int quadrants(Board board, int activePlayer)
	{
		int score = 0;
		int adj = 2;
		int[] quadrant = new int [4]; // 0 = NW, 1 = NE, 2 = SW, 3 = SE
		int[] topBounds = {0, 0, Board.SIZE/2, Board.SIZE/2};
		int[] bottomBounds = {Board.SIZE/2, Board.SIZE/2, Board.SIZE, Board.SIZE};
		int[] leftBounds = {0, Board.SIZE/2, 0, Board.SIZE/2};
		int[] rightBounds = {Board.SIZE/2, Board.SIZE, Board.SIZE/2, Board.SIZE};

		for (int i = 0; i < quadrant.length; i++)
		{
			quadrant[i] = findInRegion(board, topBounds[i], bottomBounds[i], 
					leftBounds[i], rightBounds[i], activePlayer);

			if (quadrant[i] == 1)
				score += adj;
			else if (quadrant[i] > 2)
				score -= 2*adj;
		}

		//Top half of board
		int north = quadrant[0] + quadrant[1];
		if (north == 2)
			score += adj;
		else if (north == 0 || north == 4)
			score -= adj;

		//Left half of board
		int west = quadrant[0] + quadrant[2];
		if (west == 2)
			score += adj;
		else if (west == 0 || west == 4)
			score -= adj;

		return score;
	}

	/**
	 * Variation of minimum stone play (MSP) that values larger contiguous areas over smaller areas.
	 * The heuristic finds each region on the board owned by each colour.
	 * A region is scored as the number of squares in the region squared.
	 * The score for each region is added to the score for the colour owning that region.
	 * @param markedBoard	The board maintaining the owners of each square.
	 * @param activePlayer	The player whose turn it is.
	 * @return	The value of the regions owned by the active player in this board position.
	 */
	public static int areaMSP(byte[][] markedBoard, int activePlayer)
	{
		int whiteArea = 0;
		int blackArea = 0;
		for (int i = 0; i < Board.SIZE; i++)
		{
			for (int j = 0; j < Board.SIZE; j++)
			{
				// Checks if square is not empty/occupied, not marked neutral, not already visited
				if (markedBoard[i][j] > 10 && markedBoard[i][j] != 'N' && markedBoard[i][j] < 100)
				{
					int colour = markedBoard[i][j]/10; //Result in 1 for white, 2 for black
					int area = visit(markedBoard, i, j, colour);
					if (colour-1 == Board.WHITE)
					{
						whiteArea += Math.pow(area, 2);
					}
					else if (colour-1 == Board.BLACK)
					{
						blackArea += Math.pow(area, 2);
					}
				}
			}
		}
		if (activePlayer == Board.WHITE)
			return whiteArea - blackArea;
		else //active player is black
			return blackArea - whiteArea;
	}
	
	/**
	 * Variation of minimum stone play (MSP) that values squares that can be reached in fewer plies.
	 * The heuristic weights each square owned by the player by the number of plies it will take to reach.
	 * @param markedBoard	The board maintaining the owners of each square.
	 * @param ActivePlayer	The player whose turn it is.
	 * @return	The value of the squares owned by the active player in this board position.
	 */
	public static int distanceMSP(byte[][] markedBoard, int activePlayer)
	{
		int whiteScore = 0;
		int blackScore = 0;
		
		if (activePlayer == Board.WHITE)
			return whiteScore - blackScore;
		else //active player is black
			return blackScore - whiteScore;
	}

	
	
	
	
	/*
	 * The functions below are helper functions for the main heuristics:
	 * 
	 * 	- colourBoard required for all variants of MSP
	 * 	- markSquare used by colourBoard (marks square starting from amazon)
	 * 	- findMarkedSquares used by colourBoard
	 * 	- markSquare used by colourBoard (marks square starting from empty square)
	 * 
	 * 	- getNumberAvailableMoves used by minMobility
	 * 	- findAvailableArrowPlacements used by minMobility
	 * 
	 * 	- findInRegion used by quadrants
	 * 
	 * 	- visit used by areaMSP
	 */



	
	
	/**
	 * Calculates closest player to each open square on the board.
	 * The closest player to a square owns that square.
	 * @param board	The current board state.
	 * @return	Returns a copy of the board that is coloured by which players owns each square
	 * 	and the number of plies it takes to reach that square.
	 */
	public static byte[][] colourBoard(Board board)
	{
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
						markSquare(markedBoard, arow, c, i);
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
						markSquare(markedBoard, arow, c, i);
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
						markSquare(markedBoard, r, acol, i);;
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
						markSquare(markedBoard, r, acol, i);
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
						markSquare(markedBoard, r, c, i);
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
						markSquare(markedBoard, r, c, i);
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
						markSquare(markedBoard, r, c, i);
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
						markSquare(markedBoard, r, c, i);
					}
				}
			}
		}// end of Step 1


		/*
		 * Step 2: For each unmarked square find a path to a marked square
		 * 	Repeat until all squares are marked or we run a set number of iterations
		 */
		int[] unmarked = new int[92];
		int index = 0;
		for (int row = 0; row < Board.SIZE; row++)
		{
			for (int col = 0; col < Board.SIZE; col++)
			{
				if (markedBoard[row][col] == 0)
				{
					findMarkedSquares(board, markedBoard, row, col, 2);
					if (markedBoard[row][col] == 0) //square still cannot be reached in this iteration
					{
						unmarked[index++] = row*10 + col; //put unmarked square in list to check later
					}
				}
			}
		}
		int maxIterations = 8;
		for (int itr = 3; itr < maxIterations; itr++)
		{
			for (int i = 0; i < index; i++) //for each unmarked square
			{
				int row = unmarked[i]/10;
				int col = unmarked[i]%10;
				if (markedBoard[row][col] == 0)
				{
					findMarkedSquares(board, markedBoard, row, col, itr);
				}
			}
		}
		//end of Step 2

		return markedBoard;
	}

	/**
	 * Used for the first iteration of <code>colourBoard</code>.
	 * Marks square with a colour (white=10, black=20) plus the number of moves for the closest player to reach that square.
	 * A square reached by a white player in one move will be marked 11, for example.
	 * Squares that can be reached in equal number of turns by both colours with be marked 'N'.
	 * @param markedBoard	The board maintaining the owners of each square.
	 * @param row	The row of the square being marked.
	 * @param col	The column of the square being marked.
	 * @param colour	The colour of the amazon able to reach this square.
	 */
	private static void markSquare(byte[][] markedBoard, int row, int col, int colour)
	{
		byte mark = (byte)(colour*10+11); // white->11, black->21
		if (markedBoard[row][col] == 0) // if not already marked
		{
			markedBoard[row][col] = mark;

		}
		else if (mark > markedBoard[row][col])
		{
			markedBoard[row][col] = 'N'; //square is neutral
		}
	}

	/**
	 * Used for the second part of <code>colourBoard</code>.
	 * From each unmarked square attempts to find a path to the nearest amazon(s).
	 * @param board		The current state of the entire board.
	 * @param markedBoard	The board maintaining the owners of each square.
	 * @param row		The row of the square being marked.
	 * @param col		The column of the square being marked.
	 * @param iteration	The minimum number of turns to reach an amazon from this square.
	 */
	private static void findMarkedSquares(Board board, byte[][] markedBoard, int row, int col, int iteration)
	{
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
				markSquare(markedBoard, row, c, row, col, iteration);
				if (markedBoard[row][col] == 'N')
				{
					return;
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
				markSquare(markedBoard, row, c, row, col, iteration);
				if (markedBoard[row][col] == 'N')
				{
					return;
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
				markSquare(markedBoard, r, col, row, col, iteration);
				if (markedBoard[row][col] == 'N')
				{
					return;
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
				markSquare(markedBoard, r, col, row, col, iteration);
				if (markedBoard[row][col] == 'N')
				{
					return;
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
				markSquare(markedBoard, r, c, row, col, iteration);
				if (markedBoard[row][col] == 'N')
				{
					return;
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
				markSquare(markedBoard, r, c, row, col, iteration);
				if (markedBoard[row][col] == 'N')
				{
					return;
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
				markSquare(markedBoard, r, c, row, col, iteration);
				if (markedBoard[row][col] == 'N')
				{
					return;
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
				markSquare(markedBoard, r, c, row, col, iteration);
				if (markedBoard[row][col] == 'N')
				{
					return;
				}
			}
		}
	}

	/**
	 * Used for the second part of <code>colourBoard</code>.
	 * Marks an unmarked square with the colour of the player who can reach the square fastest.
	 * If both players can reach the square equally fast, the square will be marked neutral.
	 * @param markedBoard	The board maintaining the owners of each square.
	 * @param row		The row of a previously marked square indicating a path to an amazon in this iteration.
	 * @param col		The column of a previously marked square indicating a path to an amazon in this iteration.
	 * @param row_s		The row of the square being marked.
	 * @param col_s		The column of the square being marked.
	 * @param itr		The minimum number of turns to reach an amazon from the square being marked.
	 */
	private static void markSquare(byte[][] markedBoard, int row, int col, int row_s, int col_s, int itr)
	{	
		if (markedBoard[row][col] == 'N') //found a neutral square
		{
			markedBoard[row_s][col_s] = 'N'; //start square is neutral
			return;
		}
		else if (markedBoard[row][col] == 10+itr-1) //found square marked by white in previous iteration
		{
			if (markedBoard[row_s][col_s] == 0) //if starting square is unmarked
			{
				markedBoard[row_s][col_s] = (byte)(10+itr); //white can reach start square in i moves
			}
			else if (markedBoard[row_s][col_s] == 20+itr) //black can reach start square in same iteration
			{
				markedBoard[row_s][col_s] = 'N'; //both players can reach square in i moves, square is neutral
				return;
			}
		}
		else if (markedBoard[row][col] == 20+itr-1) //found sqaure marked by black in previous iteration
		{
			if (markedBoard[row_s][col_s] == 0) //if starting square is unmarked
			{
				markedBoard[row_s][col_s] = (byte)(20+itr); //black can reach start square in i moves
			}
			else if (markedBoard[row_s][col_s] == 10+itr) //white can reach start square in same iteration
			{
				markedBoard[row_s][col_s] = 'N'; //both players can reach start square in i moves, square is neutral
				return;
			}
		}
	}

	/**
	 * Calculates the number of moves available to the amazon.
	 * @param board	The current board state.
	 * @param amazon	An individual amazon to find possible moves for.
	 * @return	The number of moves available to the amazon.
	 */
	private static int getNumberAvailableMoves(Board board, byte amazon)
	{
		int moves = 0;

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
				moves += findAvailableArrowPlacements(board, arow, c, arow, acol);
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
				moves += findAvailableArrowPlacements(board, arow, c, arow, acol);
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
				moves += findAvailableArrowPlacements(board, r, acol, arow, acol);
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
				moves += findAvailableArrowPlacements(board, r, acol, arow, acol);
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
				moves += findAvailableArrowPlacements(board, r, c, arow, acol);
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
				moves += findAvailableArrowPlacements(board, r, c, arow, acol);
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
				moves += findAvailableArrowPlacements(board, r, c, arow, acol);
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
				moves += findAvailableArrowPlacements(board, r, c, arow, acol);
			}
		}

		return moves;
	}

	/**
	 * Finds and returns the number of places an arrow can be placed from a specified square.
	 * @param board		The current state of the board.
	 * @param arow		The row the amazon is in.
	 * @param acol		The column the amazon is in.
	 * @param row_s		The row the amazon began the move in.
	 * @param col_s		The column the amazon began the move in.
	 * @return		The number of places an arrow can be placed from specified square.
	 */
	private static int findAvailableArrowPlacements(Board board, int arow, int acol, int row_s, int col_s)
	{
		int arrows = 0;
		// The following comments assume [0][0] is considered top left
		// Find moves to the right
		for (int c = acol+1; c < Board.SIZE; c++)
		{
			if (board.isOccupied(arow, c) && !(arow == row_s && c == col_s))
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
			if (board.isOccupied(arow, c) && !(arow == row_s && c == col_s))
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
			if (board.isOccupied(r, acol) && !(r == row_s && acol == col_s))
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
			if (board.isOccupied(r, acol) && !(r == row_s && acol == col_s))
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
			if (board.isOccupied(r, c) && !(r == row_s && c == col_s))
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
			if (board.isOccupied(r, c) && !(r == row_s && c == col_s))
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
			if (board.isOccupied(r, c) && !(r == row_s && c == col_s))
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
			if (board.isOccupied(r, c) && !(r == row_s && c == col_s))
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

	/**
	 * Counts the number of amazons of given colour that are within given region.
	 * @param board		The current board position.
	 * @param top		The first row to search in.
	 * @param bottom	The bottom bounds of the search. This row is not searched.
	 * @param left		The first column to search in.
	 * @param right		The right bounds of the search. This column is not searched.
	 * @param colour	The colour of the amazons to count.
	 * @return		The number of amazons of the given colour found in the region.
	 */
	private static int findInRegion(Board board, int top, int bottom, int left, int right, int colour)
	{
		int count = 0;

		for (int row = top; row < bottom; row++)
		{
			for (int col = left; col < right; col++)
			{
				if (colour == Board.WHITE)
				{
					if (board.isWhite(row, col))
						count++;
				}
				else
				{
					if (board.isBlack(row, col))
						count++;
				}
			}
		}

		return count;
	}

	/**
	 * Counts the number of contiguous squares in a region.
	 * The function works recursively checking if the neighbours of the current square are the same colour.
	 * It is modelled after the flood fill algorithm using a depth-first approach.
	 * @param row		The row of the current square being visited.
	 * @param col		The column of the current square being visited.
	 * @param colour	The colour the current region belongs to.
	 * @return		The number of unvisited squares connected to the starting square.
	 */
	public static int visit(byte[][] markedBoard, int row, int col, int colour)
	{
		markedBoard[row][col] += 100; // Indicates this square has been visited.
		int area = 1;

		// Check square to the right
		if (col != Board.SIZE-1 && markedBoard[row][col+1]/10 == colour)
		{
			area += visit(markedBoard, row, col+1, colour);
		}
		// Check square to the left
		if (col != 0 && markedBoard[row][col-1]/10 == colour)
		{
			area += visit(markedBoard, row, col-1, colour);
		}
		// Check square below
		if (row != Board.SIZE-1 && markedBoard[row+1][col]/10 == colour)
		{
			area += visit(markedBoard, row+1, col, colour);
		}
		// Check square above
		if (row != 0 && markedBoard[row-1][col]/10 == colour)
		{
			area += visit(markedBoard, row-1, col, colour);
		}
		// Check square diagonally (\) to the right
		if (row != Board.SIZE-1 && col != Board.SIZE-1 && markedBoard[row+1][col+1]/10 == colour)
		{
			area += visit(markedBoard, row+1, col+1, colour);
		}
		// Check square diagonally (\) to the left
		if (row != 0 && col != 0 && markedBoard[row-1][col-1]/10 == colour)
		{
			area += visit(markedBoard, row-1, col-1, colour);
		}
		// Check square anti-diagonally (/) to the right
		if (row != 0 && col != Board.SIZE-1 && markedBoard[row-1][col+1]/10 == colour)
		{
			area += visit(markedBoard, row-1, col+1, colour);
		}
		// Check square anti-diagonally (/) to the left
		if (row != Board.SIZE-1 && col != 0 && markedBoard[row+1][col-1]/10 == colour)
		{
			area += visit(markedBoard, row+1, col-1, colour);
		}

		return area;
	}
}
