package snozama.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import snozama.amazons.mechanics.Board;
import snozama.ui.eventListeners.*;

/**
 * 
 * @author Alex Yakovlev
 * 
 * Main UI For the Amazons Game
 * 
 *
 */

public class AmazonUI extends AbstractAmazonUI
{
	
	/**
	 * Ready Functions to run when the UI is ready for them
	 */
	private static List<ReadyListener> readyFunctions = new ArrayList<ReadyListener>();
	
	/**
	 * History of moves for quick visualization
	 */
	private static List<String> moveHistory = new ArrayList<String>();
	private static int currentMove;
	private static int historicMove;
	
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
	private static final int WINDOW_HEIGHT = 620;
	
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
	
	private static String[][] labels = new String[2][10];
	
	/**
	 * Width of a square on the board in pixels;
	 */
	private static final int SQUARE_WIDTH = 50;
	private static final int X_OFFSET = 40;
	private static final int Y_OFFSET = 20;
	
	private static JLayeredPane pane;
	private static JPanel panel;
	private static JLabel boardPanel;
	private static JLayeredPane gameLayer;
	private static JPanel log;
	
	private AmazonUI()
	{
		this.board = new Board();
		initCommon();
	}
	
	private AmazonUI( Board board )
	{
		this.board = board;
		initCommon();
	}
	
	private void initCommon()
	{
		currentMove = 0;
		historicMove = 0;
		initWindowSettings();
		createMenuBar();
		
		setImages();
	    createMainPanel();
	    setUpLabels();
	    
	    createLogPanel();
	    
	    ready();
	}
	
