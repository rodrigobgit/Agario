package server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import game.Game;
import game.HumanPlayer;
import utils.Data;
import utils.Message;

public class DealWithClient extends Thread {

	private Socket socket;
	private BufferedReader  in;
	private ObjectOutputStream out;
	private Server server;
	private Message msg;
	
	public DealWithClient(Socket socket,Server server) {
		super();
		this.socket = socket;
		this.server=server;
	}

	@Override
	public void run() {
		try {
			doConnections(socket);
			serve();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void doConnections(Socket socket) throws IOException {
		out = new ObjectOutputStream (socket.getOutputStream());
		in = new BufferedReader (new InputStreamReader ( socket.getInputStream ()));
	}

	private void serve() throws IOException, InterruptedException {
		Game game=server.getGame();
		int playerID=server.getNewPlayerID();		
		HumanPlayer player=new HumanPlayer(game, playerID, (byte) 5);
		player.start();	
		while (!game.isEndOfGame()) {
			sleep(Game.REFRESH_INTERVAL);
			refreshData(game);					
			out.writeObject(msg);			
			String key=in.readLine();
			if(key!=null) {
				switch(key){
				case "LEFT" :
					player.setNextDirection(environment.Direction.LEFT);				
					break;
				case "UP" :
					player.setNextDirection(environment.Direction.UP);
					break;
				case "DOWN" :
					player.setNextDirection(environment.Direction.DOWN);
					break;
				case "RIGHT" :
					player.setNextDirection(environment.Direction.RIGHT);
					break;
				}
			}
		}		
		endComs();
	}

	public void endComs() throws IOException {
		msg=new Message(null, 1);
		out.writeObject(msg);
		out.close();
		in.close();
	}

	public void refreshData(Game game) {
		ArrayList<Data> info = new ArrayList<>(); 
		msg=new Message(info,0);
		for (int x = 0; x < Game.DIMX; x++) {
			for (int y = 0; y < Game.DIMY; y++) {
				if(game.board[x][y].isOccupied()) {
					int xPos=game.board[x][y].getPosition().getX();
					int yPos=game.board[x][y].getPosition().getY();
					int strength=game.board[x][y].getPlayer().getCurrentStrength();
					boolean isHuman=game.board[x][y].getPlayer().isHumanPlayer();
					Data data=new Data(xPos,yPos,strength,isHuman);
					info.add(data);
				}
			}
		}
	}
}