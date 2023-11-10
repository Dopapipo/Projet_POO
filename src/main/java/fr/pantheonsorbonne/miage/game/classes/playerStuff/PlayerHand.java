package fr.pantheonsorbonne.miage.game.classes.playerStuff;

import java.util.List;

import fr.pantheonsorbonne.miage.game.classes.cards.Card;




public class PlayerHand {
	private List<Card> hand;
	
	public List<Card> getHand() {
		return this.hand;
	}
	
	public void add(Card card) {
		this.hand.add(card);
	}
	public PlayerHand(List<Card> cards) {
		this.hand=cards;
	}
	public void remove(Card card) {
		this.hand.remove(card);
 	}
	public void removeRandomCard() {
		this.hand.remove((int) (Math.random() * this.hand.size()));
	}

	public void show() {
		for (Card card : this.hand) {
			card.show();
		}
	}
	
	
}
