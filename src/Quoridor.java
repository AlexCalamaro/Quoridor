//Author Richard Lin
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.*;

public class Quoridor {

	public static void main(String[] args) throws IOException{
		int width = 500;
		int height = 600;

		MenuPanel menuFrame = new MenuPanel();
		menuFrame.setPreferredSize(new Dimension(width, height));
		menuFrame.setFocusable(true);
		menuFrame.addMouseListener(menuFrame);

		JFrame worldFrame = new JFrame("Quoridor");
		worldFrame.add(menuFrame);
        worldFrame.setContentPane(menuFrame);
        worldFrame.pack();
        worldFrame.setVisible(true);
        worldFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
			System.exit(0);
			}});

        while(menuFrame.inMenu){
        	 try {
                 Thread.sleep(20);
             } catch (Exception e1) {
             }
        }
        
        String scenario = menuFrame.getState();
        
        GamePanel gameFrame = new GamePanel(scenario);
        gameFrame.setPreferredSize(new Dimension(width, height));
		gameFrame.setFocusable(true);
		gameFrame.addMouseListener(gameFrame);

        worldFrame.setContentPane(gameFrame);
        worldFrame.pack();
        worldFrame.setVisible(true);
        menuFrame.transferFocus();
        gameFrame.requestFocus();
        menuFrame = null;
        
        while(true){
	        while (!gameFrame.gameOver) {
	            try {
	                Thread.sleep(200);
	            } catch (Exception e1) {
	            }
	
	            gameFrame.step();
	        }

	        while(gameFrame.gameOver){
	        
	        	try {
	                Thread.sleep(20);
	            } catch (Exception e1) {
	            }
	        }   
        }
    }
}