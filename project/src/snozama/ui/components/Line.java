package snozama.ui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import snozama.amazons.mechanics.Board;

/**
 * Graphical line used for movement tracking.
 * 
 * @author Alex Yakovlev
 * 
 */
public class Line
{
	private int x1;
	private int x2;
	private int y1;
	private int y2;
	private Color color;
	private boolean arrow;
	private int whoseTurn;

	public static final int OPACITY = 80;

	private int opacity = OPACITY;

	/**
	 * Default constructor.
	 * 
	 * @param x1			First x-coordinate.
	 * @param y1			First y-coordinate.
	 * @param x2			Second x-coordinate.
	 * @param y2			Second y-coordinate.
	 * @param whoseTurn		Either {@code Board.BLACK} or {@code Board.WHITE}.
	 * @param arrow			{@code true} if this is an arrow, {@code false}
	 * 						otherwise.
	 */
	public Line(int x1, int y1, int x2, int y2, int whoseTurn, boolean arrow)
	{
		this.x1 = arrow ? x1 - 5 : x1;
		this.x2 = arrow ? x2 - 5 : x2;
		this.y1 = arrow ? y1 - 5 : y1;
		this.y2 = arrow ? y2 - 5 : y2;
		this.arrow = arrow;

		this.whoseTurn = whoseTurn;

		setColor();
	}

	/**
	 * Apply line to UI.
	 * 
	 * @param g			The graphics context object to apply line to.
	 */
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
		if (arrow)
		{
			g2.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_BEVEL, 0, new float[]
					{ 9 }, 0));
		} else
		{
			g2.setStroke(new BasicStroke(5));
		}

		g2.draw(new Line2D.Float(x1, y1, x2, y2));

	}

	/**
	 * Set opacity of line.
	 * 
	 * @param opacity	The opacity level.
	 */
	public void setOpacity(int opacity)
	{
		this.opacity = opacity;
		setColor();
	}

	/**
	 * Set the colour of the line based on whose turn it is.
	 */
	private void setColor()
	{
		this.color = whoseTurn == Board.WHITE ? new Color(255, 255, 255,
				opacity) : new Color(0, 0, 0, opacity);
	}

}
