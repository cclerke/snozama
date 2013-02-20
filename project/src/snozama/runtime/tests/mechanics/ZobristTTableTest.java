package snozama.runtime.tests.mechanics;

import static org.junit.Assert.*;

import org.junit.Test;

import snozama.amazons.mechanics.Board;
import snozama.amazons.mechanics.MoveManager;
import snozama.amazons.mechanics.SnozamaHeuristic;
import snozama.amazons.mechanics.transtable.ZobristTTable;

public class ZobristTTableTest {

	@Test
	public void testNew() {
		ZobristTTable table = new ZobristTTable(1);
		assertEquals(table.size, 1);
		
		table = new ZobristTTable(1000);
		assertEquals(table.size, 1000);
	}
	
	@Test
	public void testPutGetBasic() {
		ZobristTTable table = new ZobristTTable(2500000);
		
		Board board = new Board();
		
		// Totally fake data.
		table.put(board, 10, 0, -100, -10, 233);
		
		// Get the data.
		int[] record = table.get(board);
		
		// Test data.
		assertEquals(record[ZobristTTable.POS_INFO], 10);
		assertEquals(record[ZobristTTable.DEPTH], 0);
		assertEquals(record[ZobristTTable.LOWER], -100);
		assertEquals(record[ZobristTTable.UPPER], -10);
		assertEquals(record[ZobristTTable.MOVE], 233);
		
		record = null;
		
		record = table.get(table.computeBoardHash(board));
		assertEquals(record[ZobristTTable.POS_INFO], 10);
		assertEquals(record[ZobristTTable.DEPTH], 0);
		assertEquals(record[ZobristTTable.LOWER], -100);
		assertEquals(record[ZobristTTable.UPPER], -10);
		assertEquals(record[ZobristTTable.MOVE], 233);
	}
	
	// Note, get cannot be tested well, because there might be a collision we cannot predict.
	@Test
	public void testPutMany()
	{
		ZobristTTable table = new ZobristTTable(2500000);
		
		Board board = new Board();
		MoveManager successors = board.getSuccessors(Board.WHITE);
		int next;
		int score;
		int row_s;
		int col_s;
		
		while (successors.hasIterations())
		{
			next = successors.nextIterableIndex();
			row_s = Board.decodeAmazonRow(board.amazons[Board.WHITE][successors.getAmazonIndex(next)]);
			col_s = Board.decodeAmazonColumn(board.amazons[Board.WHITE][successors.getAmazonIndex(next)]);
			successors.applyMove(board, next);
			
			score = SnozamaHeuristic.evaluateBoard(board, Board.WHITE, 1);
			
			// Some fakery here.
			table.put(board, score, 1, -100, 100, successors.getMove(next));
			
			successors.undoMove(board, next, row_s, col_s);
		}
		
		System.out.printf("Total number of collisions: %d\n", table.collisions);
	}
	
	// Note, get cannot be tested well, because there might be a collision we cannot predict.
	@Test
	public void testPutManyUpdateKey()
	{
		ZobristTTable table = new ZobristTTable(2500000);

		Board board = new Board();
		int key = table.computeBoardHash(board);
		int baseKey = key;
		MoveManager successors = board.getSuccessors(Board.WHITE);
		int next;
		int score;
		int row_s;
		int col_s;

		while (successors.hasIterations())
		{
			next = successors.nextIterableIndex();
			row_s = Board.decodeAmazonRow(board.amazons[Board.WHITE][successors.getAmazonIndex(next)]);
			col_s = Board.decodeAmazonColumn(board.amazons[Board.WHITE][successors.getAmazonIndex(next)]);
			successors.applyMove(board, next);
			
			// Make move to key.
			key = table.updateHashKeyByMove(key, ZobristTTable.WHITE_AMAZON,
					row_s, col_s, successors.getFinishRow(next),
					successors.getFinishColumn(next),
					successors.getArrowRow(next),
					successors.getArrowColumn(next));
			
			score = SnozamaHeuristic.evaluateBoard(board, Board.WHITE, 1);

			// Some fakery here.
			table.put(key, score, 1, -100, 100, successors.getMove(next));

			successors.undoMove(board, next, row_s, col_s);
			
			// Undo move to key
			key = table.updateHashKeyByMove(key, ZobristTTable.WHITE_AMAZON,
					row_s, col_s, successors.getFinishRow(next),
					successors.getFinishColumn(next),
					successors.getArrowRow(next),
					successors.getArrowColumn(next));
			
			assertEquals(key, baseKey);
		}

		System.out.printf("Total number of collisions: %d\n", table.collisions);
	}
}
