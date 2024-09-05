package environment;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import game.Game;
import game.Player;

public class Cell {
	private Coordinate position;
	private Game game;
	private Player player;
	private Lock lock = new ReentrantLock();
	private Condition cellIsFree = lock.newCondition();

	public Cell(Game g, Coordinate position) {
		super();
		this.game=g;
		this.position = position;
	}

	public Coordinate getPosition() {
		return position;
	}

	public Player getPlayer() {
		return player;
	}

	public synchronized void setPlayer(Player player) {
		this.player = player;
	}
	
	public Cell getCell(Coordinate at) {
		return game.board[at.x][at.y];
	}

	public boolean isOccupied() {
		return player != null;
	}

	public void initialPut(Player player) throws InterruptedException {
		lock.lock();

		try {
			if (isOccupied()) {
				player.setNotPlacedAtFirst(); 
			}
			
			while (isOccupied() && getPlayer().isActive()) { 
				cellIsFree.await();
			}
	
			if (isOccupied() && !getPlayer().isActive()) { 
				player.addPlayerToGame(); 
				return;
			}

			setPlayer(player);
			game.notifyChange();
		} finally {
			lock.unlock();
		}
	}

	public synchronized void movementPut(Player movingPlayer, Cell currentCell) throws InterruptedException { 
		if (!movingPlayer.isActive()) 
			return;
		if (isOccupied()) {
			if (this.getPlayer().isActive()) {
				movingPlayer.duel(this.getPlayer()); 
			} else {
				if(!movingPlayer.isHumanPlayer()) {
					game.goThreadTwoSeconds(movingPlayer);
				try {
					wait();
				} catch (InterruptedException e) {
					if (game.getEndOfGame()) {
						Thread.currentThread().interrupt();
					} else {
						return;
					}
				}
			}
			}
		} else { 
			currentCell.clear();
			currentCell.releaseAwaitingPlayers();
			this.setPlayer(movingPlayer); 
			if (movingPlayer.isHumanPlayer())
				movingPlayer.setMove(0);
		}

		game.notifyChange();
	}

	private synchronized void clear() throws InterruptedException {
		setPlayer(null);
	}
	
	public void releaseAwaitingPlayers() throws InterruptedException {
		lock.lock();
		try {
			cellIsFree.signalAll();
		} finally {
			lock.unlock();
		}
	}

}
