package checkers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/** 
 * CheckersGui Class
 * 
 * Defines all methods related to user / computer interaction
 * 
 * @author Mike Scoboria, Dan Thurston
 * @version 12/15/2014
 */
@SuppressWarnings("serial")
public class CheckersGui extends JPanel implements ActionListener{

	public static final int SIDE_LENGTH = 85;

	public static final int ROW_COUNT = 8;
	private static final String ROW = "row";
	private static final String COLUMN = "column";

	private static final Color LIGHT_COLOR = new Color(210, 180, 140);
	private static final Color DARK_COLOR = new Color(107, 68, 35);

	private static int computerColor = 0;

	Evaluator eval;
	Rules matrix = null;
	static JDialog confWin = null;
	JLabel gridCell;

	private Map<Checker, Icon> checkerIconMap 
	= new EnumMap<Checker, Icon>(Checker.class);
	private JLabel[][] labelGrid = new JLabel[ROW_COUNT][ROW_COUNT];
	private Checker[][] checkerGrid = new Checker[ROW_COUNT][ROW_COUNT];

	JFrame frame = null;
	//JPanel container = null;

	//constructor initializes board and places piece in correct locations
	public CheckersGui() {

		//creates checkers as JLabel objects and stores in map
		for (Checker checker : Checker.values()) {

			checkerIconMap.put(checker, createCheckerIcon(checker));
		}

		setLayout(new GridLayout(ROW_COUNT, ROW_COUNT));

		int count = 1;
		//sets board properties
		for (int row = 0; row < labelGrid.length; row++) {
			for (int col = 0; col < labelGrid[row].length; col++) {

				//fill with empty checker type first
				checkerGrid[row][col] = Checker.EMPTY;

				//create each square on the board
				gridCell = new JLabel(checkerIconMap.get(Checker.EMPTY));

				//set square properties
				gridCell.setOpaque(true);
				gridCell.putClientProperty(ROW, row);
				gridCell.putClientProperty(COLUMN, col);

				//set square colors
				Color c = row % 2 == col % 2 ? LIGHT_COLOR : DARK_COLOR;

				//apply color to square
				gridCell.setBackground(c);

				String squareNum = row % 2 == col % 2 ? "" : count++ + "";
				gridCell.setText(squareNum);
				gridCell.setHorizontalTextPosition(JLabel.LEFT);
				gridCell.setVerticalTextPosition(JLabel.BOTTOM);
				gridCell.setForeground(Color.WHITE);
				gridCell.setBorder(new EmptyBorder(0, 2, 2, 0));
				gridCell.setIconTextGap(-15);

				add(gridCell);

				labelGrid[row][col] = gridCell;
			}
		}

		MyMouseAdapter myMouseAdapter = new MyMouseAdapter();
		addMouseListener(myMouseAdapter);
		addMouseMotionListener(myMouseAdapter);
	}

