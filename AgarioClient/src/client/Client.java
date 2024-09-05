package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JOptionPane;


public class Client {
	private static final int PORT = 8080;
	private static final String IP = "127.0.0.1";
	private static final String QUESTION = "Do you want to play with arrow keys?";
	private static final String GAME_OVER_MSG = "Game over!";
	private Socket socket;
	private InetAddress ip;
	private int port;
	private boolean useArrows;
	
	public Client(InetAddress ip, int port,boolean useArrows) {
		this.ip=ip;
		this.port=port;
		this.useArrows=useArrows;
		
	}
	
	public void runClient() throws IOException {
		try {
			connectToServer();
			DealWithServer dws=new DealWithServer(socket,useArrows);
			dws.start();
			dws.join();
			socket.close();
			JOptionPane.showMessageDialog(null,GAME_OVER_MSG);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void connectToServer() throws IOException {		
		socket = new Socket(ip, port);
	}

	public boolean isUseArrows() {
		return useArrows;
	}

	public static void main(String[] args) throws IOException {
		InetAddress serverip=InetAddress.getByName(IP);
		int serverport=PORT;
		boolean choice=true;
		int answer=JOptionPane.showConfirmDialog(null, QUESTION);
		choice = (answer == 0);
		Client client=new Client(serverip,serverport,choice);
		client.runClient();
	}
}
