package game;

import environment.Direction;

public class BotPlayer extends Player {
	private long interval = Game.BOT_REFRESH_INTERVAL;

	public BotPlayer(Game game, int id, byte originalStrength) {
		super(game, id, originalStrength);
	}
	
	public void run() {
		try {
			addPlayerToGame();
		} catch (InterruptedException e1) {
			return;
		}
		if (placedAtFirst) {
			try {
				Thread.sleep(Game.INITIAL_WAITING_TIME);
			} catch (InterruptedException e2) {
				return;
			}
		}

		while (!isInterrupted() && this.isActive()) {
			try {
				sleep(interval * getDebuffMultiplier());
				rollDice();
			} catch (InterruptedException e3) {
				return;
			}
		}
	}

	public void rollDice() throws InterruptedException {
		double random=Math.random();
		Direction nextDirection = null;
		if (random<0.25) nextDirection=environment.Direction.UP;
		if (random<0.50 && random>=0.25) nextDirection=environment.Direction.DOWN;
		if (random<0.75 && random>=0.5)	nextDirection=environment.Direction.LEFT;
		if (random>=0.75) nextDirection=environment.Direction.RIGHT;
		movePlayer(this, nextDirection);	
	}
	
	public boolean isHumanPlayer() {
		return false;
	}
}
