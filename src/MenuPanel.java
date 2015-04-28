//Author: Richard Lin

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
/**
 * MenuPanel.java
 *
 * MenuPanel is in charge of pre-game menu options. It does this by flipping through various menu
 * images based on mouse click locations in order to avoid default Java GUI (swing) options and colors. 
 *
 */

public class MenuPanel extends JPanel implements MouseListener
{
	private Image current;
	private String state;
	public boolean inMenu;
	JFrame worldFrame;

	//Instantiation
    public MenuPanel() throws IOException
    {
        super();
        state = "";
        inMenu = true;
        current = ImageIO.read(new File("images/menuSmall.png"));
    }
    
    //Redraws the screen upon selecting something
    public void redraw(){
    	String bg = null;
    	
    	if(state.equals(""))
    		bg = "images/menuSmall.png";
    	else
    		bg = "images/menu-" + state + "Small.png";
    	
		try {
			current = ImageIO.read(new File(bg));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		repaint();
    }
    
	//Used to determine scenario of the GamePanel
	public String getState(){ return state; }

	//Modified paint method that draws a background image
	public void paint(Graphics g)
    {	//modified to draw the menu image
    	super.paint(g);
    	g.drawImage(current,0,0,null);    	
    }


	//returns if given mouse coordinates match with Player vs. AI button
	private boolean playerButton(int x, int y){
		return ((12<x)&&(x<270)&&(260<y)&&(y<310));
	}

	//returns if given mouse coordinates match with AI vs. AI button
	private boolean aiButton(int x, int y){
		return ((12<x)&&(x<270)&&(335<y)&&(y<385));
	}
	

	//returns if given mouse coordinates match with the quit button
	private boolean quitButton(int x, int y){
		return ((12<x)&&(x<270)&&(410<y)&&(y<460));
	}

	//returns if given mouse coordinates match with the quit button
	private boolean pvpButton(int x, int y){
		return ((12<x)&&(x<270)&&(485<y)&&(y<535));
	}
	
	// checks mouseEvent
	public void mouseClicked(MouseEvent ev) {

		int x = ev.getX();
		int y = ev.getY();

		if(aiButton(x,y) && state.equals("AvA"))
			inMenu = false;
		else if(aiButton(x,y))
			state = "AvA";
		else if(playerButton(x,y) && state.equals("PvA"))
			inMenu = false;
		else if(playerButton(x,y))
			state = "PvA";
		else if(pvpButton(x,y) && state.equals("PvP"))
			inMenu = false;
		//else if(pvpButton(x,y))
		//	state = "PvP";
		else if(quitButton(x,y) && state.equals("quit"))
			System.exit(1);
		else if(quitButton(x,y))
			state = "quit";
		else
			state = "";
		
		redraw();
	}

	// Unused keyListener methods
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}   
}