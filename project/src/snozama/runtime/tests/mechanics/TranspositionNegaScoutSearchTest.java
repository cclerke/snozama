package snozama.runtime.tests.mechanics;

/**
 * NegaScout tests
 */

import org.junit.Test;

import snozama.amazons.mechanics.Board;
import snozama.amazons.mechanics.MoveChoice;
import snozama.amazons.mechanics.algo.TranspositionNegaScout;
import snozama.amazons.settings.Settings;

public class TranspositionNegaScoutSearchTest {
	
	@Test
	public void testNegaScout()
	{
		System.out.flush();
		Board board = new Board();
		
		System.out.println("DUNUNUNUNU:");
		System.out.flush();
		for (int i = 0; i < board.amazons.length; i++)
		{
			for (int j = 0; j < board.amazons[i].length; j++)
			{
				byte pos = board.amazons[i][j];
				int row = Board.decodeAmazonRow(pos);
				int col = Board.decodeAmazonColumn(pos);
				
				System.out.println("(" + row + ", " + col + ")");
				System.out.flush();
			}
		}
		
		TranspositionNegaScout search = new TranspositionNegaScout(System.currentTimeMillis() + 25*1000, 2000000, board);
		
		int colour = Settings.teamColour;
		int turn = 1;
		
		int move = search.chooseMove(board, colour, turn);
		
		System.out.println("Reached depth: "+search.depthCompleted);
		System.out.println("Nodes: "+search.nodes);
			int mColour = ((move & (0xf << (4*0))) >> (4*0));
			int mAmazon = ((move & (0xf << (4*1))) >> (4*1));
			int mRowF = ((move & (0xf << (4*2))) >> (4*2));
			int mColF = ((move & (0xf << (4*3))) >> (4*3));
			int mRowA = ((move & (0xf << (4*4))) >> (4*4));
			int mColA = ((move & (0xf << (4*5))) >> (4*5));
			System.out.println("Move: "+move+" = "+mColour+" "+mAmazon+" "+" "+ mRowF+mColF+" "+mRowA+mColA);
		
		MoveChoice mc = new MoveChoice(move, board);
		
		for (int i = 0; i < board.amazons.length; i++)
		{
			for (int j = 0; j < board.amazons[i].length; j++)
			{
				byte pos = board.amazons[i][j];
				int row = Board.decodeAmazonRow(pos);
				int col = Board.decodeAmazonColumn(pos);
				
				System.out.println("(" + row + ", " + col + ")");
				System.out.flush();
			}
		}
		System.out.println(mc);
	}
}
