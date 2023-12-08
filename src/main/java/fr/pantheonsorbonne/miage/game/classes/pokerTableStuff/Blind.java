package fr.pantheonsorbonne.miage.game.classes.pokerTableStuff;

import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;

public class Blind {
	// A blind has a value and what player needs to pay it
	private int value;
	private Player player;

	public Blind(int value, Player player) {
		this.value = value;
		this.player = player;
	}

	protected int getValue() {
		return this.value;
	}

	protected Player getPlayer() {
		return this.player;
	}

	protected void increase(int howMuch) {
		this.value += howMuch;
	}

	protected void setPlayer(Player player) {
		this.player = player;
	}

	public String toString() {
		return this.player.getName() + " has to pay a blind of " + this.value;
	}
}
