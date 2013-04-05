package snozama.ui.components;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLayeredPane;

/**
 * The movement layer portion of the UI.
 * 
 * @author Alex Yakovlev
 *
 */
public class MovementLayer extends JLayeredPane
{
	/**
	 * Default serialized UID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The  list of movements.
	 */
	private static List<Line> movements;
	
	/**
	 * Which historic move we are currently at.
	 */
	private static int historicMove;
	
	/**
	 * Default constructor.
	 */
	public MovementLayer()
	{
		super();
		setVisible(Boolean.FALSE);
		movements = new ArrayList<Line>();
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		if (movements != null)
		{
			for (int i = 0; i < Math.min(historicMove * 2, movements.size()); i++)
			{
				if (i == Math.min(historicMove * 2, movements.size()) / 2)
				{
					movements.get(i).setOpacity(100);
				} else
				{
					movements.get(i).setOpacity(Line.OPACITY);
				}
				movements.get(i).paint(getComponentGraphics(g));
			}
		}
	}

	/**
	 * Add a movement to the layer.
	 * 
	 * @param movement		The movement to add.
	 */
	public void addMovement(Line movement)
	{
		movements.add(movement);
	}
	
	/**
	 * Set the movement layer to a predefined list of movements.
	 * 
	 * @param movements		The list of movements to use for this movement
	 * 						layer.
	 */
	public void setMovements(List<Line> movements)
	{
		MovementLayer.movements = movements;
	}
	
	/**
	 * Clear all of the movements.
	 */
	public void resetMovements()
	{
		MovementLayer.movements = new ArrayList<Line>();
	}
	
	/**
	 * Set the historic move indicator.
	 * 
	 * @param historicMove		The value to set.
	 */
	public void setHistoricMove(int historicMove)
	{
		MovementLayer.historicMove = historicMove;
	}

}