	public static AmazonUI getInstance()
	{
		return new AmazonUI();
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
		
		JButton back = new JButton("Back");
		JButton forward = new JButton("Forward");
		
		back.addActionListener( new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				historyBack();
			}
			
		});
		
		forward.addActionListener( new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				historyForward();
			}
			
		});
		
		back.setBounds(550,50,100,50);
		forward.setBounds(670,50,100,50);
		
		panel.add(back);
		panel.add(forward);
		
		add(pane);
		
	}
	
	public void createLogPanel()
	{
		log = new JPanel();
		log.setLayout( new BoxLayout( log, BoxLayout.Y_AXIS) );
		log.setBounds(X_OFFSET + boardPanel.getWidth() + 20, 120, 400, 400);

		panel.add( log );
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
	
	public Boolean moveAmazon( int row_s, int col_s, int row_f, int col_f, int row_a, int col_a)
	{
		int whoseMove = board.isWhite( row_s, col_s ) ? Board.WHITE : Board.BLACK;
		
		if( historicMove == currentMove )
		{
			/* Move Amazon */
			movePiece(row_s, col_s, row_f, col_f);
			
			/* Place arrow */
			placeArrow(row_a, col_a);
			historicMove++;
		}
		
		/* Save move in history */
		String move = row_s + "-" + col_s + "-" + row_f + "-" + col_f + "-" + row_a + "-" + col_a;
		moveHistory.add(move);
		
		String message = whoseMove == Board.WHITE ? "White" : "Black";
		message += " moved from " + row_s + ", " + col_s + " to ";
		message += row_f + ", " + col_f + " and shot an arrow to " + row_a + ", " + col_a;
		log.add( new JLabel( message ) );
		
		board.move( row_s, col_s, row_f, col_f, row_a, col_a, whoseMove );
		
		currentMove++;
		
		// TODO: return based on whether move was successful.
		return true;
	}
	
	private void placeArrow( int row_a, int col_a )
	{
		JLabel arrowImage = new JLabel(new ImageIcon(wallImage));
		setPieceLocation(arrowImage, row_a, col_a);
		gameLayer.add(arrowImage);
	}
	
	private void movePiece( int row_s, int col_s, int row_f, int col_f)
	{
		setPieceLocation( getPiece( row_s, col_s), row_f, col_f );
	}
	
	public void historyBack()
	{
		
		if( historicMove > 0 )
		{
			String move = moveHistory.get( historicMove - 1 );
			String[] params = move.split("-");
			int col_s = Integer.parseInt(params[1]);
			int row_s = Integer.parseInt(params[0]);
			int col_f = Integer.parseInt(params[3]);
			int row_f = Integer.parseInt(params[2]);
			int col_a = Integer.parseInt(params[5]);
			int row_a = Integer.parseInt(params[4]);
			
			
			/*Remove arrow*/
			removePiece(row_a, col_a);
			
			/*Move the piece back */
			movePiece( row_f, col_f, row_s, col_s );
			
			historicMove--;
		}
	}
	
	public void historyForward()
	{
		if( historicMove < currentMove )
		{
			historicMove++;
			
			String move = moveHistory.get( historicMove - 1 );
			String[] params = move.split("-");
			int col_s = Integer.parseInt(params[1]);
			int row_s = Integer.parseInt(params[0]);
			int col_f = Integer.parseInt(params[3]);
			int row_f = Integer.parseInt(params[2]);
			int col_a = Integer.parseInt(params[5]);
			int row_a = Integer.parseInt(params[4]);
			
			movePiece(row_s, col_s, row_f, col_f);
			placeArrow(row_a, col_a);
		}
	}
	
	private JLabel getPiece( int row_s, int col_s )
	{
		int x = col_s * SQUARE_WIDTH + 5;
		int y = row_s * SQUARE_WIDTH + 5;
		return (JLabel) gameLayer.findComponentAt( x, y );
	}
	
	private void removePiece( int row_s, int col_s )
	{
		JLabel piece = getPiece(row_s, col_s);
		gameLayer.remove(piece);
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
	 * Window settings for Game Window
	 */
	private void initWindowSettings()
	{
		setTitle("Amazons");
	    setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
	    setLocationRelativeTo(null);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/*
	 * Base Labels
	 */
	private void setUpLabels()
	{
		labels = Labels.INVERTED_ZERO;
		
		int top = SQUARE_WIDTH * 10 + Y_OFFSET;
		int left = X_OFFSET + 20;
		
		Font font = new Font( "Verdana", 0, 14 );
		
		JLabel[] x_s =  new JLabel[10];
		for( int i = 0; i < x_s.length ; i ++ )
		{
			x_s[i] = new JLabel( labels[0][i] );
			x_s[i].setBounds( left, top, 30, 30 );
			x_s[i].setFont( font );
			x_s[i].setForeground( new Color( 240, 240, 240 ) );
			x_s[i].setHorizontalTextPosition( SwingConstants.CENTER );
			panel.add( x_s[i] );
			
			left+=SQUARE_WIDTH;
		}
		
		top = Y_OFFSET + 10;
		left = X_OFFSET - 25;
		
		JLabel[] y_s =  new JLabel[10];
		for( int i = 9; i >= 0 ; i -- )
		{
			y_s[i] = new JLabel( labels[1][i] );
			y_s[i].setBounds( left, top, 30, 30 );
			y_s[i].setFont( font );
			y_s[i].setForeground( new Color( 240, 240, 240 ) );
			y_s[i].setHorizontalTextPosition( SwingConstants.RIGHT );
			panel.add( y_s[i] );
			
			top+=SQUARE_WIDTH;
		}
		
	}
	
	private void changeLabels( String[][] type )
	{
		labels = type;
		
		int top = SQUARE_WIDTH * 10 + Y_OFFSET;
		int left = X_OFFSET + 20;
		
		for( int i = 0; i < 10 ; i ++ )
		{
			JLabel t = (JLabel) panel.findComponentAt( left, top );
			if( labels == null )
			{
				t.setText( "" );
			}
			else
			{
				t.setText( labels[0][i] );
			}
			left+=SQUARE_WIDTH;
		}
		
		top = Y_OFFSET + 10;
		left = X_OFFSET - 25;
		
		for( int i = 9; i >= 0 ; i -- )
		{
			JLabel t = (JLabel) panel.findComponentAt( left, top );
			if( labels == null )
			{
				t.setText( "" );
			}
			else
			{
				t.setText( labels[1][i] );
			}
			top+=SQUARE_WIDTH;
		}
		
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
		
		/* Labels */
		
		JMenu view = new JMenu("View");
		JMenu v_labels = new JMenu("Labels");
		JMenuItem standard = new JMenuItem("Standard");
		standard.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent event)
			{
				changeLabels( Labels.STANDARD );
			}
		});
		JMenuItem inverted = new JMenuItem("Inverted");
		inverted.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent event)
			{
				changeLabels( Labels.INVERTED );
			}
		});
		JMenuItem standard_zero = new JMenuItem("Standard Zero-Based");
		standard_zero.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent event)
			{
				changeLabels( Labels.STANDARD_ZERO );
			}
		});
		JMenuItem inverted_zero = new JMenuItem("Inverted Zero-Based");
		inverted_zero.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent event)
			{
				changeLabels( Labels.INVERTED_ZERO );
			}
		});
		JMenuItem letters = new JMenuItem("Letters");
		letters.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent event)
			{
				changeLabels( Labels.LETTERS );
			}
		});
		JMenuItem numbers = new JMenuItem("Numbers");
		numbers.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent event)
			{
				changeLabels( Labels.NUMBERS );
			}
		});
		JMenuItem numbers_zero = new JMenuItem("Numbers Zero-Based");
		numbers_zero.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent event)
			{
				changeLabels( Labels.NUMBERS_ZERO );
			}
		});
		JMenuItem none = new JMenuItem("None");
		none.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent event)
			{
				changeLabels( null );
			}
		});
		v_labels.add( standard );
		v_labels.add( inverted );
		v_labels.add( standard_zero );
		v_labels.add( inverted_zero );
		v_labels.add( letters );
		v_labels.add( numbers );
		v_labels.add( numbers_zero );
		v_labels.add( none );
		
		view.add( v_labels );
		
		menu.add( view );
		
		setJMenuBar(menu);
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
	
	public void addReadyListener( final ReadyListener rl )
	{
		readyFunctions.add( rl );
	}
	
	/**
	 * Called when the UI is finished working and should do some things that were added to the
	 * Ready Listener.
	 */
	public void ready()
	{
		List<ReadyListener> copy = new ArrayList<ReadyListener>();
		
		copy.addAll( readyFunctions );
		readyFunctions.clear();
		Iterator<ReadyListener> it = copy.iterator();
		
		while(it.hasNext())
		{
			try
			{
				it.next().ready();
			}
			catch( Exception e )
			{
				System.out.println( "Could not invoke ready function." );
			}
		}
		copy.clear();
	}

}
