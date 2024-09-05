package client;

import java.util.Observable;
import utils.Message;

public class Game extends Observable{
	public static final int DIMY = 30;
	public static final int DIMX = 30;
	private Message msg;
	public Game() {}
	
	public Message getMsg() {
		return msg;
	}
	public void notifyChange() {
		setChanged();
		notifyObservers();
	}
	public void setMsg(Message message) {
		this.msg = message;
	}
}