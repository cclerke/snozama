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
	 * Test.
	 */
	@Test
	public void testFindArrowPlacements()
	{
		Board board = new Board();
		SnozamaHeuristic heuristic = new SnozamaHeuristic();
		
		assertTrue(heuristic.findAvailableArrowPlacements(board, 6, 0) == 20);
	}
}
