package environment;

import game.Player;

public class TwoSecondsWait extends Thread {
	private Player blockedPlayer;

	public TwoSecondsWait(Player blockedPlayer) {
		this.blockedPlayer = blockedPlayer;
	}

	public void run() {
		while (!isInterrupted()) {
			try {
				Thread.sleep(2000);
				blockedPlayer.interrupt();
				return;
			} catch (InterruptedException e) {
				return;
			}
		}
	}

}
