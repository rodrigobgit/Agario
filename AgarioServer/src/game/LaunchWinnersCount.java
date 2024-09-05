package game;

public class LaunchWinnersCount extends Thread {
	private Game game;
	private int winnersToGameOver;
	private WinnersCount wc;

	public LaunchWinnersCount(Game game, int winnersToGameOver) {
		this.game = game;
		this.winnersToGameOver = winnersToGameOver;
		wc = new WinnersCount(game, winnersToGameOver);
	}

	public void run() {
		try {
			wc.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		game.gameOver();
	}
}
 