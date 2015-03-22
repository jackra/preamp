/**
 */

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.text.Document;

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * 
 *
 */
public class ConnectFour implements ActionListener, ItemListener {
	// create objects
	static PlayBoard board = new PlayBoard();
	static JFrame frameMainWindow;
	static JFrame frameWin;
	static JFrame frameErr;
	static int counter=0;
	static String playerColor="None";
	static JFrame frameCredits;
	static int red=0;
	static int green=0;
	static JPanel panelNumbers;
	static JLayeredPane GameBoard;
	private static PlayerInterface p1 = new HumanPlayer();
	private static PlayerInterface p2 = new HumanPlayer();
	private static int p1TypeFlag = 0;
	private static int p2TypeFlag = 0;
	static JPanel statusPanel = new JPanel();
	static JLabel statusLabel1 = new JLabel("Moves so far :"+counter);
	static JLabel statusLabel2 = new JLabel("Last Move :"+playerColor);
	static JLabel statusLabel3 = new JLabel("   ( Red First )   ");
	static JLabel statusLabel4 = new JLabel("         Score : "+red+"-"+green);
	public JMenuBar createMenuBar() {
		JMenuBar menuBar;
		JMenu menu, submenu, subsubmenu;
		JMenuItem menuItem;
		JRadioButtonMenuItem rbMenuItem;
		int[][] boardView;
		// create and build first menu
		menuBar = new JMenuBar();
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(menu);
		// add items to menu
		menuItem = new JMenuItem("New game", KeyEvent.VK_N);
		menuItem.setName("NewGame");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menu.addSeparator();
		menuItem = new JMenuItem("Quit", KeyEvent.VK_Q);
		menuItem.setName("QuitGame");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		// create and build second menu
		menu = new JMenu("Players");
		menu.setMnemonic(KeyEvent.VK_P);
		menuBar.add(menu);
		// add items to menu
		// Submenu one
		submenu = new JMenu("Red Player");
		ButtonGroup groupPlayers1 = new ButtonGroup();
		rbMenuItem = new JRadioButtonMenuItem("Human");
		if (p1.getType() == 0) rbMenuItem.setSelected(true);
		rbMenuItem.setName("P1Human");
		rbMenuItem.addItemListener(this);
		groupPlayers1.add(rbMenuItem);
		submenu.add(rbMenuItem);
		//subsubmenu = new JMenu("Computer");
		rbMenuItem = new JRadioButtonMenuItem("Computer");
		if (p1.getType() == 2) rbMenuItem.setSelected(true);
		rbMenuItem.setName("P1Computer");
		rbMenuItem.addItemListener(this);
		groupPlayers1.add(rbMenuItem);
		submenu.add(rbMenuItem);
		menu.add(submenu);
		menu.addSeparator();
		// submenu 2
		submenu = new JMenu("Green Player");
		ButtonGroup groupPlayers2 = new ButtonGroup();
		rbMenuItem = new JRadioButtonMenuItem("Computer");
		if (p2.getType() == 2) rbMenuItem.setSelected(true);
		rbMenuItem.setName("P2Computer");
		rbMenuItem.addItemListener(this);
		groupPlayers2.add(rbMenuItem);
		submenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Human");
		if (p1.getType() == 0) rbMenuItem.setSelected(true);
		rbMenuItem.setName("P2Human");
		rbMenuItem.setSelected(true);
		rbMenuItem.setMnemonic(KeyEvent.VK_H);
		groupPlayers2.add(rbMenuItem);
		rbMenuItem.addItemListener(this);
		submenu.add(rbMenuItem);
		menu.add(submenu);
		menuBar.add(Box.createGlue());
		// create Help menu
		menu = new JMenu("Help");
		menuBar.add(menu);
		// add items to menu
		menuItem = new JMenuItem("Display");
		menuItem.setName("Display");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menu.addSeparator();
		menuItem = new JMenuItem("Report");
		menuItem.setName("Report");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		//	menu.add(Box.createHorizontalGlue());
		menu.addSeparator();
		menuItem = new JMenuItem("About");
		menuItem.setName("About");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		return menuBar;
	}
	public static JLayeredPane createLayeredBoard() {
		GameBoard = new JLayeredPane();
		GameBoard.setPreferredSize(new Dimension(570, 490));
		GameBoard.setBorder(BorderFactory.createTitledBorder("Connect 4"));
		ImageIcon imageBoard = new ImageIcon("images/Board.jpg");
		JLabel imageBoardLabel = new JLabel(imageBoard);
		imageBoardLabel.setBounds(20, 20, imageBoard.getIconWidth(), imageBoard.getIconHeight());
		GameBoard.add(imageBoardLabel, new Integer (0), 1);
		return GameBoard;
	}
	/**
	 * 
	 */
	public static void createNewGame() {
		board = new PlayBoard();
		board.out=false;
		if (frameMainWindow != null) frameMainWindow.dispose();
		frameMainWindow = new JFrame("Connect4");
		frameMainWindow.setBounds(100, 100, 400, 300);
		ConnectFour conFour = new ConnectFour();
		frameMainWindow.setJMenuBar( conFour.createMenuBar() );
		Component compMainWindowContents = createContentComponents();
		frameMainWindow.getContentPane().add(compMainWindowContents, BorderLayout.CENTER);
		frameMainWindow.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// show window
		frameMainWindow.pack();
		frameMainWindow.setVisible(true);
		if (p1TypeFlag == 1) {
			p1.go(board);
			redrawBoard();
		}
		if ( (p1TypeFlag == 1) && (p2TypeFlag == 1) ) {
			panelNumbers.setEnabled(false);
			panelNumbers.repaint();
			while (!board.over() ){
				if (board.next() == 1) p1.go(board);
				else p2.go(board);
				redrawBoard();
			}
			panelNumbers.setVisible(false);
			panelNumbers.repaint();
		}
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		createStatusPanel();
	}
	/**
	 * 
	 */
	private static void createStatusPanel() {
		statusLabel1.setText("         Moves so far :"+counter);
		statusLabel2.setText("Last Move :"+playerColor);
		statusLabel4.setText("       Score : "+red+"-"+green);
		statusPanel.add(statusLabel2);
		statusPanel.add(statusLabel4);
		statusPanel.add(statusLabel3);
		statusPanel.add(statusLabel1);
		frameMainWindow.add(statusPanel, BorderLayout.SOUTH);
	}
	public static void paintRed(int row, int col) {
		int xOffset = 75 * col;
		int yOffset = 75 * row;
		ImageIcon redIcon = new ImageIcon("images/Red.jpg");
		JLabel redIconLabel = new JLabel(redIcon);
		redIconLabel.setBounds(27 + xOffset, 27 + yOffset, redIcon.getIconWidth(),redIcon.getIconHeight());
		GameBoard.add(redIconLabel, new Integer(0), 0);
		frameMainWindow.paint(frameMainWindow.getGraphics());
	}
	/**
	 * 
	 * @param row
	 * @param col
	 */
	public static void paintBlack(int row, int col) {
		int xOffset = 75 * col;
		int yOffset = 75 * row;
		ImageIcon blackIcon = new ImageIcon("images/Green.jpg");
		JLabel blackIconLabel = new JLabel(blackIcon);
		blackIconLabel.setBounds(27 + xOffset, 27 + yOffset, blackIcon.getIconWidth(),blackIcon.getIconHeight());
		GameBoard.add(blackIconLabel, new Integer(0), 0);
		frameMainWindow.paint(frameMainWindow.getGraphics());
	}
	/**
	 * 
	 */
	public static void redrawBoard() {

		int[][] boardView = board.view();
		int r = board.move_x;
		int c = board.move_y;
		try{
		int playerPos = boardView[r][c];
		if (playerPos == 1) {
			paintRed(r, c);
			playerColor="red";
			System.out.println("xCurrent Board View");
			checkPlayerScore(board.location);
			System.out.println("Player 1 Red"+red+"Green"+green);
			System.out.println(board);
			counter=counter+1;
		} else if (playerPos == 2) {
			paintBlack(r, c);
			playerColor="green";
			System.out.println("yCurrent Board View");
			checkPlayerScore(board.location);
			System.out.println("Player 2Red"+red+"Green"+green);
			System.out.println(board);
			counter=counter+1;
		}}catch(ArrayIndexOutOfBoundsException e ){
			System.out.println("Invalid Move Column is full");
		}
		System.out.println("(Red First)");
		System.out.println("         Moves so Far::"+counter);
		System.out.println("***********************");
		createStatusPanel();
		if (board.over() == true) {
			checkPlayerScore(board.location);
			gameOverPanelUpdate();
		}
	}
	/**
	 * 
	 */
	public static void gameOverPanelUpdate() {
		System.out.println("red"+red);
		System.out.println("green"+green);
		createStatusPanel();
		endOfGame();
	}
	/**
	 * 
	 */
	public static void game(){
		if (board.next() == 1) p1.go(board);
		else p2.go(board);
		redrawBoard();
		if (!board.over()) {
			int nextTypeFlag = 0;
			if (board.next() == 1) nextTypeFlag = p1TypeFlag;
			else nextTypeFlag = p2TypeFlag;
			if (nextTypeFlag == 1) {
				if (board.next() == 1) p1.go(board);
				else p2.go(board);
				redrawBoard();
			}
		}
	}

