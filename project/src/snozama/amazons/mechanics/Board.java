package snozama.amazons.mechanics;

public class Board {

	//board and player/arrow idea: byte is the smallest type to store this (that I'm aware of)
	/*
	 * Starting setup
	 *  -WHITE at [6][0], [9][3], [9][6], [6][9]
	 *  -BLACK at [3][0], [0][3], [0][6], [3][9]
	 *  -the rest will be empty
	 */
	byte board[][] = new byte[10][10];
	public static byte EMPTY = 0;
	public static byte WHITE = 1;
	public static byte BLACK = 2;
	public static byte ARROW = 3;
}
