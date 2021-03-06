package snozama.amazons.mechanics;

/**
 * Regions calculation code.
 * 
 * @author Alex Yakovlev
 * 
 */
public class Regions
{
	public static final int FULL = 0;
	public static final int LATERAL = 1;
	public static final int DIAGONAL = 2;

	private static byte[][] regions = new byte[Board.SIZE][Board.SIZE];
	private static Board board;
	private static int regionCount = 1;
	private static int[] regionSquareCount = new int[100];

	/* For display only */
	private static boolean lateral = true;
	private static boolean diagonal = true;

	/** Set to true when actually evaluation, false for display */
	private static boolean eval = false;

	/* For playing */
	private static boolean evalLateral = true;
	private static boolean evalDiagonal = true;
	
	/**
	 * Calculate full region region heuristic.
	 * 
	 * @param board			The board state to evaluate the heuristic over.
	 * @param whoseTurn		Either {@code Board.BLACK} or {@code Board.WHITE}.
	 * @return				The full regions heuristic value of the board.
	 */
	public static int region(Board board, int whoseTurn)
	{
		return region(board, whoseTurn, FULL);
	}
	
	/**
	 * Calculate region region heuristic.
	 * 
	 * @param board			The board state to evaluate the heuristic over.
	 * @param whoseTurn		Either {@code Board.BLACK} or {@code Board.WHITE}.
	 * @param type			Either:
	 * 							{@code this.LATERAL} for lateral regions.
	 * 							{@code this.DIAGONAL} for diagonal regions.
	 * 							{@code this.FULL} for full regions.
	 * @return				The regions heuristic value of the board.
	 */
	public static int region(Board board, int whoseTurn, int type)
	{

		setEvalRegionType(type);

		eval = true;
		calcRegions(board);
		eval = false;

		int[] numWhiteByRegion = new int[regionCount + 1];
		int[] numBlackByRegion = new int[regionCount + 1];
		for (int i = 0; i < numWhiteByRegion.length; i++)
		{
			numWhiteByRegion[i] = 0;
		}
		for (int i = 0; i < numBlackByRegion.length; i++)
		{
			numBlackByRegion[i] = 0;
		}

		for (int color = 0; color < 2; color++)
		{
			for (int i = 0; i < board.amazons[color].length; i++)
			{
				int row = Board.decodeAmazonRow(board.amazons[color][i]);
				int col = Board.decodeAmazonColumn(board.amazons[color][i]);

				if (color == Board.WHITE)
				{
					numWhiteByRegion[regions[row][col]]++;
				} else
				{
					numBlackByRegion[regions[row][col]]++;
				}
			}
		}

		int score = 0;

		for (int i = 1; i <= regionCount; i++)
		{
			int queenRatio = numWhiteByRegion[i] - numBlackByRegion[i];
			score += queenRatio * regionSquareCount[i];
		}

		return whoseTurn == Board.WHITE ? score : -score;
	}

	/**
	 * Set the evaluation type for the heuristic.
	 * 
	 * @param regionType		The region type; one of:
	 * 								{@code this.LATERAL} for lateral regions.
	 * 								{@code this.DIAGONAL} for diagonal regions.
	 * 								{@code this.FULL} for full regions.
	 */
	public static void setEvalRegionType(int regionType)
	{
		if (regionType == FULL)
		{
			evalLateral = true;
			evalDiagonal = true;
		} else if (regionType == LATERAL)
		{
			evalLateral = true;
			evalDiagonal = false;
		} else if (regionType == DIAGONAL)
		{
			evalLateral = false;
			evalDiagonal = true;
		}
	}

	/**
	 * Set the region tye for displaying.
	 * 
	 * NOTE: For Display ONLY.
	 * 
	 * @param regionType		The region type; one of:
	 * 								{@code this.LATERAL} for lateral regions.
	 * 								{@code this.DIAGONAL} for diagonal regions.
	 * 								{@code this.FULL} for full regions.
	 */
	public static void setRegionType(int regionType)
	{
		if (regionType == FULL)
		{
			lateral = true;
			diagonal = true;
		} else if (regionType == LATERAL)
		{
			lateral = true;
			diagonal = false;
		} else if (regionType == DIAGONAL)
		{
			lateral = false;
			diagonal = true;
		}
	}
	
