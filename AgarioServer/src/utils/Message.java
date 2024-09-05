package utils;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {		
	private final ArrayList<Data> info; 
	public final int isOver;
	
	public Message(ArrayList<Data>info,int isOver) {
		this.info=info;
		this.isOver=isOver;
	
	}

	public ArrayList<Data> getInfo() {
		return info;
	}

}