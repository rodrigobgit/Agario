package client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JComponent;


import utils.*;

/**
 * Creates a JComponent to display the game state.
 * At the same time, this is also a KeyListener for itself: when a key is pressed,
 * attribute lastPressedDirection is updated accordingly. This feature is a demo to
 * better understand how to deal with keys pressed, useful for the remote client.
 * This feature is not helpful for the main application and should be ignored.
 * This class does not need to be edited.
 * @author luismota
 *
 */
public class BoardJComponent extends JComponent implements KeyListener {
	private Game game;
	private Image obstacleImage = new ImageIcon("obstacle.png").getImage();
	private Image humanPlayerImage= new ImageIcon("abstract-user-flat.png").getImage();
	private String lastPressedDirection=null;
	private boolean useArrows;
	
	public BoardJComponent(Game game,boolean useArrows) {
		this.game = game;
		this.useArrows=useArrows;
		setFocusable(true);
		addKeyListener(this);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		double cellHeight=getHeight()/(double)Game.DIMY;
		double cellWidth=getWidth()/(double)Game.DIMX;

		for (int y = 1; y < Game.DIMY; y++) {
			g.drawLine(0, (int)(y * cellHeight), getWidth(), (int)(y* cellHeight));
		}
		for (int x = 1; x < Game.DIMX; x++) {
			g.drawLine( (int)(x * cellWidth),0, (int)(x* cellWidth), getHeight());
		}		
			
				Message msg=game.getMsg();
				if(msg!=null) {
				ArrayList<Data> data=msg.getInfo();
				for ( Data dt: data) {					
					if(dt.getStrength()==0) {
						g.setColor(Color.YELLOW);
						g.fillRect((int)(dt.getxPos()* cellWidth), 
								(int)(dt.getyPos() * cellHeight),
								(int)(cellWidth),(int)(cellHeight));
						g.drawImage(obstacleImage, (int)(dt.getxPos() * cellWidth), (int)(dt.getyPos()*cellHeight), 
								(int)(cellWidth),(int)(cellHeight), null);
						// if player is dead, don'd draw anything else?
						continue;
					}
					// Fill green if it is a human player
					if(dt.isHuman()) {
						g.setColor(Color.GREEN);
						g.fillRect((int)(dt.getxPos()* cellWidth), 
								(int)(dt.getyPos() * cellHeight),
								(int)(cellWidth),(int)(cellHeight));
						// Custom icon?
						g.drawImage(humanPlayerImage, (int)(dt.getxPos() * cellWidth), (int)(dt.getyPos()*cellHeight), 
								(int)(cellWidth),(int)(cellHeight), null);
						
					}					
					g.setColor(new Color(0,0,0));
					((Graphics2D) g).setStroke(new BasicStroke(5));
					Font font = g.getFont().deriveFont( (float)cellHeight);
					g.setFont( font );
					String strengthMarking=(dt.getStrength()==10?"X":""+dt.getStrength());
					g.drawString(strengthMarking,
							(int) ((dt.getxPos() + .2) * cellWidth),
							(int) ((dt.getyPos() + .9) * cellHeight));
				}

			}
	}
	

	@Override
	public void keyPressed(KeyEvent e) {
		if(!useArrows) {
			switch(e.getKeyCode()){
			case KeyEvent.VK_W:
				lastPressedDirection="UP";
				break;
			case KeyEvent.VK_A:
				lastPressedDirection="LEFT";			
				break;
			case KeyEvent.VK_S:
				lastPressedDirection="DOWN";
				break;
			case KeyEvent.VK_D:
				lastPressedDirection="RIGHT";
				break;
			}
		}
		else {
			switch(e.getKeyCode()){
			case KeyEvent.VK_LEFT :
				lastPressedDirection="LEFT";			
				break;
			case KeyEvent.VK_RIGHT:
				lastPressedDirection="RIGHT";
				break;
			case KeyEvent.VK_UP:
				lastPressedDirection="UP";
				break;
			case KeyEvent.VK_DOWN:
				lastPressedDirection="DOWN";
				break;
		}
	}
	}


	@Override
	public void keyReleased(KeyEvent e) {
		//ignore
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Ignored...
	}

	public String getLastPressedDirection() {
		return lastPressedDirection;
	}

	public void clearLastPressedDirection() {
		lastPressedDirection=null;
	}
}
