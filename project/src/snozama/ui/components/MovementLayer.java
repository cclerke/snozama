package snozama.ui.components;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLayeredPane;

public class MovementLayer extends JLayeredPane
{
	private static List<Line> movements;
	private static int historicMove;
	
	public MovementLayer()
	{
		super();
		setVisible( Boolean.FALSE );
		movements = new ArrayList<Line>();
	}
	
	@Override
	public void paintComponent( Graphics g )
	{
		super.paintComponent( g );
		
		if( movements != null )
		{
			System.out.println( historicMove );
			for( int i = 0; i < Math.min( historicMove * 2, movements.size());i++ )
			{
				movements.get(i).paint( getComponentGraphics( g ) );
			}
		}
	}
	
	public void addMovement( Line movement )
	{
		movements.add( movement );
	}
	
	public void setMovements( List<Line> movements )
	{
		MovementLayer.movements = movements;
	}
	
	public void setHistoricMove( int historicMove )
	{
		MovementLayer.historicMove = historicMove;
	}
	
}