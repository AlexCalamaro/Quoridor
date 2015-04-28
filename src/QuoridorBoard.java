//Author: Aidan Carroll

import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

public class QuoridorBoard implements Board {

	Space[][] board;
	//Player 1 and 2
	AI p1, p2;
	
	//number of walls the players have left
	int p1walls, p2walls;
	
	//the positions of the 2 players
	Point p1Pos, p2Pos;
	
	//stores the last move so that it can be undone
	Move lastMove;
	
	//stores whose turn it is
	boolean isP1Turn;
	
	//used to track the number of moves in a game
	//if this gets too big call it a draw.
	int numSteps = 0;
	
	//the Winner
	//we only use this to solve a rare edge case normally the winner is checked based on the pos of the two players
	AI winner;

	
	//initializes the board and default values
	public QuoridorBoard(AI player1, AI player2) {
		board = new Space[9][9];
		p1 = player1;
		p2 = player2;
		isP1Turn = true;
		p1walls = 8;
		p2walls = 8;
		p1Pos = new Point(4, 0);
		p2Pos = new Point(4, 8);
		
		//create all the spaces for the board and adds walls for the edges
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				board[i][j] = new Space();
				if (i == 0) {
					board[i][j].addWall(Direction.WEST);
				}
				if (i == 8) {
					board[i][j].addWall(Direction.EAST);
				}
				if (j == 0) {
					board[i][j].addWall(Direction.NORTH);
				}
				if (j == 8) {
					board[i][j].addWall(Direction.SOUTH);
				}

			}
		}

		board[4][0].setPlayer(true);
		board[4][8].setPlayer(true);
	}

	public int getMyWalls(AI p) {
		if (p == p1) {
			return p1walls;
		} else {
			return p2walls;
		}
	}
	
	//Takes a turn for the player whose turn it is
	public Move getNextMove() {
		Move m;
		if (isP1Turn) {
			m = p1.getMove(this);
			makeMove(m);
			return m;
		}
		m = p2.getMove(this);
		makeMove(m);
		return m;

	}

	public int getOpponentsWalls(AI p) {
		if (p == p1) {
			return p2walls;
		} else {
			return p1walls;
		}
	}

	public boolean hasWon(AI p) {
		if (p == p1) {
			return p1Pos.getY() == 8;
		}
		if (p == p2) {
			return p2Pos.getY() == 0;
		}
		return false;

	}
	
	//because it must alternate turns the player doesn't matter
	public void makeMove(AI p, Move m){
		makeMove(m);
	}

	
	//makes a move
	public void makeMove(Move m) {
		
		//this is the rare edge case of a player not having a move )happens VERY rarely)
		if(m == null){
			if(isP1Turn){
				winner = p2;
			}else{
				winner = p1;
			}
			return;
		}
		numSteps++;
		lastMove = m;
		
		//if it is not a wall move
		if (!m.isWallMove()) {
			//if it is player 1's turn update thir positoin
			if (isP1Turn) {
				board[(int) p1Pos.getX()][(int) p1Pos.getY()].setPlayer(false);
				p1Pos = m.getDestination();
				board[(int) p1Pos.getX()][(int) p1Pos.getY()].setPlayer(true);
			}
			//if it is player 2's turn update thir positoin
			else{
				board[(int) p2Pos.getX()][(int) p2Pos.getY()].setPlayer(false);
				p2Pos = m.getDestination();
				board[(int) p2Pos.getX()][(int) p2Pos.getY()].setPlayer(true);
			}
			
			//if it is a wall move
		}else{
		
			int	x = (int) m.getDestination().getX();
			int	y = (int) m.getDestination().getY();
		
			//adds walls apropriatly
			if(m.isVertical()){
				board[x][y].addWall(Direction.WEST);
				board[x][y - 1].addWall(Direction.WEST);
				board[x-1][y].addWall(Direction.EAST);
				board[x-1][y - 1].addWall(Direction.EAST);
			}else{
				board[x][y].addWall(Direction.NORTH);
				board[x - 1][y].addWall(Direction.NORTH);
				board[x][y-1].addWall(Direction.SOUTH);
				board[x - 1][y-1].addWall(Direction.SOUTH);
			}
			if(isP1Turn){
				p1walls--;

			}else{
				p2walls--;

			}
		}
		
		//switches whose turn it is
		isP1Turn = !isP1Turn;
	}

	//checks if the number of squares to a wall in a given direction is odd
	public boolean oddDistance(AI p, Direction dir) {
		return distanceFromWall(p, dir) % 2 == 1;
	}

	//gets the shortest path distance to the end useing bfs
	public int getMyEndDistance(AI p) {
		int x;
		int y;
		if (p == p1) {
			x = (int) p1Pos.getX();
			y = (int) p1Pos.getY();
		} else {
			x = (int) p2Pos.getX();
			y = (int) p2Pos.getY();
		}
		Stack<Point> s = new Stack<Point>();
		s.push(new Point(x, y));
		return bfs(0, p, s);
	}

	//gets the shortest path distance of the opponent
	public int getOpponentsEndDistance(AI p) {
		if (p == p1) {
			return getMyEndDistance(p2);
		} else {
			return getMyEndDistance(p1);
		}
	}

	//bfs to find how far to the opponents back line
	//returns -1 if it cannot get to the back row
	private int bfs(int length, AI p, Stack<Point> stack) {
		if(stack.empty()){
			unvisitAll();
			return -1;
		}
		
		ArrayList<Point> toAdd = new ArrayList<Point>();
			
		while(!stack.empty()){
			Point temp = stack.pop();
			if(p == p1 && temp.getY() == 8){
				unvisitAll();
				return length;
			}if(p == p2 && temp.getY() == 0){
				unvisitAll();
				return length;
			}
			
			int x;
			int y;
			x = (int) temp.getX();
			y = (int) temp.getY();
			
			board[x][y].visit();
			if(!board[x][y].hasWall(Direction.NORTH) && !board[x][y-1].wasVisited()){
				toAdd.add(new Point(x, y - 1));
			}
			if(!board[x][y].hasWall(Direction.SOUTH) && !board[x][y+1].wasVisited()){
				toAdd.add(new Point(x, y + 1));
			}
			if(!board[x][y].hasWall(Direction.EAST) && !board[x+1][y].wasVisited()){
				toAdd.add(new Point(x + 1, y));
			}
			if(!board[x][y].hasWall(Direction.WEST) && !board[x-1][y].wasVisited()){
				toAdd.add(new Point(x - 1, y));
			}
			
		}
		
		for(Point t: toAdd){
			stack.push(t);
		}
		
		return bfs(length + 1, p, stack);
		
		
	}
	
	//undoes the tracking that bfs needs to work
	private void unvisitAll(){
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				board[i][j].unvisit();
			}
		}
	}

	//returns a list of all legal moves for a player
	public ArrayList<Move> getValidMoves(AI p) {
		ArrayList<Move> moves = new ArrayList<Move>();

		int x;
		int y;
		boolean hasWalls;
		if (p == p1) {
			x = (int) p1Pos.getX();
			y = (int) p1Pos.getY();
			hasWalls = (p1walls > 0);

		} else {
			x = (int) p2Pos.getX();
			y = (int) p2Pos.getY();
			hasWalls = (p2walls > 0);
		}


		//gets all movement moves
		if (!board[x][y].hasWall(Direction.NORTH)) {
			if(y > 0 && board[x][y-1].hasPlayer()){
				if(!board[x][y-1].hasWall(Direction.NORTH)){
					moves.add(new Move(new Point(x, y), new Point(x, y - 2)));
				}
			}else{
				moves.add(new Move(new Point(x, y), new Point(x, y - 1)));
			}
		}
		if (!board[x][y].hasWall(Direction.SOUTH)) {
			if(y < 8 && board[x][y+1].hasPlayer()){
				if(!board[x][y+1].hasWall(Direction.SOUTH)){
					moves.add(new Move(new Point(x, y), new Point(x, y + 2)));
				}
			}else{
				moves.add(new Move(new Point(x, y), new Point(x, y + 1)));
			}
		}
		if (!board[x][y].hasWall(Direction.EAST)) {
			if(x < 8 && board[x+1][y].hasPlayer()){
				if(!board[x+1][y].hasWall(Direction.EAST)){
					moves.add(new Move(new Point(x, y), new Point(x + 2, y)));
				}
			}else{
				moves.add(new Move(new Point(x, y), new Point(x + 1, y)));
			}
		}
		if (!board[x][y].hasWall(Direction.WEST)) {
			if(x > 0 && board[x-1][y].hasPlayer()){
				if(!board[x-1][y].hasWall(Direction.WEST)){
					moves.add(new Move(new Point(x, y), new Point(x - 2, y)));
				}
			}else{
				moves.add(new Move(new Point(x, y), new Point(x - 1, y)));
			}
		}

		//if it has no walls left break
		if (!hasWalls) {
			return moves;
		}

		//adds all legal wall moves
		ArrayList<Move> temp = getValidWallMoves();
		for (Move m : temp) {
			moves.add(m);
		}

		return moves;
	}

	//gets the legal wall moves
	public ArrayList<Move> getValidWallMoves() {
		
		ArrayList<Move> moves = new ArrayList<Move>();

		//checks all possible places to put a wall and checks if it is valid
		for (int i = 1; i < 8; i++) {
			for (int j = 1; j < 8; j++) {
				Move m = new Move(true, false, new Point(i, j), new Point(i, j));
				if (isValidMove(m)) {
					moves.add(m);
				}
				m = new Move(true, true, new Point(i, j), new Point(i, j));
				if (isValidMove(m)) {
					moves.add(m);
				}

			}
		}

		return moves;

	}

	//preforms bfs to make sure a player can get to the end
	public boolean canWin(AI p) {
		Stack<Point> s = new Stack<Point>();
		if (p == p1) {
			s.push(new Point(p1Pos));
		} else {
			s.push(new Point(p2Pos));
		}
		return bfs(0, p, s) != -1;
	}

	//undoes a move
	public void undoMove() {
		//moves a player back if the made a movement move
		if (!lastMove.isWallMove()) {
			if (!isP1Turn) {
				board[(int) p1Pos.getX()][(int) p1Pos.getY()].setPlayer(false);
				p1Pos = lastMove.getOrigin();
				board[(int) p1Pos.getX()][(int) p1Pos.getY()].setPlayer(true);
			}
			else{
				board[(int) p2Pos.getX()][(int) p2Pos.getY()].setPlayer(false);
				p2Pos = lastMove.getOrigin();
				board[(int) p2Pos.getX()][(int) p2Pos.getY()].setPlayer(true);
			}
			
			//removes the wall if they made a wall move
		}else{
		
			int	x = (int) lastMove.getDestination().getX();
			int	y = (int) lastMove.getDestination().getY();
		
			if(lastMove.isVertical()){
				board[x][y].removeWall(Direction.WEST);
				board[x][y - 1].removeWall(Direction.WEST);
				board[x-1][y].removeWall(Direction.EAST);
				board[x-1][y - 1].removeWall(Direction.EAST);
			}else{
				board[x][y].removeWall(Direction.NORTH);
				board[x - 1][y].removeWall(Direction.NORTH);
				board[x][y-1].removeWall(Direction.SOUTH);
				board[x - 1][y-1].removeWall(Direction.SOUTH);
			}
			if (isP1Turn) {
				p2walls++;
			}else{
				p1walls++;
			}
		}
		
		//toggles whose turn it is
		isP1Turn = !isP1Turn;
	}

	//checks if a move is valid
	public boolean isValidMove(Move m) {
		//all movement moves that are ever generated are valid
		if (!m.isWallMove()){
			return true;
		}
		//cannot place a wall in these spots because of our convention of wall placement (see move class)
		if (m.getDestination().getX() == 0 || m.getDestination().getY() == 0) {
			return false;
		}

		int x = (int) m.getDestination().getX();
		int y = (int) m.getDestination().getY();
		
		Move temp = lastMove;

		//makes sure 2 walls do not overlap/intersect
		if (m.isVertical()) {
			if (!board[x][y].hasWall(Direction.WEST)
					&& !board[x][y - 1].hasWall(Direction.WEST) && 
					(!board[x][y].hasWall(Direction.NORTH) && !board[x - 1][y].hasWall(Direction.NORTH))) {
				
				makeMove(m);
				
				//makes sure neither player is prevented from winning
				if (canWin(p1) && canWin(p2)) {
					undoMove();
					lastMove = temp;
					return true;
				}
				undoMove();
				lastMove = temp;
				return false;
			}
			return false;
		}
		
		
		//does the same thjing as above but with horizontal moves
		if (!board[x][y].hasWall(Direction.NORTH)
				&& !board[x - 1][y].hasWall(Direction.NORTH) &&
				(!board[x][y].hasWall(Direction.WEST)
					&& !board[x][y - 1].hasWall(Direction.WEST))) {
			makeMove(m);
			if (canWin(p1) && canWin(p2)) {
				undoMove();
				lastMove = temp;
				return true;
			}
			undoMove();
			lastMove = temp;
			return false;
		}
		
		//default return false
		return false;
	}

	//allows AIs to orient themselves on the board
	public Direction getDirectionOfMove(AI p, Move m) {
		int x, y;
		if (p == p1) {
			x = (int) p1Pos.getX();
			y = (int) p1Pos.getY();

		} else {
			x = (int) p2Pos.getX();
			y = (int) p2Pos.getY();
		}

		if (m.getDestination().getX() < x) {
			return Direction.WEST;
		} else if (m.getDestination().getX() > x) {
			return Direction.EAST;
		} else if (m.getDestination().getY() < y) {
			return Direction.NORTH;
		}

		return Direction.SOUTH;

	}

	//returns the distance away from a wall in a given direction
	public int distanceFromWall(AI p, Direction dir) {
		int x;
		int y;
		if (p == p1) {
			x = (int) p1Pos.getX();
			y = (int) p1Pos.getY();
		} else {
			x = (int) p2Pos.getX();
			y = (int) p2Pos.getY();
		}

		int dis = 0;
		while (!board[x][y].hasWall(dir)) {
			dis++;
			switch (dir) {
			case NORTH:
				y--;
				break;
			case SOUTH:
				y++;
				break;
			case EAST:
				x++;
				break;
			case WEST:
				x--;
				break;
			default:
				break;

			}
		}

		return dis;
	}
	
	//returns the winner
	//null if there is no winner
	public AI getWinner(){
		if(hasWon(p1)){
			return p1;
		}
		if(hasWon(p2)){
			return p2;
		}
		return winner;
	}
	
	//returns the loser
	//null if there is no loser
	public AI getLoser(){
		if(hasWon(p1)){
			return p2;
		}
		if(hasWon(p2)){
			return p1;
		}
		if(winner == p1){
			return p2;
		}if(winner == p2){
			return p1;
		}
			return null;
	}
	
	//plays a game
	public void play(){
		while(getWinner() == null){
			getNextMove();
			if(numSteps >= 5000){
				break;
			}
		}
	}
	
	//test main
	//plays one game and prints the winnner
	public static void main(String[] args){
		QuoridorBoard b = new QuoridorBoard(new AI(), new AI());
		b.play();
		System.out.println(b.getWinner());
	}

}
