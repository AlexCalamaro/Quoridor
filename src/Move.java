//Author: Aidan Carroll

import java.awt.*;
public class Move {

	boolean vert;
	boolean wallMove;
	Point from;
	Point to;
	
	public Move(boolean isWallMove, boolean vertical, Point curLocation, Point destination){
		vert = vertical;
		from = curLocation;
		wallMove = isWallMove;
		to = destination;
	}
	
	public Move(Point curLocation, Point destination){
		vert = false;
		from = curLocation;
		wallMove = false;
		to = destination;
	}
	
	public boolean isVertical(){
		return vert;
	}
	
	public boolean isWallMove(){
		return wallMove;
	}
	
	//loc is the bottom right of the 4 squares it effects
	// in this example it is the x            OO
	//                                        OX
	//
	// if vert = true the the wall would be   O|O
	//										  O|X
	public Point getDestination(){
		return to;
	}
	
	public Point getOrigin(){
		return from;
	}
	
	public String toString(){
		return "" + to.toString();
	}
	
}