	/**
	 * @return Component - Returns a component to be drawn by main window
	 * @see main()
	 * This function creates the main window components.
	 */
	public static Component createContentComponents() {

		// create panels to hold and organize board numbers
		panelNumbers = new JPanel();
		panelNumbers.setLayout(new GridLayout(1, 7, 4, 4));
		JButton buttonCol0 = new JButton("1");
		buttonCol0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (board.next() == 1) p1.setMove(0);
				else p2.setMove(0);
				game();
			}
		});
		JButton buttonCol1 = new JButton("2");
		buttonCol1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (board.next() == 1) p1.setMove(1);
				else p2.setMove(1);
				game();
			}
		});
		JButton buttonCol2 = new JButton("3");
		buttonCol2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (board.next() == 1) p1.setMove(2);
				else p2.setMove(2);
				game();
			}
		});
		JButton buttonCol3 = new JButton("4");
		buttonCol3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (board.next() == 1) p1.setMove(3);
				else p2.setMove(3);
				game();
			}
		});
		JButton buttonCol4 = new JButton("5");
		buttonCol4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (board.next() == 1) p1.setMove(4);
				else p2.setMove(4);
				game();
			}
		});
		JButton buttonCol5 = new JButton("6");
		buttonCol5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (board.next() == 1) p1.setMove(5);
				else p2.setMove(5);
				game();
			}
		});
		JButton buttonCol6 = new JButton("7");
		buttonCol6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (board.next() == 1) p1.setMove(6);
				else p2.setMove(6);
				game();
			}
		});
		panelNumbers.add(buttonCol0);
		panelNumbers.add(buttonCol1);
		panelNumbers.add(buttonCol2);
		panelNumbers.add(buttonCol3);
		panelNumbers.add(buttonCol4);
		panelNumbers.add(buttonCol5);
		panelNumbers.add(buttonCol6);
		// create game board with pieces
		GameBoard = createLayeredBoard();
		// create panel to hold all of above
		JPanel panelMain = new JPanel();
		panelMain.setLayout(new BorderLayout());
		panelMain.setBorder( BorderFactory.createEmptyBorder(5, 5, 5, 5) );
		// add objects to pane
		panelMain.add(panelNumbers, BorderLayout.NORTH);
		panelMain.add(GameBoard, BorderLayout.CENTER);
		return panelMain;
	}
	/**
	 * 
	 * @param o
	 * @return
	 */
	// Returns just the class name -- no package info.
	protected String getClassName(Object o) {
		String classString = o.getClass().getName();
		int dotIndex = classString.lastIndexOf(".");
		return classString.substring(dotIndex+1);
	}
	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		JMenuItem source = (JMenuItem)(e.getSource());
		String s = source.getName();

		if ( s.equals("NewGame") ) {
			// create new game
			playerColor="none";
			counter=0;
			red=0;
			green=0;
			
			createStatusPanel();
			createNewGame();
		} else if ( s.equals("QuitGame")) {
			System.exit(0);
		} else if (s.equals("Display")) {
			showDisplay();
		}else if (s.equals("Report")) {
			showReports();
		} else if (s.equals("About")) {
			showCredits();
		}

	}
	/**
	 * 
	 */
	public void itemStateChanged(ItemEvent e) {
		JMenuItem source = (JMenuItem)(e.getSource());
		String s = source.getName() +
				((e.getStateChange() == ItemEvent.SELECTED) ?
						"-selected":"-unselected");
		if (s.equals("P1Human-selected")) {
			p1 = new HumanPlayer();
			p1TypeFlag = 0;
		}  else if (s.equals("P1Computer-selected")) {
			p1 = new ComputerPlayer();
			p1TypeFlag = 1;
		} else if (s.equals("P2Human-selected")) {
			p2 = new HumanPlayer();
			p2TypeFlag = 0;
		} else if (s.equals("P2Computer-selected")) {
			p2 = new ComputerPlayer();
			p2TypeFlag = 1;
		}
	}
	/**
	 * 
	 */
	public static void endOfGame() {
		panelNumbers.setVisible(false);
		frameWin = new JFrame("Winner!");
		frameWin.setBounds(300, 300, 220, 120);
		JPanel winPanel = new JPanel(new BorderLayout());
		winPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
		JLabel winLabel;
		if (red>green) {
			winLabel = new JLabel("Red Player wins !!");
			System.out.println("Red Player wins !!");
			winPanel.add(winLabel);
		} else if (green>red) {
			winLabel = new JLabel("Green Player wins !!");
			winPanel.add(winLabel);
			System.out.println("Green Player wins !!");
		} else {
			winLabel = new JLabel("Nobody Win! - You both loose!");
			winPanel.add(winLabel);
			System.out.println("Nobody Win! - You both loose!");
		}
		winPanel.add(winLabel, BorderLayout.NORTH);
		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameWin.setVisible(false);
			}
		});
		winPanel.add(okButton, BorderLayout.SOUTH);
		frameWin.getContentPane().add(winPanel, BorderLayout.CENTER);
		frameWin.setVisible(true);
	}
	/**
	 * 
	 * @param loc
	 */
	private static void checkPlayerScore(int[][] loc) {
		int ii,  ri, ci, di;
		red=0;
		green=0;
		String checkGrid[] = new String[25];
		// copy rows:
		for(ri = 0; ri < 6; ri++) {
			String temp = "";
			for(ci = 0; ci < 7; ci++) {
				temp += loc[ri][ci];
			}
			checkGrid[ri] = temp;
		}
		// copy columns:
		for(ci = 0; ci < 7; ci++) {
			String temp = "";
			for(ri = 0; ri < 6; ri++) {
				temp += loc[ri][ci];
			}
			checkGrid[ci + 6] = temp;
		}
		// copy first diagonals:
		for(di = 0; di < 6; di++) {
			String temp = "";
			for(ri = 0; ri < 6; ri++) {
				ci = di - 2;
				ri = 0;
				while(ci < 0) {
					ri++;
					ci++;
				}
				for(; ri < 6; ri++, ci++) {
					if( ci > 6 ) continue;
					temp += loc[ri][ci];
				}
			}
			checkGrid[di+13] = temp;
		}
		// diagonals in the other direction:
		for(di = 0; di < 6; di++) {
			String temp = "";
			for(ri = 0; ri < 6; ri++) {
				ci = 8 - di;
				ri = 0;
				while(ci >  6) {
					ri++;
					ci--;
				}
				for(; ri < 6; ri++, ci--) {
					if( ci < 0 ) continue;
					temp += loc[ri][ci];
				}
			}
			checkGrid[di+19] = temp;
		}
		for(ii = 0; ii < 25; ii++) {
			//System.out.println("Checking '" + checkGrid[ii] + "'");
			if (checkGrid[ii].contains("1111"))
			{
				red=red+1; System.out.println("Player A wins!");
			}
			if (checkGrid[ii].contains("11111"))
			{
				red=red+1; System.out.println("Player A wins!");
			}
			if (checkGrid[ii].contains("111111"))
			{
				red=red+1; System.out.println("Player A wins!");
			}
			if (checkGrid[ii].contains("2222"))
			{System.out.println("Player B wins!");
			green=green+1; }
			if (checkGrid[ii].contains("22222"))
			{
				green=green+1;System.out.println("Player B wins!");
			}
			if (checkGrid[ii].contains("222222"))
			{
				green=green+1;System.out.println("Player B wins!");
			}
		}
	}
	/**
	 * 
	 */
	public static void showCredits() {
		frameCredits = new JFrame("Credits");
		frameCredits.setBounds(300, 300, 480, 320);
		JPanel winPanel = new JPanel(new BorderLayout());
		winPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));

		JLabel winLabel = new JLabel("<HTML><strong>Max Connect-Four</strong><br>" +
				"<pre>Programmers:          Deepak Rohan Sekar<br>" +
				"                      Ganesh Ramamoorthy<br><br></pre></HTML>");
		

		frameMainWindow.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frameCredits.setVisible(false);
			}
		});

		winPanel.add(winLabel);
		frameCredits.getContentPane().add(winPanel, BorderLayout.CENTER);
		frameCredits.setVisible(true);
	}
	/**
	 * 
	 */
	private static void showDisplay() {
		frameCredits = new JFrame("Display");
		frameCredits.setBounds(300, 300, 680, 320);
		JPanel winPanel = new JPanel(new BorderLayout());
		winPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));

		JLabel winLabel = new JLabel("<HTML><strong>How To Play</strong><br>" +
				"<pre> Red and Green players randomly drop discs by clicking on  <br>" +
				" the number of the columnand the disc fill the column. The player <br>" +
				" who creates the maximum number of quadraples in four consecutive positions<br>" +
				" on board, either in the horizontal, vertical, or each of the<br>" +
				" two diagonal directions wins the game. You can select the players from  <br>" +
				" the Players menu. Red starts first followed by Green. Scores and last <br>" +
				" moves are updated at the bottom in status bar.<br></pre></HTML>");
		frameMainWindow.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frameCredits.setVisible(false);
			}
		});

		winPanel.add(winLabel);
		frameCredits.getContentPane().add(winPanel, BorderLayout.CENTER);
		frameCredits.setVisible(true);
	}
	/**
	 * 
	 */
	public static void errorColumn() {
		frameErr = new JFrame("Error !!");
		frameErr.setBounds(300, 300, 220, 120);
		JPanel winPanel = new JPanel(new BorderLayout());
		winPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
		JLabel winLabel;
		winLabel = new JLabel("Move Invalid!!");
		winPanel.add(winLabel);
		winPanel.add(winLabel, BorderLayout.NORTH);
		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameErr.setVisible(false);
			}
		});
		winPanel.add(okButton, BorderLayout.SOUTH);
		frameErr.getContentPane().add(winPanel, BorderLayout.CENTER);
		frameErr.setVisible(true);
	}
	/**
	 * 
	 */
	private static void showReports() {
		JFrame f = new JFrame("Reports");
		JTextArea textArea = new JTextArea();
		Document doc = textArea.getDocument();
		PrintStream out = new PrintStream(new DocumentOutputStream(doc));
		out.println("Current Board View");
		out.println(board);
		out.println("Score : "+red+"-"+green+" (red first)");
		out.println("Last Move : "+playerColor);
		out.println("All the steps and final results can be viewed in answer file");
		WindowListener l = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				f.setVisible(false);
			}
		};

		f.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
		f.setSize(640, 480);
		f.setVisible(true);
	}
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Set look and feel to the java look
		try {
			/* try {
			PrintStream out = new PrintStream(new FileOutputStream("C:/DELL/answer3.txt"));
			System.setOut(out);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		} catch (Exception e) { }

		createNewGame();
	}
}
