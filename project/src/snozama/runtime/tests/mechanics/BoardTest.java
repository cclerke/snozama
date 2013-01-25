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
	public void testValidMove()
	{
		Board board = new Board();
		
		assertTrue(board.isValidMove(1, 1, 2, 2));
		
		assertTrue(board.isValidMove(2, 2, 1, 1));
		
		assertTrue(board.isValidMove(4, 4, 1, 1));
		
		assertTrue(board.isValidMove(2, 5, 5, 5));
		
		assertTrue(board.isValidMove(6, 1, 6, 2));
		
		// TODO: Many more.  Including negative case.
	}
}
