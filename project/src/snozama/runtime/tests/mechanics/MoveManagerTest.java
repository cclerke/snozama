package snozama.runtime.tests.mechanics;

import static org.junit.Assert.*;

import org.junit.Test;

import snozama.amazons.mechanics.Board;
import snozama.amazons.mechanics.MoveManager;

public class MoveManagerTest {

	@Test
	public void testNew()
	{
		MoveManager moves = new MoveManager();
		
		assertTrue(moves.size() == 0);
		assertTrue(moves.maxSize() == 2176);
	}
	
	@Test
	public void testAddBasics()
	{
		MoveManager moves = new MoveManager();
		
		moves.add(Board.BLACK, 0, 0, 0, 1, 1);
		moves.add(Board.BLACK, 3, 1, 1, 2, 2);
		moves.add(Board.WHITE, 1, 2, 2, 2, 6);
		
		// Test first move is correct.
		assertTrue(moves.hasIterations());
		int index = moves.nextIterableIndex();
		assertEquals(index, 0);
		assertEquals(moves.getColour(index), Board.BLACK);
		assertEquals(moves.getAmazonIndex(index), 0);
		assertEquals(moves.getFinishRow(index), 0);
		assertEquals(moves.getFinishColumn(index), 0);
		assertEquals(moves.getArrowRow(index), 1);
		assertEquals(moves.getArrowColumn(index), 1);
		
		// Test second move is correct.
		assertTrue(moves.hasIterations());
		index = moves.nextIterableIndex();
		assertEquals(index, 1);
		assertEquals(moves.getColour(index), Board.BLACK);
		assertEquals(moves.getAmazonIndex(index), 3);
		assertEquals(moves.getFinishRow(index), 1);
		assertEquals(moves.getFinishColumn(index), 1);
		assertEquals(moves.getArrowRow(index), 2);
		assertEquals(moves.getArrowColumn(index), 2);
		
		// Test third move is correct.
		assertTrue(moves.hasIterations());
		index = moves.nextIterableIndex();
		assertEquals(index, 2);
		assertEquals(moves.getColour(index), Board.WHITE);
		assertEquals(moves.getAmazonIndex(index), 1);
		assertEquals(moves.getFinishRow(index), 2);
		assertEquals(moves.getFinishColumn(index), 2);
		assertEquals(moves.getArrowRow(index), 2);
		assertEquals(moves.getArrowColumn(index), 6);
		
		assertFalse(moves.hasIterations());
		
		moves.condense();
		assertTrue(moves.size() == 3);
		assertTrue(moves.maxSize() == 3);
		moves.clearIteratorState();
		
		// Test first move is correct.
		assertTrue(moves.hasIterations());
		index = moves.nextIterableIndex();
		assertEquals(index, 0);
		assertEquals(moves.getColour(index), Board.BLACK);
		assertEquals(moves.getAmazonIndex(index), 0);
		assertEquals(moves.getFinishRow(index), 0);
		assertEquals(moves.getFinishColumn(index), 0);
		assertEquals(moves.getArrowRow(index), 1);
		assertEquals(moves.getArrowColumn(index), 1);

		// Test second move is correct.
		assertTrue(moves.hasIterations());
		index = moves.nextIterableIndex();
		assertEquals(index, 1);
		assertEquals(moves.getColour(index), Board.BLACK);
		assertEquals(moves.getAmazonIndex(index), 3);
		assertEquals(moves.getFinishRow(index), 1);
		assertEquals(moves.getFinishColumn(index), 1);
		assertEquals(moves.getArrowRow(index), 2);
		assertEquals(moves.getArrowColumn(index), 2);

		// Test third move is correct.
		assertTrue(moves.hasIterations());
		index = moves.nextIterableIndex();
		assertEquals(index, 2);
		assertEquals(moves.getColour(index), Board.WHITE);
		assertEquals(moves.getAmazonIndex(index), 1);
		assertEquals(moves.getFinishRow(index), 2);
		assertEquals(moves.getFinishColumn(index), 2);
		assertEquals(moves.getArrowRow(index), 2);
		assertEquals(moves.getArrowColumn(index), 6);
	}

}
