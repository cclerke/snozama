package snozama.runtime.tests.mechanics;

import static org.junit.Assert.*;

import org.junit.Test;

import snozama.amazons.mechanics.Board;

/**
 * Unit tests for snozama.amazons.mechanics.Board.java
 * @author gdouglas
 *
 */
public class BoardTest {

	/**
	 * Test that board setup correctly.
	 */
	@Test
	public void testCreateNewBoard()
	{
		Board board = new Board();
		
		assertTrue(board.isWhite(6, 0));
		assertTrue(board.isWhite(9, 3));
		assertTrue(board.isWhite(9, 6));
		assertTrue(board.isWhite(6, 9));
		
		assertTrue(board.isBlack(3, 0));
		assertTrue(board.isBlack(0, 3));
		assertTrue(board.isBlack(0, 6));
		assertTrue(board.isBlack(3, 9));
		
		// TODO: Maybe test everything else is empty?
	}
	
	/**
	 * Test valid move checking function.
	 */
	@Test
	public void testValidMoves()
	{
		Board board = new Board();
		
		assertTrue(board.isValidMove(1, 1, 2, 2));
		
		assertTrue(board.isValidMove(2, 2, 1, 1));
		
		assertTrue(board.isValidMove(4, 4, 1, 1));
		
		assertTrue(board.isValidMove(2, 5, 5, 5));
		
		assertTrue(board.isValidMove(6, 1, 6, 2));
		
		assertTrue(board.isValidMove(0, 0, 9, 9));
		
		assertTrue(board.isValidMove(9, 0, 0, 9));
		
		assertTrue(board.isValidMove(0, 9, 9, 0));
		
		assertTrue(board.isValidMove(2, 1, 2, 3));
		
		// TODO: Add some tests 
	}
	
	/**
	 * Test invalid moves are invalid.
	 */
	@Test
	public void testInvalidMoves()
	{
		Board board = new Board();
		
		assertFalse(board.isValidMove(1, 1, 2, 3));
		
		assertFalse(board.isValidMove(2, 1, 1, 3));
		
		// Add an arrow at (5, 5)
		assertTrue(board.placeArrow(1, 1, 5, 5));
		
		assertFalse(board.isValidMove(1, 1, 5, 5));
		
		assertFalse(board.isValidMove(1, 1, 6, 6));
		
		assertFalse(board.isValidMove(1, 1, 7, 6));
		
		// TODO: Test for amazons.
	}
}
