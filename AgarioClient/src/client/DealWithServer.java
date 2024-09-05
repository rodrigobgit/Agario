package client;

import java.awt.event.KeyEvent;
import java.io.*;
import java.net.*;
import utils.Message;

public class DealWithServer extends Thread{
	
	private Game game;
	private ObjectInputStream in;
	private PrintWriter out;
	private Socket socket;	
	private GameGuiMain gui;
	private boolean isGameOver=false;
	private boolean useArrows;
	
	public DealWithServer(Socket socket,boolean useArrows) {
		super();
		this.socket=socket;
		this.useArrows=useArrows;
	}
	
	public void run() {
		try {
			doConnections(socket);
			serve();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void serve() throws ClassNotFoundException, IOException {		
		game=new Game();		
		gui=new GameGuiMain(game,useArrows);		
		gui.init();
		KeyEvent e = new KeyEvent(gui.getBoardGui(), 1, 20, 1, 10, 'a');
		while(!isGameOver()){
			Message msg=(Message)in.readObject();
			if(msg.isOver==0) {
				game.setMsg(msg);
				game.notifyChange();			
				gui.getBoardGui().keyPressed(e);	
				out.println(gui.getBoardGui().getLastPressedDirection());
				gui.getBoardGui().clearLastPressedDirection();
			}
			else {
				isGameOver=true;
				in.close();
				out.close();
			}
		}
	}
		
	private void doConnections(Socket socket) throws IOException {
		in = new ObjectInputStream ( socket.getInputStream ());
		out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream())), true);
	}
	
	public boolean isGameOver() {
		return isGameOver;
	}
}