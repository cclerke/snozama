package snozama.runtime.tests.mechanics;

/**
 * NegaScout tests
 */

import org.junit.Test;

import snozama.amazons.mechanics.Board;
import snozama.amazons.mechanics.algo.NegaScout;
import snozama.amazons.settings.Settings;

public class NegaScoutSearchTest {
	
	@Test
	public void testNegaScout()
	{
		NegaScout search = new NegaScout(System.currentTimeMillis() + 25*1000);
		Board board = new Board();
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
	}
	
	//@Test
	public void testChooseMove()
	{
		NegaScout search = new NegaScout(System.currentTimeMillis() + 25*1000);
		
		Board board = new Board();
		
		int move = search.chooseMove(board, Board.WHITE, 1);
		System.out.println(move);
	}
}
