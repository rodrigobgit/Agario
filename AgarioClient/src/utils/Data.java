package utils;

import java.io.Serializable;

public class Data implements Serializable {		
	private int xPos;
	private int yPos;
	private int strength;
	private boolean isHuman;
	
	public Data(int xPos, int yPos, int strength, boolean isHuman) {
		this.xPos=xPos;
		this.yPos = yPos;
		this.strength=strength;
		this.isHuman = isHuman;
	}
	
	public int getStrength() {
		return strength;
	}
	public int getxPos() {
		return xPos;
	}
	public int getyPos() {
		return yPos;
	}
	public boolean isHuman() {
		return isHuman;
	}
}