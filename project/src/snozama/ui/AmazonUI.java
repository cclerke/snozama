package snozama.ui;

import snozama.amazons.mechanics.Board;

public class AmazonUI extends AbstractAmazonUI
{
	private Board board;
	
	public AmazonUI( Board board )
	{
		this.board = board;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}
}
