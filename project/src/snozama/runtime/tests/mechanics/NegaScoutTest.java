package snozama.runtime.tests.mechanics;

import static org.junit.Assert.*;

/**
 * NegaScout tests
 */

import org.junit.Test;

import snozama.amazons.mechanics.Board;
import snozama.amazons.mechanics.MoveManager;
import snozama.amazons.mechanics.algo.NegaScoutSearch;

public class NegaScoutTest {
	
	@Test
	public void testChooseMoveWithNegaScout()
	{
		Board board = new Board();
		MoveManager manager = board.getSuccessors(Board.WHITE);

		int index = NegaScoutSearch.chooseMove(board, Board.WHITE, 0, null);
		
		int row_s = Board.decodeAmazonRow(board.amazons[Board.WHITE][manager.getAmazonIndex(index)]);
		int col_s = Board.decodeAmazonColumn(board.amazons[Board.WHITE][manager.getAmazonIndex(index)]);
		
		int row_f = manager.getFinishRow(index);
		int col_f = manager.getFinishColumn(index);
		int arrow_row = manager.getArrowRow(index);
		int arrow_col = manager.getArrowColumn(index);
		
		System.out.println(index);
		System.out.println(row_s +","+col_s+" - "+row_f+","+col_f+" ("+arrow_row+","+arrow_col+")");
	}
}
