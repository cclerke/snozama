package snozama.runtime.tests.mechanics;

/**
 * NegaScout tests
 */

import org.junit.Test;

import snozama.amazons.mechanics.Board;
import snozama.amazons.mechanics.MoveChoice;
import snozama.amazons.mechanics.algo.NegaScout;
import snozama.amazons.settings.Settings;

public class NegaScoutSearchTest {
	
	@Test
	public void testNegaScout()
	{
		NegaScout search = new NegaScout(System.currentTimeMillis() + 30*1000 - 200);
		Board board = new Board();
		int maxDepth = 2;
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		int colour = Settings.teamColour;
		int turn = 1;
		
		int score = search.NegaScoutSearch(board, maxDepth, alpha, beta, colour, turn);
		
		System.out.println("Score: "+score);
		System.out.println("Nodes: "+search.nodes);
			int mColour = ((search.bestMoves[maxDepth] & (0xf << (4*0))) >> (4*0));
			int mAmazon = ((search.bestMoves[maxDepth] & (0xf << (4*1))) >> (4*1));
			int mRowF = ((search.bestMoves[maxDepth] & (0xf << (4*2))) >> (4*2));
			int mColF = ((search.bestMoves[maxDepth] & (0xf << (4*3))) >> (4*3));
			int mRowA = ((search.bestMoves[maxDepth] & (0xf << (4*4))) >> (4*4));
			int mColA = ((search.bestMoves[maxDepth] & (0xf << (4*5))) >> (4*5));
			System.out.println("Move: "+search.bestMoves[maxDepth]+" = "+mColour+" "+mAmazon+" "+" "+ mRowF+mColF+" "+mRowA+mColA);
	}
	/*
	@Test
	public void testChooseMove()
	{
		NegaScout search = new NegaScout(System.currentTimeMillis() + 30*1000 - 200);
		
		Board board = new Board();
		
		MoveChoice move = search.chooseMove(board, Board.WHITE, 1, 1);
		System.out.println(move);
	}*/
}
