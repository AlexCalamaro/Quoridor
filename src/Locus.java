//Author: Richard Lin

/**
 * Locus.java
 * 
 * A location class for the GamePanel where a wall or square is located. Is used to represent 
 * placement locations as a coordinate grid via arrays.
 */

public class Locus {

	private int x;
	private int y;
	private int radius;
	
	// instantiates a locus with type and coordinates
	public Locus(String type, int centerX, int centerY){
		x = centerX;
		y = centerY;
		if(type.equals("Wall"))
			radius = 5;
		else if(type.equals("Square"))
			radius = 12;		
	}
	
	// returns if a locus has been clicked on
	public boolean clickedOn(int clickX, int clickY){
		return ((x-radius<clickX)&&(clickX<x+radius)&&(y-radius<clickY)&&(clickY<y+radius));
	}	
	
	// return coordinates
	public int getX() { return x; }
	public int getY() { return y; }
}
