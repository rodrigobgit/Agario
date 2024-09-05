package client;

import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;

public class GameGuiMain implements Observer { 
	private JFrame frame = new JFrame("Agario");
	private BoardJComponent boardGui;
	private Game game;
	private boolean useArrows;

	public GameGuiMain(Game game,boolean useArrows) {
		super();
		this.game=game;
		this.useArrows=useArrows;
		game.addObserver(this);
		buildGui();
		
	}

	private void buildGui() {
		boardGui = new BoardJComponent(game,useArrows);
		frame.add(boardGui);
		frame.setSize(800,800);
		frame.setLocation(0, 0);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void init() {
		frame.setVisible(true);
	}

	@Override
	public void update(Observable o, Object arg) {
		boardGui.repaint();
	}
	public BoardJComponent getBoardGui() {
		return boardGui;
	}
}
