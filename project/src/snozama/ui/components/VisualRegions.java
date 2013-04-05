package snozama.ui.components;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLayeredPane;

import snozama.amazons.mechanics.Board;
import snozama.amazons.mechanics.Regions;
import snozama.ui.AmazonUI;

/**
 * Visualization of "owned regions".
 * 
 * @author Alex Yakovlev
 * 
 */
public class VisualRegions extends JLayeredPane
{
	/**
	 * Default serialized UID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default opacity level.
	 */
	public static final int OPACITY = 200;
	
	/**
	 * Possible region colours.  We define twelve.
	 */
	public static final Color ZERO = new Color(0, 0, 0, OPACITY);
	public static final Color ONE = new Color(0, 0, 128, OPACITY);
	public static final Color TWO = new Color(0, 0, 255, OPACITY);
	public static final Color THREE = new Color(0, 128, 0, OPACITY);
	public static final Color FOUR = new Color(0, 128, 128, OPACITY);
	public static final Color FIVE = new Color(0, 128, 255, OPACITY);
	public static final Color SIX = new Color(0, 255, 0, OPACITY);
	public static final Color SEVEN = new Color(0, 255, 128, OPACITY);
	public static final Color EIGHT = new Color(0, 255, 255, OPACITY);
	public static final Color NINE = new Color(128, 0, 0, OPACITY);
	public static final Color TEN = new Color(128, 0, 128, OPACITY);
	public static final Color ELEVEN = new Color(128, 0, 255, OPACITY);
	public static final Color TWELVE = new Color(128, 128, 0, OPACITY);

	/**
	 * Make array of possible colours.
	 */
	public static final Color[] COLORS = { ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, ELEVEN, TWELVE };
	public static byte[][] regions = new byte[Board.SIZE][Board.SIZE];
	
	/**
	 * Default constructor.
	 */
	public VisualRegions()
	{
		super();
		setVisible(Boolean.FALSE);
		regions = Regions.initRegions();
	}
	
	/**
	 * Set visual regions to a instance.
	 * 
	 * @param regions		Instance to set to.
	 */
	public void setRegions(byte[][] regions)
	{
		VisualRegions.regions = regions;
	}
	
	/**
	 * Apply regions to the user interface.
	 * 
	 * @param g				The graphics context object to apply regions to.
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		for (int i = 0; i < Board.SIZE; i++)
		{
			for (int j = 0; j < Board.SIZE; j++)
			{
				if (regions[i][j] != -1)
				{
					g.setColor(COLORS[regions[i][j] % COLORS.length]);
					g.fillRect(j * AmazonUI.SQUARE_WIDTH, i
							* AmazonUI.SQUARE_WIDTH, AmazonUI.SQUARE_WIDTH,
							AmazonUI.SQUARE_WIDTH);
				}
			}
		}
	}

}
