package fr.pantheonsorbonne.miage.game.classes;

import java.util.ArrayList;
/**
 * An all-in pot's purpose is to payout all-in players accordingly in case they win.
 */
public class AllInPot extends Pot {
	private Player player;
	private int allInBet;
	
	public AllInPot(int allInBet, Player player) {
		super();
		this.setPlayer(player);
		this.setAllInBet(allInBet);
	}
	public AllInPot(ArrayList<Player> players) {
		super(players);
		this.allInBet=players.get(0).getBet();
	}
	public int getAllInBet() {
		return allInBet;
	}

	public void setAllInBet(int allInBet) {
		this.allInBet = allInBet;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
