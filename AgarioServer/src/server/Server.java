package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import game.Game;

public class Server {
	public static final int PORT = 8080;
	private Game game;
	private int newPlayerID=Game.NUM_BOT_PLAYERS-1;
	
	public void startServing() throws IOException {
		System.out.println("Server started");
		ServerSocket ss = new ServerSocket(PORT);
		game=new Game();
		game.go();
		try {
			while (!game.isEndOfGame()) { 
				Socket socket = ss.accept(); 
				DealWithClient dwc = new DealWithClient(socket,this); 		
				dwc.start();
			}
		} finally {
			ss.close();
		}
	}

	public Game getGame() {
		return game;
	}

	public synchronized int getNewPlayerID() {
		newPlayerID++;
		return newPlayerID;
	}

	public static void main(String[] args) {
		try {
			new Server().startServing();
		} catch (IOException e) {}
	}
}
