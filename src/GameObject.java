//Author: Richard Lin

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * GameObject.java
 * 
 * A simple object which is either a pawn or wall. It can be moved to a locus, and will be called on to paint itself.
 */

public class GameObject {

	private Image objectImage;
	private Image redPawn;
	private Image bluePawn;
	private Image redWallH;
	private Image blueWallH;
	private Image redWallV;
	private Image blueWallV;
	
	private int x;
	private int y;
	
	// instantiates a GameObject given a type and location
	public GameObject(String type, int locX, int locY)
    {
        try {
            redPawn = ImageIO.read(new File("images/redPawnSmall.png"));
            redWallH = ImageIO.read(new File("images/redWallHorizontalSmall.png"));
            redWallV = ImageIO.read(new File("images/redWallVerticalSmall.png"));
            bluePawn = ImageIO.read(new File("images/bluePawnSmall.png"));
            blueWallH = ImageIO.read(new File("images/blueWallHorizontalSmall.png"));
            blueWallV = ImageIO.read(new File("images/blueWallVerticalSmall.png"));
        	} 
        catch (IOException e) {}	

        	x = locX;
        	y = locY;
        	
            if(type.equals("RedPawn"))
            	objectImage = redPawn;
            else if(type.equals("RedWall-H"))
            	objectImage = redWallH;
            else if(type.equals("RedWall-V"))
            	objectImage = redWallV;
            else if(type.equals("BluePawn"))
            	objectImage = bluePawn;
            else if(type.equals("BlueWall-H"))
            	objectImage = blueWallH;
            else if(type.equals("BlueWall-V"))
            	objectImage = blueWallV;
            else
            	System.out.println(type);
    }
	
	// displays the gameObject on the gamePanel
	public void paint(Graphics g){	g.drawImage(objectImage,x,y,null);	}
	
	// manual input movement. UNUSED
	public void move(int dx, int dy){
		x += dx;
		y += dy;
	}
	
	// moves to a locus
	public void moveTo(Locus l){
		x = l.getX()-objectImage.getWidth(null)/2;
		y = l.getY()-objectImage.getHeight(null)/2;
	}
	
}
