package game;

import environment.Direction;

public class HumanPlayer extends Player {
	private Direction nextDirection;
	private int move;
	private long interval=Game.REFRESH_INTERVAL;
	
	public HumanPlayer(Game game, int id, byte originalStrength) {
		super(game, id, originalStrength);
	}
	
	public void run() {
		try {
			addPlayerToGame();
		} catch (InterruptedException e1) {
			return;
		}

		try {
			while(!game.isStarted())
				Thread.sleep(200);
		} catch (InterruptedException e) {
			return;
		}

		while (!isInterrupted() && this.getCurrentStrength() > 0 && this.getCurrentStrength() < 10) {
			try {
				sleep(interval * getDebuffMultiplier());
				if (move == 1) {
					movePlayer(this, nextDirection);
				} else {
				}
			} catch (InterruptedException e) {
				return;
			}
		}
	}
	
	public void setNextDirection(Direction direction) {
		this.nextDirection=direction;
		setMove(1);
	}
	public void setMove(int move) {
		this.move = move;
	}
	public int getMove() {
		return move;
	}
	
	public boolean isHumanPlayer() {
		return true;
	}
}
