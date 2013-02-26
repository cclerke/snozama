package snozama.amazons.mechanics.killerheuristic;

import java.util.Arrays;

/**
 * Killer heuristic management class.
 * 
 * @author Graeme Douglas
 * 
 * 
 */
public class KillerTable {
	/**
	 * Array storing moves.
	 * 
	 * For depth d, the available moves positions start at 
	 */
	private int[] table;
	
	/**
	 * The maximum depth of move storable in the table.
	 */
	public final int maxDepth = 92;
	
	/**
	 * The maximum number of moves stored per depth.
	 */
	public final int movesPerDepth;
	
	/**
	 * Default constructor.
	 */
	public KillerTable()
	{
		this.movesPerDepth = 2;
		table = new int[(maxDepth+1)*(2 + 1)];
		Arrays.fill(table, 0);
	}
	
	/**
	 * Constructor where number of moves to store per depth is specified.
	 * 
	 * @param movesPerDepth		The maximum number of moves to store per depth.
	 */
	public KillerTable(int movesPerDepth)
	{
		this.movesPerDepth = movesPerDepth;
		table = new int[(maxDepth+1)*(movesPerDepth + 1)];
		Arrays.fill(table, 0);
	}
	
	/**
	 * Insert a move in the Killer move manager.
	 * 
	 * @param move		The move to be remembered.
	 * @param depth		The depth of the move to be remembered.
	 * @return			{@code true} if the move was added without overwriting
	 * 					a previous move, false otherwise.
	 */
	public void put(int move, int depth)
	{
		int index = getStartingIndex(depth) - 1;
		
		// Write in next available spot.
		table[index+table[index]+1] = move;
		
		// Update next avalailable spot.
		table[index] = (table[index]+1) % movesPerDepth;
	}
	
	/**
	 * Get the move stored at index.
	 * 
	 * @param index		The index of the move stored.
	 * @return			The encoded move stored.
	 */
	public int get(int index)
	{
		return table[index];
	}
	
	/**
	 * Get the index of the first stored move for this depth.
	 * 
	 * @param depth		The depth of the move
	 * @return			The index of the first move for the current depth.
	 */
	public int getStartingIndex(int depth)
	{
		return ((depth)*(movesPerDepth+1))+1;
	}
}
