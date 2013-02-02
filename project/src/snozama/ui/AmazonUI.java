package snozama.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import snozama.amazons.mechanics.Board;

/**
 * 
 * @author Alex Yakovlev
 *
 */

public class AmazonUI extends AbstractAmazonUI
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int WINDOW_WIDTH = 1192;
	private static final int WINDOW_HEIGHT = 600;
	
	private Board board;
	
	public AmazonUI( Board board )
	{
		this.board = board;
	    initWindowSettings();
	    createMainPanel();
	    createMainBoard();
	    createMenuBar();
		
		
	}
	
	private void initWindowSettings()
	{
		setTitle("Amazons");
	    setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
	    setLocationRelativeTo(null);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void createMainPanel()
	{
		JPanel panel = new JPanel( null );
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));
		getContentPane().add(panel);
		
		panel.setBackground(new Color(66,66,66));
		JPanel game = new JPanel();
		game.setMinimumSize( new Dimension(WINDOW_WIDTH,200));
		game.setLayout( new BoxLayout(game, BoxLayout.Y_AXIS) );
		game.setBackground(new Color(121,121,121));
		game.add(new JLabel("ALEX"));

		panel.add(game);
		
		/*
		JButton quit = new JButton("Quit");
		quit.setBounds(50,60,80,30);
		quit.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent event)
			{
				System.exit(0);
			}
		});
		
		panel.add(quit);
		*/
		
	}
	
	private void createMenuBar()
	{
		JMenuBar menu = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem quit = new JMenuItem("Quit");
		quit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				System.exit(0);
			}
		} );
		file.add(quit);
		menu.add(file);
		
		setJMenuBar(menu);
	}
	
	private void createMainBoard()
	{
		
	}

	protected Board getBoard() {
		return board;
	}

	protected void setBoard(Board board) {
		this.board = board;
	}
	
	public void doSomething()
	{
		getContentPane().getComponent(0).setBackground(new Color(4,4,4));
	}
}
