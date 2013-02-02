package snozama.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import snozama.amazons.mechanics.Board;

/**
 * 
 * @author Alex Yakovlev
 * 
 * Main UI For the Amazons Game
 * 
 * TODO: have to go move by move, instead of board by board.
 *
 */

public class AmazonUI extends AbstractAmazonUI
{
	/**
	 * Inherited Serial UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Main Window Width
	 */
	private static final int WINDOW_WIDTH = 1192;
	
	/**
	 * Main Window Height
	 */
	private static final int WINDOW_HEIGHT = 600;
	
	/**
	 * Main Images used in the game
	 */
	private static BufferedImage boardImage;
	private static BufferedImage whiteQueenImage;
	private static BufferedImage blackQueenImage;
	private static BufferedImage wallImage;
	
	/**
	 * Amazon Game Board passed in
	 */
	private Board board;
	
	/**
	 * Width of a square on the board in pixels;
	 */
	private static final int SQUARE_WIDTH = 50;
	private static final int X_OFFSET = 20;
	private static final int Y_OFFSET = 20;
	
	private static JLayeredPane pane;
	private static JPanel panel;
	private static JLabel boardPanel;
	private static JLayeredPane gameLayer;
	
	public AmazonUI( Board board )
	{
		this.board = board;
		
		setImages();
		
	    initWindowSettings();
	    createMainPanel();
	    createMenuBar();		
	}
	
	private void createMainPanel()
	{
		pane = new JLayeredPane();
		pane.setBounds(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);
		
		panel = new JPanel( null );
		pane.add(panel, new Integer(20));
		
		panel.setBounds(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);
		panel.setBackground(new Color(66,66,66));
		
		
		gameLayer = new JLayeredPane();
		gameLayer.setBounds(X_OFFSET,Y_OFFSET,500,500);
		
		pane.add(gameLayer, new Integer( 50 ));
		
		if( boardImage != null )
		{
			boardPanel = new JLabel( new ImageIcon( boardImage ));
			boardPanel.setBounds(X_OFFSET,Y_OFFSET,500,500);
			panel.add(boardPanel,new Integer(5));
		}
		
		readBoard();
		
		add(pane);
		
	}
	
	/**
	 * Reads the current board and places the Amazons and arrows appropriately.
	 */
	private void readBoard()
	{
		
		if( blackQueenImage != null && whiteQueenImage != null )
		{
			for( int i = 0; i < 10; i ++ )
			{
				for( int j = 0; j < 10; j++ )
				{
					if(board.isBlack(i,j))
					{
						JLabel blackImage = new JLabel( new ImageIcon( blackQueenImage));
						setPieceLocation( blackImage, i, j );
						gameLayer.add(blackImage);
					}
					if(board.isWhite(i, j))
					{
						JLabel whiteImage = new JLabel( new ImageIcon( whiteQueenImage));
						setPieceLocation( whiteImage, i, j );
						gameLayer.add(whiteImage);
					}
					if(board.isArrow(i, j))
					{
						JLabel arrowImage = new JLabel( new ImageIcon( wallImage));
						setPieceLocation(arrowImage, i, j);
						gameLayer.add(arrowImage);
					}
				}
			}
		}
	}
	
	/**
	 * Set the location of a piece based on the current row and column
	 * that it is in.
	 * @param piece - JLabel piece that contains the image
	 * @param row - int row position in byte array from Board
	 * @param col - int col position in byte array from Board
	 */
	private void setPieceLocation( JLabel piece, int row, int col )
	{
		piece.setBounds(col*SQUARE_WIDTH + 5, row*SQUARE_WIDTH + 5, 40, 40 );
	}
	
	/**
	 * Set up the main menu bar with possible options.
	 */
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
	
	/**
	 * Window settings for Game Window
	 */
	private void initWindowSettings()
	{
		setTitle("Amazons");
	    setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
	    setLocationRelativeTo(null);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/**
	 * @visibility private
	 * 
	 * @ Read and set up the images used in the game
	 */
	private void setImages()
	{
		boardImage = null;
		whiteQueenImage = null;
		blackQueenImage = null;
		wallImage = null;
		
		try
		{
			 boardImage = ImageIO.read( new File("src/snozama/ui/board.png" ));
		}
		catch(IOException ioe)
		{
			System.out.println("Error reading board.png");
		}
		try
		{
			 whiteQueenImage = ImageIO.read( new File("src/snozama/ui/white_queen.png" ));
		}
		catch(IOException ioe)
		{
			System.out.println("Error reading white_queen.png");
		}
		try
		{
			 blackQueenImage = ImageIO.read( new File("src/snozama/ui/black_queen.png" ));
		}
		catch(IOException ioe)
		{
			System.out.println("Error reading black_queen.png");
		}
		try
		{
			 wallImage = ImageIO.read( new File("src/snozama/ui/wall.png" ));
		}
		catch(IOException ioe)
		{
			System.out.println("Error reading wall.png");
		}
	}

}
