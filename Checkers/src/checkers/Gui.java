package checkers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.*;


@SuppressWarnings("serial")
public class Gui extends JPanel implements ActionListener{
	
	//checker diameter
	public static final int SIDE_LENGTH = 80;
	
	//#rows on board
	public static final int ROW_COUNT = 8;
	
	
	private static final String ROW = "row";
	private static final String COLUMN = "column";
	
	//board colors
	private static final Color LIGHT_COLOR = new Color(210, 180, 140);
	private static final Color DARK_COLOR = new Color(107, 68, 35);
	
	CheckersGame matrix = null;
	
	private Map<Checker, Icon> checkerIconMap = new EnumMap<Checker, Icon>(Checker.class);
	
	//2D array of JLabel objects initialized to 8 x 8 size  
	private JLabel[][] labelGrid = new JLabel[ROW_COUNT][ROW_COUNT];
	
	//2D array of Checker objects initialized to 8 x 8 size (Checker is enum)
	private Checker[][] checkerGrid = new Checker[ROW_COUNT][ROW_COUNT];
	
	
	//constructor for Checkers
	public Gui() {
		
		//create a checker icon (based on enum Checker value) and put in the Map (this is the value)
		//checker is the key
		matrix = new CheckersGame();
		
		for (Checker checker : Checker.values()) {
		
			checkerIconMap.put(checker, createCheckerIcon(checker));
		}
		
		
		//sets JPanel Layout to GridLayout based on ROW_COUNT
		setLayout(new GridLayout(ROW_COUNT, ROW_COUNT));

		
		for (int row = 0; row < labelGrid.length; row++) {
			for (int col = 0; col < labelGrid[row].length; col++) {
			
				//fills checkerGrid with empty enum checkers
				checkerGrid[row][col] = Checker.EMPTY;
				
				//creates a Jlabel with image set to the empty checker from the checkerIconMap
				JLabel gridCell = new JLabel(checkerIconMap.get(Checker.EMPTY));
				
				//opacity of cell
				gridCell.setOpaque(true);
				
				//adds the string "row" iteratively to the JLabel as a property (based on ROW)
				gridCell.putClientProperty(ROW, row);
				
				//adds the string "column" iteratively to the JLabel as a property (based on COLUMN)
				gridCell.putClientProperty(COLUMN, col);
				
				//if/else to decide on the correct color for the cell
				Color c = row % 2 == col % 2 ? LIGHT_COLOR : DARK_COLOR;
				
				//sets the color of the cell 
				gridCell.setBackground(c);
				
				//gridcell(JLabel) added to JPanel (Checkers)
				add(gridCell);
				
				//sets labelGrid position to the gridcell JLabel
				labelGrid[row][col] = gridCell;
			}
		}

		createPieces();
		//adds mouselisteners to Checkers JPanel
		MyMouseAdapter myMouseAdapter = new MyMouseAdapter();
		addMouseListener(myMouseAdapter);
		addMouseMotionListener(myMouseAdapter);
	}
	
