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
		
		assertTrue(SnozamaHeuristic.minMobility(board, Board.WHITE) == 0);
	}
	
	@Test
	public void testMinPlies()
	{
		Board board = new Board();

		assertTrue(SnozamaHeuristic.minPliesToSquare(board) == 0);
	}
	
	@Test
	public void testQuadrants()
	{
		Board board = new Board();		
		int adj = 3;
		
		assertEquals(SnozamaHeuristic.quadrants(board, Board.WHITE), 0);
		assertEquals(SnozamaHeuristic.quadrants(board, Board.BLACK), 0);
		
		board.moveAmazon(6, 0, 1, 5, Board.WHITE);
		
		assertEquals(SnozamaHeuristic.quadrants(board, Board.WHITE), 2*adj);
	}
	
	@Test
	public void timeHeuristics()
	{
		Board board = new Board();

		long startTime = System.currentTimeMillis();

		for (int i = 0; i < 2000; i++)
		{
			SnozamaHeuristic.minMobility(board, Board.WHITE);
			SnozamaHeuristic.minPliesToSquare(board);
		}

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println(elapsedTime);
		
		assertTrue(elapsedTime < 400);
	}
	
	@Test
	public void testBoardEval()
	{
		Board board = new Board();
		
		assertTrue(SnozamaHeuristic.evaluateBoard(board, Board.WHITE, 0) == 0);
		System.out.println("Start: "+SnozamaHeuristic.evaluateBoard(board, Board.WHITE, 0));
		
		assertTrue(board.moveAmazon(9, 6, 1, 6, Board.WHITE));
		assertTrue(board.placeArrow(1, 6, 3, 8));
		
		System.out.println("Turn 1 (MSP): "+SnozamaHeuristic.MSP(board, Board.WHITE));
		System.out.println("Turn 1 (min-mobility): "+SnozamaHeuristic.minMobility(board, Board.WHITE));
		System.out.println("Turn 1: "+SnozamaHeuristic.evaluateBoard(board, Board.WHITE, 1));
		
		assertTrue(board.moveAmazon(0, 6, 5, 1, Board.BLACK));
		assertTrue(board.placeArrow(5, 1, 6, 1));
		
		System.out.println("Turn 1 (MSP): "+SnozamaHeuristic.MSP(board, Board.WHITE));
		System.out.println("Turn 1 (min-mobility): "+SnozamaHeuristic.minMobility(board, Board.WHITE));
		System.out.println("Turn 1: "+SnozamaHeuristic.evaluateBoard(board, Board.WHITE, 1));
		
		assertTrue(board.moveAmazon(9, 3, 5, 3, Board.WHITE));
		assertTrue(board.placeArrow(5, 3, 3, 1));
		
		System.out.println("Turn 2 (MSP): "+SnozamaHeuristic.MSP(board, Board.WHITE));
		System.out.println("Turn 2 (min-mobility): "+SnozamaHeuristic.minMobility(board, Board.WHITE));
		System.out.println("Turn 2: "+SnozamaHeuristic.evaluateBoard(board, Board.WHITE, 2));
		
		assertTrue(board.moveAmazon(5, 1, 6, 2, Board.BLACK));
		assertTrue(board.placeArrow(6, 2, 6, 8));
		
		System.out.println("Turn 2 (MSP): "+SnozamaHeuristic.MSP(board, Board.WHITE));
		System.out.println("Turn 2 (min-mobility): "+SnozamaHeuristic.minMobility(board, Board.WHITE));
		System.out.println("Turn 2: "+SnozamaHeuristic.evaluateBoard(board, Board.WHITE, 2));
	}
}
