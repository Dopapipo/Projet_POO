package fr.pantheonsorbonne.miage.game.classes.playerStuff;

import java.util.List;
import java.util.Random;

import fr.pantheonsorbonne.miage.game.classes.superpowers.SuperpowerChoice;

public class PlayerBot extends Player {
	Random random = new Random();

	public PlayerBot(String name) {
		super(name);
	}

	public PlayerBot(String name, int chipStack) {
		super(name, chipStack);
	}

	/**
	 * Call,fold,raise
	 * For now, we always all-in.
	 * This bot is super dumb, doesn't even care about the amount needed to call
	 * 
	 * @return 1,2,3 (call,fold,raise)
	 */
	public int getCommand(int amountToCall) {
		return 3;
	}

	/**
	 * We always all-in (our bot is really dumb so he doesn't even care how much he has to bet for call!)
	 * 
	 * @return how much we bet
	 */
	public int getBetAmount(int amountToCall) {
		return this.getChipStack();
	}

	// Use superpower at random
	public SuperpowerChoice getSuperpower() {
		return SuperpowerChoice.values()[random.nextInt(SuperpowerChoice.values().length)];
	}

	// Use superpower on random player
	public Player askForPlayerToUseSuperpowerOn(List<Player> players) {
		Player toReturn;
		do {
			toReturn = players.get(random.nextInt(players.size()));
		} while (!toReturn.isPlaying() && players.size() > 1 && toReturn != this);
		return toReturn;
	}

	// Use superpower on random player (that might not be playing but hey the bot is extra
	// dumb!)
	public String askForPlayerToUseSuperpowerOn(String players) {
		return players.split(",")[(int) Math.random() * players.split(",").length].trim();	}

	// always invert the same color because dumb :D
	public int askForInvertedColor() {
		return 0;
	}

	
}