	/**
	 * Find the regions of a board.
	 * 
	 * @param board		The board to calculate the regions for.
	 * @return			A two-dimensional array with regions found.
	 */
	public static byte[][] calcRegions(Board board)
	{
		regions = new byte[Board.SIZE][Board.SIZE];
		Regions.board = board;

		// initialize with zeros
		for (int i = 0; i < Board.SIZE; i++)
		{
			for (int j = 0; j < Board.SIZE; j++)
			{
				regions[i][j] = 0;
			}
		}

		regionCount = 1;
		for (int i = 1; i < regionSquareCount.length; i++)
		{
			regionSquareCount[i] = 0;
		}

		for (int i = 0; i < Board.SIZE; i++)
		{
			for (int j = 0; j < Board.SIZE; j++)
			{
				if (Regions.board.isArrow(i, j))
				{
					regions[i][j] = -1;
				} else if (regions[i][j] == 0)
				{
					claimRegion(i, j, regionCount++);
				}
			}
		}

		return regions;
	}
	
	/**
	 * Mark a part of the board to belong to a specific region.
	 * 
	 * @param row			The row of the location to claim.
	 * @param col			The column of the location to claim.
	 * @param regionCount	Which region the location is claimed by.
	 */
	public static void claimRegion(int row, int col, int regionCount)
	{
		regions[row][col] = (byte) regionCount;
		regionSquareCount[regionCount]++;

		if ((eval && evalLateral) || (!eval && lateral))
		{
			// go up
			if (row > 0 && !board.isArrow(row - 1, col)
					&& regions[row - 1][col] == 0)
			{
				claimRegion(row - 1, col, regionCount);
			}

			// go left
			if (col > 0 && !board.isArrow(row, col - 1)
					&& regions[row][col - 1] == 0)
			{
				claimRegion(row, col - 1, regionCount);
			}

			// go down
			if (row < Board.SIZE - 1 && !board.isArrow(row + 1, col)
					&& regions[row + 1][col] == 0)
			{
				claimRegion(row + 1, col, regionCount);
			}

			// go right
			if (col < Board.SIZE - 1 && !board.isArrow(row, col + 1)
					&& regions[row][col + 1] == 0)
			{
				claimRegion(row, col + 1, regionCount);
			}

		}

		if ((eval && evalDiagonal) || (!eval && diagonal))
		{

			// go diagonal left up (\)
			if (row > 0 && col > 0 && !board.isArrow(row - 1, col - 1)
					&& regions[row - 1][col - 1] == 0)
			{
				claimRegion(row - 1, col - 1, regionCount);
			}

			// go diagonal left down (/)
			if (col > 0 && row < Board.SIZE - 1
					&& !board.isArrow(row + 1, col - 1)
					&& regions[row + 1][col - 1] == 0)
			{
				claimRegion(row + 1, col - 1, regionCount);
			}

			// go diagonal right down (\)
			if (row < Board.SIZE - 1 && col < Board.SIZE - 1
					&& !board.isArrow(row + 1, col + 1)
					&& regions[row + 1][col + 1] == 0)
			{
				claimRegion(row + 1, col + 1, regionCount);
			}

			// go diagonal right up (/)
			if (row > 0 && col < Board.SIZE - 1
					&& !board.isArrow(row - 1, col + 1)
					&& regions[row - 1][col + 1] == 0)
			{
				claimRegion(row - 1, col + 1, regionCount);
			}
		}

	}

	/**
	 * Create an initial simulated board to use in computing regions.
	 * 
	 * @return	Unclaimed regions board.
	 */
	public static byte[][] initRegions()
	{
		byte[][] regions = new byte[Board.SIZE][Board.SIZE];

		for (int i = 0; i < Board.SIZE; i++)
		{
			for (int j = 0; j < Board.SIZE; j++)
			{
				regions[i][j] = 1;
			}
		}

		return regions;
	}

}