	public void createPieces(){
			
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
					
				if(matrix.returnBoard()[i][j] == 1){
					labelGrid[i][j].setIcon(checkerIconMap.get(Checker.BLACK));
					checkerGrid[i][j] = Checker.BLACK;
				}
				else if(matrix.returnBoard()[i][j] == 2){
					labelGrid[i][j].setIcon(checkerIconMap.get(Checker.RED));
					checkerGrid[i][j] = Checker.RED;
			
				}
			}
		}
	}
	
	//This just creates the checker ImageIcon using BufferedImage and sets
	//properties using graphics2D
	private Icon createCheckerIcon(Checker checker) {
		
		BufferedImage img = new BufferedImage(SIDE_LENGTH, SIDE_LENGTH, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g2 = img.createGraphics();
		g2.setColor(checker.getColor());
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		int x = 3;
		int y = x;
		int width = SIDE_LENGTH - 2 * x;
		int height = width;
		g2.fillOval(x, y, width, height);
		g2.dispose();

		return new ImageIcon(img);
	}

	
	
	
	
	
	
	
	
	private class MyMouseAdapter extends MouseAdapter {
		
		//sets these to impossible values
		private int selectedRow = -1;
		private int selectedCol = -1;
		
		//creates enum Checker variable
		private Checker selectedChecker = null;
		
		//creates new JPanel for some reason
		private JPanel glassPane = null;
		
		//creates Point variable 
		private Point p = null;
		
		//Creates JLabel specifically for the movement I think
		private JLabel movingLabel = new JLabel(checkerIconMap.get(Checker.EMPTY));

		//Constructor of MyMouseAdapter
		public MyMouseAdapter() {
			
			//sets movingLabel to a preset Preferred size
			movingLabel.setSize(movingLabel.getPreferredSize());
			//JLabel set to invisible at first 
			movingLabel.setVisible(false);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
			//Point p gets the point object returned by the mouseclick event
			p = e.getPoint();
			
			
			for (int row = 0; row < labelGrid.length; row++) {
				for (int col = 0; col < labelGrid[row].length; col++) {
					
					//creates JLabel object, instantiates it to the JLabel in the specified position in labelGrid
					JLabel gridCell = labelGrid[row][col];
					
					//test gridCell Label against the component returned at position p
					if (gridCell == getComponentAt(p)) {
						//do stuff if the checkerGrid position is not defined as EMPTY
						if (checkerGrid[row][col] != Checker.EMPTY) {
							
							//set the selectedRow and selectedColumn to row and col
							selectedRow = row;
							selectedCol = col;
							
							
							matrix.validUserPiece(matrix.coordinatesToSquare(row, col));
							//set enum Checker selectedChecker to that found in the checkGrid position (to store)
							selectedChecker = checkerGrid[row][col];
							
							//set the enum type in the position in checkerGrid to EMPTY
							checkerGrid[row][col] = Checker.EMPTY;
							//Here is where the checker in the position "disappears"
							//sets labelGrid at this position to the "empty" checker stored in the MAP
							labelGrid[row][col].setIcon(checkerIconMap.get(Checker.EMPTY));

							
							JRootPane rootPane = SwingUtilities.getRootPane(Gui.this);
							
							
							glassPane = (JPanel) rootPane.getGlassPane();
							glassPane.setVisible(true);
							glassPane.setLayout(null);
							
							//set icon type and visibility
							movingLabel.setIcon(checkerIconMap.get(selectedChecker));
							movingLabel.setVisible(true);
							
							//add mving label to the panel
							glassPane.add(movingLabel);
							
							//get x and y from point
							int x = p.x - SIDE_LENGTH / 2;
							int y = p.y - SIDE_LENGTH / 2;
							
							//setLocation based on x and y
							movingLabel.setLocation(x, y);	
						}
					}
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (selectedChecker == null) {
				return;
			}

			p = e.getPoint();
			if (!Gui.this.contains(p)) {
				// if mouse releases and is totally off of the grid
				returnCheckerToOriginalCell();
				clearGlassPane();
				return;
			}

			for (int row = 0; row < labelGrid.length; row++) {
				for (int col = 0; col < labelGrid[row].length; col++) {
					JLabel gridCell = labelGrid[row][col];
					if (gridCell == getComponentAt(p)) {
						if (matrix.validUserMove(matrix.coordinatesToSquare(row, col))){
							checkerGrid[row][col] = selectedChecker;
							labelGrid[row][col].setIcon(checkerIconMap.get(selectedChecker));
							matrix.printBoard();
							/*
							if(selectedChecker == Checker.BLACK)
								matrix.movePiece(selectedRow, selectedCol, row, col, 1);
							else
								matrix.movePiece(selectedRow, selectedCol, row, col, 2);
							*/
							// todo: check for jumped pieces...
						} else {
							// illegal move
							returnCheckerToOriginalCell();
						}
					}
				}
			}
			clearGlassPane();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			
			//I think this is for when an area outside the board 
			//is selected
			if (selectedChecker == null || p == null) {
				return;
			}
			
			//get the current mouse point
			p = e.getPoint();
			//get x and y from point
			int x = p.x - SIDE_LENGTH / 2;
			int y = p.y - SIDE_LENGTH / 2;
			//resets the position of the movingLabel JLabel
			movingLabel.setLocation(x, y);
		}

		private void clearGlassPane() {
			glassPane.setVisible(false);
			movingLabel.setVisible(false);
			selectedChecker = null;
			p = null;
			selectedCol = -1;
			selectedRow = -1;
		}

		private void returnCheckerToOriginalCell() {
			checkerGrid[selectedRow][selectedCol] = selectedChecker;
			labelGrid[selectedRow][selectedCol].setIcon(checkerIconMap.get(selectedChecker));
		}
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		if(command.equals("Quit")){
			
			System.exit(0);	
		}
		else if(command.equals("Red") || command.equals("Black")){
			
			
		}
	}
	
	public JMenuBar newMenu(){
		
		JMenu gameMenu = new JMenu("Options");
		
		JMenu newGame = new JMenu("New Game");
		gameMenu.add(newGame);
		
		JLabel choose = new JLabel("Choose Your Color");
		newGame.add(choose);
		
		JMenuItem redChoice = new JMenuItem("Red");
		redChoice.addActionListener(this);
		newGame.add(redChoice);
		
		JMenuItem blackChoice = new JMenuItem("Black");
		blackChoice.addActionListener(this);
		newGame.add(blackChoice);
		
		
		JMenuItem otherOption = new JMenuItem("Quit");
		otherOption.addActionListener(this);
		gameMenu.add(otherOption);
		
		JMenuBar bar = new JMenuBar();
		bar.add(gameMenu);
		
		return bar;
	}
	
	public JPanel startPanel(){
	
		 JPanel startPanel = new JPanel();
		 startPanel.setSize(400, 300);
		 startPanel.setLayout(new FlowLayout());
		 
		 JButton redButton = new JButton("Red");
		 redButton.setBackground(Color.RED);
		 redButton.addActionListener(this);
		 JButton blackButton = new JButton("Black");
		 blackButton.addActionListener(this);
		 blackButton.setBackground(Color.BLACK);
		 blackButton.setForeground(Color.WHITE);
		 
		 startPanel.add(redButton);
		 startPanel.add(blackButton);
		 
		 return startPanel;
	}
	
	private static void createAndShowGui() {
		//Gui mainPanel = new Gui();

		JFrame frame = new JFrame("JLabelGrid");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(new FlowLayout());
		
		Gui bar = new Gui();
		frame.setJMenuBar(bar.newMenu());
		
		frame.getContentPane().add(bar.startPanel());
		
		frame.getContentPane().add(bar);
		frame.pack();
		
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGui();
			}
		});
	}

	
}

/*
enum Checker {

	EMPTY(new Color(0, 0, 0, 0)), 
	RED(Color.red), 
	BLACK(Color.black);
	
	private Color color;

	private Checker(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
}
*/