	//empty the board
	public void clearBoard(){

		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){

				labelGrid[i][j].setIcon(checkerIconMap.get(Checker.EMPTY));
				checkerGrid[i][j] = Checker.EMPTY;
			}
		}
	}

	//Creates checkers based on Rules matrix and places them on
	//the gui.  Also places checker enum object in checkerGrid for reference 
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
				else if(matrix.returnBoard()[i][j] == 3){
				  labelGrid[i][j].setIcon(checkerIconMap.get(Checker.KINGBLACK));
				  checkerGrid[i][j] = Checker.KINGBLACK;
				}
				else if(matrix.returnBoard()[i][j] == 4){
				  labelGrid[i][j].setIcon(checkerIconMap.get(Checker.KINGRED));
				  checkerGrid[i][j] = Checker.KINGRED;
				}
				else{
				  labelGrid[i][j].setIcon(checkerIconMap.get(Checker.EMPTY));
				  checkerGrid[i][j] = Checker.EMPTY;
				}
			}
		}
	}

	//create checker icons to be placed on the gui
	private Icon createCheckerIcon(Checker checker) {

		BufferedImage img = new BufferedImage
			(SIDE_LENGTH, SIDE_LENGTH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2 = img.createGraphics();
		g2.setColor(checker.getColor());
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
				RenderingHints.VALUE_ANTIALIAS_ON);

		int x = 3;
		int y = x;
		int width = SIDE_LENGTH - 2 * x;
		int height = width;
		g2.fillOval(x, y, width, height);
		g2.dispose();

		if(checker == Checker.RED)
		{ return new ImageIcon("images/RedChecker.png"); }
		else if(checker == Checker.BLACK)
		{ return new ImageIcon("images/BlackChecker.png"); }
		else if(checker == Checker.KINGRED)
		{ return new ImageIcon("images/RedKing.png"); }
		else if(checker == Checker.KINGBLACK)
		{ return new ImageIcon("images/BlackKing.png"); }		

		return new ImageIcon(img);
	}

	//Defines all properties that are the result of mouse actions
	//includes calls to the evaluator.
	private class MyMouseAdapter extends MouseAdapter {

		private int selectedRow = -1;
		private int selectedCol = -1;
		private int prevSquare;

		private Checker selectedChecker = null;
		private JPanel glassPane = null;
		private Point p = null;
		

		private JLabel movingLabel = new JLabel(checkerIconMap.get(Checker.EMPTY));

		public MyMouseAdapter() {

			movingLabel.setSize(movingLabel.getPreferredSize());
			movingLabel.setVisible(false);
		}

		//on mouse click, select checker and create state
		//that allows checker to mouse tracking
		@Override
		public void mousePressed(MouseEvent e) {

			//matrix.printBoard();
			p = e.getPoint();

			for (int row = 0; row < labelGrid.length; row++) {
				for (int col = 0; col < labelGrid[row].length; col++) {

					JLabel gridCell = labelGrid[row][col];

					if (gridCell == getComponentAt(p)) {
						if (checkerGrid[row][col] != Checker.EMPTY) {

							selectedRow = row;
							selectedCol = col;

							matrix.validUserPiece
							(matrix.coordinatesToSquare(row, col));
							selectedChecker = checkerGrid[row][col];

							checkerGrid[row][col] = Checker.EMPTY;
							labelGrid[row][col].setIcon
							(checkerIconMap.get(Checker.EMPTY));


							JRootPane rootPane = SwingUtilities
									.getRootPane(CheckersGui.this);

							glassPane = (JPanel) rootPane.getGlassPane();
							glassPane.setVisible(true);
							glassPane.setLayout(null);

							movingLabel.setIcon(checkerIconMap
									.get(selectedChecker));
							movingLabel.setVisible(true);

							glassPane.add(movingLabel);

							int x = p.x - SIDE_LENGTH / 2;
							int y = p.y - SIDE_LENGTH / 2;

							movingLabel.setLocation(x, y);	
						}
					}
				}
			}
		}

		//upon mouse release, set checker piece in correct location and 
		//allow the board state to be evaluated
		@Override
		public void mouseReleased(MouseEvent e) {
			if (selectedChecker == null) {
				return;
			}

			p = e.getPoint();
			if (!CheckersGui.this.contains(p)) {
				returnCheckerToOriginalCell();
				clearGlassPane();
				return;
			}

			prevSquare = matrix.returnPieceAtSquare
					(matrix.coordinatesToSquare(selectedRow, selectedCol));

			for (int row = 0; row < labelGrid.length; row++) {
				for (int col = 0; col < labelGrid[row].length; col++) {
					JLabel gridCell = labelGrid[row][col];
					if (gridCell == getComponentAt(p)) {  
						if (matrix.validUserMove
								(matrix.coordinatesToSquare(row, col))){ 

							createPieces();

							int test1 = Math.abs(row-selectedRow);
							boolean test2 = !matrix.justKinged
								(prevSquare,
								matrix.coordinatesToSquare(row, col));
							boolean test3 = matrix.validUserPiece
								(matrix.coordinatesToSquare(row, col)); 
							boolean test4 
								= matrix.isNextJumpAvailable(row, col);

							if(test1 == 2 && test2 && test3 && test4){
								createPieces();
							}
							//if another jump is not possible,
							//get the next computer move
							else{
								matrix.setBoard
								(eval.evaluate(matrix.getBoard()));

								createPieces();
								gameOver();
							}


						} else {
							// illegal move
							returnCheckerToOriginalCell();
						}
					}
				}
			}
			clearGlassPane();
		}

		//update checker location on game board when 
		//piece selected and dragged
		@Override
		public void mouseDragged(MouseEvent e) {

			if (selectedChecker == null || p == null) {
				return;
			}

			p = e.getPoint();
			int x = p.x - SIDE_LENGTH / 2;
			int y = p.y - SIDE_LENGTH / 2;
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
			labelGrid[selectedRow][selectedCol]
					.setIcon(checkerIconMap.get(selectedChecker));
		}
	}

	//Initialize game based on user choice
	//@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("Red")){

			computerColor = 1;
			matrix = new Rules(2);
			createPieces();

			eval = new Evaluator(computerColor);

			matrix.setBoard(eval.evaluate(matrix.getBoard()));
			createPieces();
		}
		else if (e.getActionCommand().equals("Black")){
			computerColor = 2;
			matrix = new Rules(1);
			createPieces();

			eval = new Evaluator(computerColor);
		}
		else if (e.getActionCommand().equals("Quit")){

			System.exit(0);
		}

	}

	//create window drop down menu
	public static JMenuBar createWindowMenu(CheckersGui mainPanel){

		JMenu gameMenu = new JMenu("Options");

		JMenu newGame = new JMenu("New Game");
		gameMenu.add(newGame);

		JLabel choose = new JLabel("Choose Your Color");
		newGame.add(choose);

		JMenuItem redChoice = new JMenuItem("Red");
		redChoice.addActionListener(mainPanel);
		newGame.add(redChoice);

		JMenuItem blackChoice = new JMenuItem("Black");
		blackChoice.addActionListener(mainPanel);
		newGame.add(blackChoice);


		JMenuItem otherOption = new JMenuItem("Quit");
		otherOption.addActionListener(mainPanel);
		gameMenu.add(otherOption);

		JMenuBar bar = new JMenuBar();
		bar.add(gameMenu);

		return bar;
	}

	//create popup for end of game user info/selection
	public void gameOver(){

		int winner = 0;
		matrix.countCapturedPieces();
		if(matrix.getCompPiecesCaptured() == 0) 
			winner = matrix.getUser();
		else if(matrix.getUserPiecesCaptured() == 0)
			winner = matrix.getComputer();

		if(winner != 0){
			String color = null;
			if(winner == 1) color = "Black";
			else if(winner == 2) color = "Red";

			String message1 = color + " Wins!";
			String message2 = "Choose a color for a new game";
			String message3 = "or 'Quit' to end game";
			JDialog goDialog = new JDialog();
			goDialog.setSize(280, 125);
			goDialog.setLayout( new FlowLayout());
			goDialog.setAlwaysOnTop(true);


			JLabel winMsg = new JLabel(message1, JLabel.CENTER);
			goDialog.add(winMsg);
			JLabel nxtMsg1 = new JLabel(message2, JLabel.CENTER);
			goDialog.add(nxtMsg1);
			JLabel nxtMsg2 = new JLabel(message3, JLabel.CENTER);
			goDialog.add(nxtMsg2);


			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout());

			JButton redButton = new JButton("Red");
			redButton.addActionListener(this);
			redButton.addActionListener(new AbstractAction(){
				public void actionPerformed(ActionEvent arg0){
					goDialog.dispose();
				}
			});
			buttonPanel.add(redButton);

			JButton blackButton = new JButton("Black");
			blackButton.addActionListener(this);
			blackButton.addActionListener(new AbstractAction(){
				public void actionPerformed(ActionEvent arg0){
					goDialog.dispose();
				}
			});
			buttonPanel.add(blackButton);

			JButton quitButton = new JButton("Quit");
			quitButton.addActionListener(this);
			buttonPanel.add(quitButton);

			goDialog.add(buttonPanel, BorderLayout.SOUTH);
			goDialog.setLocationRelativeTo(this);
			goDialog.setVisible(true);
		}
	}

	//create pop-up window for start of game user choice
	public static void confirmWindow(String message, CheckersGui mainPanel){

		JDialog confWin = new JDialog();
		confWin.setSize(200, 125);
		confWin.setLayout( new BorderLayout());
		confWin.setAlwaysOnTop(true);

		JLabel confirmLabel = new JLabel(message, JLabel.CENTER);
		confWin.add(confirmLabel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		JButton redButton = new JButton("Red");
		redButton.addActionListener(mainPanel);
		redButton.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent arg0){
				confWin.dispose();
			}
		});
		buttonPanel.add(redButton);

		JButton blackButton = new JButton("Black");
		blackButton.addActionListener(mainPanel);
		blackButton.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent arg0){
				confWin.dispose();
			}
		});
		buttonPanel.add(blackButton);

		confWin.add(buttonPanel, BorderLayout.SOUTH);
		confWin.setLocationRelativeTo(mainPanel);
		confWin.setVisible(true);
	}	

	//create window for game play
	private static void createAndShowGui() {

		CheckersGui mainPanel = new CheckersGui();

		JFrame frame = new JFrame("JLabelGrid");

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		frame.setLayout(new GridBagLayout());
		frame.setJMenuBar(CheckersGui.createWindowMenu(mainPanel));
		frame.getContentPane().add(mainPanel);
		frame.pack();
		//frame.setLocationByPlatform(true);
		frame.setLocation(600, 20);	
		frame.setVisible(true);

		confirmWindow("Choose a color", mainPanel);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				CheckersGui.createAndShowGui();
			}
		});
	}


}
//
////enum to define checker type
//enum Checker {
//
//	EMPTY(new Color(0, 0, 0, 0)), 
//	RED(Color.red), 
//	BLACK(Color.black),
//	KINGRED(Color.blue),
//	KINGBLACK(Color.green);
//
//	private Color color;
//
//	private Checker(Color color) {
//		this.color = color;
//	}
//
//	public Color getColor() {
//		return color;
//	}
//}