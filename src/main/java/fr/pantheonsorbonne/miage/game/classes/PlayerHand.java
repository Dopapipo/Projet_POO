package fr.pantheonsorbonne.miage.game.classes;

import java.util.ArrayList;
import java.util.List;

public class PlayerHand {
	private List<Card> hand;
	
	public List<Card> getPlayerHand() {
		return this.hand;
	}
	
	public void add(Card card) {
		this.hand.add(card);
	}
	public PlayerHand(ArrayList<Card> cards) {
		this.hand=cards;
	}
	public void remove(Card card) {
		this.remove(card);
	}
	
}
