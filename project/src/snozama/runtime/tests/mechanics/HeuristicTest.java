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
	
	@Test
	public void timeHeuristics()
	{
		Board board = new Board();
		SnozamaHeuristic heuristic = new SnozamaHeuristic();

		long startTime = System.currentTimeMillis();

		for (int i = 0; i < 2000; i++)
		{
			heuristic.minMobility(board, Board.WHITE);
			heuristic.minPliesToSquare(board);
		}

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println(elapsedTime);
		
		assertTrue(elapsedTime < 400);
	}
}
