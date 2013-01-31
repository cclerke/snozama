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
	
	@Test
	public void testBoardEval()
	{
		Board board = new Board();
		SnozamaHeuristic heuristic = new SnozamaHeuristic();
		
		assertTrue(heuristic.evaluateBoard(board, Board.WHITE, 0) == 0);
		System.out.println("Start: "+heuristic.evaluateBoard(board, Board.WHITE, 0));
		
		assertTrue(board.moveAmazon(9, 6, 1, 6, Board.WHITE));
		assertTrue(board.placeArrow(1, 6, 3, 8));
		
		System.out.println("Turn 1 (MSP): "+heuristic.MSP(board, Board.WHITE));
		System.out.println("Turn 1 (min-mobility): "+heuristic.minMobility(board, Board.WHITE));
		System.out.println("Turn 1: "+heuristic.evaluateBoard(board, Board.WHITE, 1));
		
		assertTrue(board.moveAmazon(0, 6, 5, 1, Board.BLACK));
		assertTrue(board.placeArrow(5, 1, 6, 1));
		
		System.out.println("Turn 1 (MSP): "+heuristic.MSP(board, Board.WHITE));
		System.out.println("Turn 1 (min-mobility): "+heuristic.minMobility(board, Board.WHITE));
		System.out.println("Turn 1: "+heuristic.evaluateBoard(board, Board.WHITE, 1));
		
		assertTrue(board.moveAmazon(9, 3, 5, 3, Board.WHITE));
		assertTrue(board.placeArrow(5, 3, 3, 1));
		
		System.out.println("Turn 2 (MSP): "+heuristic.MSP(board, Board.WHITE));
		System.out.println("Turn 2 (min-mobility): "+heuristic.minMobility(board, Board.WHITE));
		System.out.println("Turn 2: "+heuristic.evaluateBoard(board, Board.WHITE, 2));
		
		assertTrue(board.moveAmazon(5, 1, 6, 2, Board.BLACK));
		assertTrue(board.placeArrow(6, 2, 6, 8));
		
		System.out.println("Turn 2 (MSP): "+heuristic.MSP(board, Board.WHITE));
		System.out.println("Turn 2 (min-mobility): "+heuristic.minMobility(board, Board.WHITE));
		System.out.println("Turn 2: "+heuristic.evaluateBoard(board, Board.WHITE, 2));
	}
}
