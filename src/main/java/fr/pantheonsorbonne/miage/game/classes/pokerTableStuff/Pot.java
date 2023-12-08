package fr.pantheonsorbonne.miage.game.classes.pokerTableStuff;

import java.util.ArrayList;
import java.util.List;

import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;

/**
 * A Pot is used to distribute gains at the end of a round,
 * and holds all the bets. There can be multiple pots when a player goes all-in,
 * because the remaining players will compete for a pot that the all-in player
 * can not win, as he has not paid into it.
 * A pot has a thresholdBet that a player needs to pay in order to compete for
 * it.
 */
public class Pot implements Comparable<Pot> {
	private int value;
	private int thresholdBet;
	private List<Player> players;

	public Pot() {
		this(0);
	}


	public Pot(int thresholdBet) {
		this.thresholdBet = thresholdBet;
		this.value = 0;
		this.players = new ArrayList<>();
	}

	protected void addPlayer(Player player) {
		this.players.add(player);
	}

	protected void addBet(int bet) {
		this.value += bet;
	}

	protected int getValue() {
		return value;
	}

	protected void setValue(int value) {
		this.value = value;
	}
	//Return a copy of the list of players
	protected List<Player> getPlayers() {
		return new ArrayList<>(this.players);
	}


	protected int getThresholdBet() {
		return thresholdBet;
	}

	protected void setThresholdBet(int thresholdBet) {
		this.thresholdBet = thresholdBet;
	}

	@Override
	public int compareTo(Pot o) {
		return this.getValue() - o.getValue();
	}

}
