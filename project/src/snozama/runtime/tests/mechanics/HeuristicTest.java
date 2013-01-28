package snozama.runtime.tests.mechanics;

import static org.junit.Assert.*;

import org.junit.Test;

import snozama.amazons.mechanics.Board;
import snozama.amazons.mechanics.SnozamaHeuristic;

/**
 * Unit tests for snozama.amazons.mechanics.SnozamaHeuristic.java
 * @author Cody Clerke
 *
 */
public class HeuristicTest {

	/**
	 * Test finding all arrow placements for a given amazon in a given board state.
	 */
	@Test
	public void testFindArrowPlacements()
	{
		Board board = new Board();
		SnozamaHeuristic heuristic = new SnozamaHeuristic();
		
		assertTrue(heuristic.findAvailableArrowPlacements(board, 6, 0) == 20);
		assertTrue(heuristic.findAvailableArrowPlacements(board, 9, 6) == 20);
		assertTrue(heuristic.findAvailableArrowPlacements(board, 0, 3) == 20);
	}
	
	/**
	 * Test min-mobility heuristic.
	 */
	@Test
	public void testMinMobility()
	{
		Board board = new Board();
		SnozamaHeuristic heuristic = new SnozamaHeuristic();
		
		assertTrue(heuristic.minMobility(board, Board.WHITE) == 0);
	}
	
	@Test
	public void testMinPlies()
	{
		Board board = new Board();
		SnozamaHeuristic heuristic = new SnozamaHeuristic();
		
		assertTrue(heuristic.minPliesToSquare(board) == 0);
	}
}
