package snozama.ui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;

import snozama.amazons.mechanics.Board;

public class Line
{
	private int x1;
	private int x2;
	private int y1;
	private int y2;
	private Color color;
	private boolean arrow;
	
	private final int OPACITY = 80;
	
	public Line(int x1, int y1, int x2, int y2, int color, boolean arrow)
	{
		this.x1 = arrow ? x1 - 5 : x1;
		this.x2 = arrow ? x2 - 5 : x2;
		this.y1 = arrow ? y1 - 5 : y1;
		this.y2 = arrow ? y2 - 5 : y2;
		this.arrow = arrow;
		
		this.color = color == Board.WHITE ? new Color( 255, 255, 255, OPACITY ) : new Color(0,0,0, OPACITY);
	}
	
	public void paint( Graphics g )
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor( color );
		if( arrow )
		{
			g2.setStroke( new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
		}
		else
		{
			g2.setStroke( new BasicStroke( 5 ) );
		}
		
		g2.draw( new Line2D.Float(x1,y1,x2,y2) );
		
	}
	
}
