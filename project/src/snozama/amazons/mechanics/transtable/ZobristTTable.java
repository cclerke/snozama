package snozama.amazons.mechanics.transtable;

import java.util.Random;

/**
 * A transposition table using Zobrist hashing.
 * 
 * @author Graeme Douglas
 *
 */
public class ZobristTTable implements TranspositionTable
{
	/**
	 * Arrow index for Zobrist hash.
	 */
	private static final int ARROW = 0;
	
	/**
	 * White amazon index for Zobrist hash.
	 */
	private static final int WHITE_AMAZON = 1;
	
	/**
	 * Black amazon index for Zobrist hash.
	 */
	private static final int BLACK_AMAZON = 2;
	
	// TODO: Can we make this general to all ZobristTTables?  Does it matter?
	/**
	 * Table storing all possible Zobrist values.
	 */
	private final long[][][] zobristValues;
	
	// TODO: Decide record format.  Long _should_ be big enough.
	/**
	 * The hash table.  The key is the result of the Zobrist hash; the record
	 * is a long with all the relevant information.
	 */
	public long[] hashTable;
	
	/**
	 * Default constructor for the transposition table.
	 */
	public ZobristTTable()
	{
		zobristValues = new long[3][10][10];
		
		Random rand = new Random();
		
		// Create the Zobrist value lookup.
		for (int t = 0; t < zobristValues.length; t++)
		{
			for (int i = 0; i < zobristValues[t].length; i++)
			{
				for (int j = 0; i < zobristValues[t][i].length; j++)
				{
					zobristValues[t][i][j] = rand.nextLong();
				}
			}
		}
		
		// TODO: What is the correct size?
		hashTable = new long[10000000];
	}
	
	
	
}
