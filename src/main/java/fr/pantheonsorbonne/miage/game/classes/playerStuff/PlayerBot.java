package fr.pantheonsorbonne.miage.game.classes.playerStuff;

import java.util.List;
import java.util.Random;

public class PlayerBot extends Player {
	Random random = new Random();
    public PlayerBot(String name) {
        super(name);
    }
    public PlayerBot(String name, int chipStack) {
        super(name, chipStack);    }
    
    	/**
	 * Call,fold,raise
	 * For now, we always all-in. 
	 * @return 1,2,3 (call,fold,raise)
	 */
	public int getCommand() {
		return 3;
	}
	/**
	 * We always all-in
	 * @return how much we bet
	 */
	public int getBetAmount() {
		return this.getChipStack();
	}

	public int getSuperpower() {
		return random.nextInt(5);
	}
	public Player askForPlayerToUseSuperpowerOn(List<Player> players) {
		Player toReturn;
		do { toReturn = players.get((int) Math.random() * players.size());}
		while (!toReturn.isPlaying());
		return toReturn;
	}
}
