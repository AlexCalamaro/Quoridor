//Author: Richard Lin

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * GamePanel.java
 * 
 * An GamePanel draws and steps the entire image. It takes in information about
 * from the QuoridorBoard and displays it on the screen. It is also in charge 
 * of player movement functionality.
 */
public class GamePanel extends JPanel implements MouseListener {

	private Image current;
	private Image redTurn;
	private Image blueTurn;
	private Color currentTurn;
	private boolean playerControl;
	public boolean gameOver;
	public Locus[][] wallLoci;
	public Locus[][] squareLoci;
	public int numWallsRed;
	public int numWallsBlue;
	public GameObject[] gameObjects;
	public String playerMode;
	private QuoridorBoard gameBoard;

	final private int SQUARE_EDGE = 36;

	// Instantiation
	public GamePanel(String scenario) throws FileNotFoundException {
		super();

		gameOver = false;
		currentTurn = Color.RED;

		try {
			redTurn = ImageIO.read(new File("images/redTurnSmall.png"));
			blueTurn = ImageIO.read(new File("images/blueTurnSmall.png"));
		} catch (IOException e) {
		}

		AI[] evolvedAIs = getAIPlayers();
		
		if (scenario.equals("PvA")){
			gameBoard = new QuoridorBoard(evolvedAIs[0], evolvedAIs[1]);
			playerControl = true;
		}
		else{
			gameBoard = new QuoridorBoard(evolvedAIs[0], evolvedAIs[1]);
			playerControl = false;
		}
		playerMode = "";
		current = redTurn;
		instantiateBoard();
	}

