package game;
import java.util.ArrayList;
import java.util.Observable;
import environment.Cell;
import environment.Coordinate;
import environment.TwoSecondsWait;

public class Game extends Observable {
	public static final int DIMY = 30;
	public static final int DIMX = 30;
	public static final int NUM_BOT_PLAYERS = 50;
	public static final int NUM_FINISHED_PLAYERS_TO_END_GAME=2;
	public static final long REFRESH_INTERVAL = 20;
	public static final long BOT_REFRESH_INTERVAL = 100;
	public static final double MAX_INITIAL_STRENGTH = 3;
	public static final long MAX_WAITING_TIME_FOR_MOVE = 2000;
	public static final long INITIAL_WAITING_TIME = 10000;
	private LaunchWinnersCount lwc; 
	private WinnersCount wc; 
	private boolean endOfGame = false;
	private boolean isStarted;

	public Cell[][] board;
	private ArrayList<Player> arrayPlayerThreads; 
	private ArrayList<TwoSecondsWait> arrayTwoSecondsThreads; 
	
	public Game() {
		board = new Cell[Game.DIMX][Game.DIMY]; 
		for (int x = 0; x < Game.DIMX; x++) 
			for (int y = 0; y < Game.DIMY; y++) 
				board[x][y] = new Cell(this, new Coordinate(x, y));
		this.wc = null;
		lwc = new LaunchWinnersCount (this, NUM_FINISHED_PLAYERS_TO_END_GAME);
	}
		
	public void go() {
		arrayPlayerThreads = new ArrayList<>();
		arrayTwoSecondsThreads = new ArrayList<>();
		lwc.start();
			
		for (int i = 0; i < NUM_BOT_PLAYERS; i++) {
			Byte originalStrength = 0;
			int random = (int) (Math.random() * 3);
			switch (random) {
			case 0:
				originalStrength = (byte) 1;
				break;
			case 1:
				originalStrength = (byte) 2;
				break;
			case 2:
				originalStrength = (byte) 3;
				break;
			}

			BotPlayer botPlayer = new BotPlayer(this, i, originalStrength);
			arrayPlayerThreads.add(botPlayer);
			botPlayer.start();
		}
	}

	public void gameOver() {
		endOfGame = true;

		for (TwoSecondsWait tsw : arrayTwoSecondsThreads) {
			tsw.interrupt();
		}
		for (TwoSecondsWait tsw : arrayTwoSecondsThreads) {
			if (tsw.isAlive()) {
				try {
					tsw.join();
				} catch (InterruptedException e) {
				}
			}
		}
		
		for (Player pl : arrayPlayerThreads) {
			pl.interrupt();
		}
		for (Player pl : arrayPlayerThreads) {
			if (pl.isAlive()) {
				try {
					pl.join();
				} catch (InterruptedException e1) {
				}
			}
		}
		System.out.println("Game Over!");
	}
	
	public Cell getRandomCell() {
		Cell newCell=getCell(new Coordinate((int)(Math.random()*Game.DIMX),(int)(Math.random()*Game.DIMY)));
		return newCell; 
	}
	public Cell[][] getBoard() {
		return board;
	}
	public void setBoard(Cell[][] board) {
		this.board = board;
	}
	public Cell getCell(Coordinate at) {
		return board[at.x][at.y];
	}

	public WinnersCount getWinnersCount() {
		return wc;
	}
	
	public void setWinnersCount(WinnersCount wc) {
		this.wc = wc;
	}

	public boolean getEndOfGame(){
		return endOfGame;
	}
	
	public void notifyChange() {
		setChanged();
		notifyObservers();
	}

	public void goThreadTwoSeconds(Player player) throws InterruptedException {
		TwoSecondsWait tsw = new TwoSecondsWait(player);
		arrayTwoSecondsThreads.add(tsw);
		tsw.start();
	}
	public boolean isEndOfGame() {
		return endOfGame;
	}
	public boolean isStarted() {
		return isStarted;
	}
	public void setStarted(boolean isStarted) {
		this.isStarted = isStarted;
	}
}