	// creates all game objects and locuses
	public void instantiateBoard() {

		gameObjects = new GameObject[18];

		gameObjects[0] = new GameObject("RedPawn", 169, 230);
		gameObjects[1] = new GameObject("BluePawn", 169, 512);

		numWallsRed = 8;
		numWallsBlue = 8;

		for (int i = 2; i < 18; i += 2) {
			gameObjects[i] = new GameObject("RedWall-V", 374, 372);
			gameObjects[i + 1] = new GameObject("BlueWall-V", 374, 482);
		}

		wallLoci = new Locus[8][8];

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++)
				wallLoci[i][j] = new Locus("Wall", 56 + SQUARE_EDGE * i
						+ (5 - i) / 2, 258 + SQUARE_EDGE * j + (5 - j) / 2);
		}

		squareLoci = new Locus[9][9];

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++)
				squareLoci[i][j] = new Locus("Square", 38 + SQUARE_EDGE * i
						+ (5 - i) / 2, 240 + SQUARE_EDGE * j + (5 - j) / 2);
		}
	}

	// Redraws the screen upon selecting something
	public void redraw() {

		if (!playerControl) {
			if (currentTurn.equals(Color.RED))
				current = redTurn;
			else if (currentTurn.equals(Color.BLUE))
				current = blueTurn;
		} else {
			if (currentTurn.equals(Color.BLUE))
				current = blueTurn;
			if (currentTurn.equals(Color.RED)) {
				if (playerMode.equals(""))
					current = redTurn;
				else {
					String cur;
					cur = "images/redTurn-" + playerMode + "Small.png";

					try {
						current = ImageIO.read(new File(cur));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		repaint();
	}

	// Steps and animates all objects on the screen
	public void step() {
		if(gameBoard.getWinner() != null)
			gameOver = true;
		
		if (currentTurn.equals(Color.RED) && !playerControl) {
			Move m = gameBoard.getNextMove();
			makeAIMove(currentTurn, m);
			currentTurn = Color.BLUE;
		} else if (currentTurn.equals(Color.BLUE)) {
			Move m = gameBoard.getNextMove();
			makeAIMove(currentTurn, m);
			currentTurn = Color.RED;
		} 
		
		repaint();
	}

	// Modified paint method that draws background images and all GameObjects
	public void paint(Graphics g) { // modified to draw the menu image
		super.paint(g);
		g.drawImage(current, 0, 0, null);

		g.setFont(new Font("Tahoma", Font.BOLD, 30));
		g.setColor(new Color(255, 255, 255));
		g.drawString(Integer.toString(numWallsRed), 425, 405);

		g.setFont(new Font("Tahoma", Font.BOLD, 30));
		g.setColor(new Color(255, 255, 255));
		g.drawString(Integer.toString(numWallsBlue), 425, 515);

		for (GameObject o : gameObjects)
			o.paint(g);
	}

	// makes an AI player move
	public void makeAIMove(Color player, Move m) {

		if (!m.isVertical() && m.isWallMove()) {
			if (player.equals(Color.RED)) {
				GameObject wall = new GameObject("RedWall-H", 0, 0);
				wall.moveTo(wallLoci[m.to.x - 1][m.to.y - 1]);
				gameObjects[2 + (8 - numWallsRed)] = wall;
				numWallsRed--;
			} else {
				GameObject wall = new GameObject("BlueWall-H", 0, 0);
				wall.moveTo(wallLoci[m.to.x - 1][m.to.y - 1]);
				gameObjects[10 + (8 - numWallsBlue)] = wall;
				numWallsBlue--;
			}
		} else if (m.isVertical() && m.isWallMove()) {
			if (player.equals(Color.RED)) {
				GameObject wall = new GameObject("RedWall-V", 0, 0);
				gameObjects[2 + (8 - numWallsRed)] = wall;
				wall.moveTo(wallLoci[m.to.x - 1][m.to.y - 1]);
				numWallsRed--;
			} else {
				GameObject wall = new GameObject("BlueWall-V", 0, 0);
				gameObjects[10 + (8 - numWallsBlue)] = wall;
				wall.moveTo(wallLoci[m.to.x - 1][m.to.y - 1]);
				numWallsBlue--;
			}
		} else if (!m.isWallMove()) {
			if (player.equals(Color.RED))
				gameObjects[0].moveTo(squareLoci[m.to.x][m.to.y]);
			else
				gameObjects[1].moveTo(squareLoci[m.to.x][m.to.y]);
		}
	}

	
	// check mouse click if PvA
	public void mouseClicked(MouseEvent e) {
		if (playerControl && currentTurn.equals(Color.RED)) {

			int x = e.getX();
			int y = e.getY();

			int[] wallCheck = checkWallLoci(x, y);
			int[] squareCheck = checkSquareLoci(x, y);

			if (wallCheck[0] != -1 && numWallsRed > 0) {
				if (playerMode.equals("horizontal")) {
					Move playerMove = new Move(true, false, null, new Point(wallCheck[0] + 1, wallCheck[1] + 1));

					if (gameBoard.isValidMove(playerMove)) {
						GameObject wall = new GameObject("RedWall-H", 0, 0);
						wall.moveTo(wallLoci[wallCheck[0]][wallCheck[1]]);
						gameObjects[2 + (8 - numWallsRed)] = wall;
						numWallsRed--;
						currentTurn = Color.BLUE;
						gameBoard.makeMove(playerMove);
					} else
						playerMove = null;
				} else if (playerMode.equals("vertical")) {
					Move playerMove = new Move(true, true, null, new Point(wallCheck[0] + 1, wallCheck[1] + 1));

					if (gameBoard.isValidMove(playerMove)) {
						GameObject wall = new GameObject("RedWall-V", 0, 0);
						wall.moveTo(wallLoci[wallCheck[0]][wallCheck[1]]);
						gameObjects[2 + (8 - numWallsRed)] = wall;
						numWallsRed--;
						currentTurn = Color.BLUE;
						gameBoard.makeMove(playerMove);
					} else
						playerMove = null;
				} 
			} else if (squareCheck[0] != -1 && playerMode.equals("move")) {
				Move playerMove = new Move(false, false, gameBoard.p1Pos, new Point(squareCheck[0], squareCheck[1]));
				if (gameBoard.isValidMove(playerMove)) {
					gameObjects[0].moveTo(squareLoci[squareCheck[0]][squareCheck[1]]);
					currentTurn = Color.BLUE;
					gameBoard.makeMove(playerMove);
				} else
					playerMove = null;
			}else if (horizontalButton(x, y))
				playerMode = "horizontal";
			else if (verticalButton(x, y))
				playerMode = "vertical";
			else if (moveButton(x, y))
				playerMode = "move";
			else
				playerMode = "";

			redraw();
		}
	}

	// returns coordinate of a found wall locus
	public int[] checkWallLoci(int x, int y) {

		int[] ret = { -1, -1 };

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (wallLoci[i][j].clickedOn(x, y)) {
					ret[0] = i;
					ret[1] = j;
				}
			}
		}
		return ret;
	}

	// returns coordinates of a found square locus
	public int[] checkSquareLoci(int x, int y) {

		int[] ret = { -1, -1 };

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (squareLoci[i][j].clickedOn(x, y)) {
					ret[0] = i;
					ret[1] = j;
				}
			}
		}
		return ret;
	}

	// reads in the weights from the generated evolution text file
	public AI[] getAIPlayers() throws FileNotFoundException{
		
		FileReader reader = new FileReader("evoOutput.txt");
		BufferedReader bReader = new BufferedReader(reader);
		Scanner in = new Scanner(bReader);
		
		int[] weights = new int[10];
		int i = 0;
		
		while(in.hasNextInt()){
			weights[i] = in.nextInt();
			i++;
		
		}
		
		return new AI[]{new AI(weights[0],weights[1],weights[2],weights[3],weights[4]), new AI(weights[5],weights[6],weights[7],weights[8],weights[9])};
		
	}
	
	
	// returns if given mouse coordinates match with horizontal button
	private boolean horizontalButton(int x, int y) {
		return ((365 < x) && (x < 450) && (250 < y) && (y < 270));
	}

	// returns if given mouse coordinates match with horizontal button
	private boolean verticalButton(int x, int y) {
		return ((365 < x) && (x < 450) && (270 < y) && (y < 290));
	}

	// returns if given mouse coordinates match with horizontal button
	private boolean moveButton(int x, int y) {
		return ((365 < x) && (x < 450) && (290 < y) && (y < 310));
	}

	// unused mouseListener methods
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}
