package fr.pantheonsorbonne.miage.game.classes.playerStuff;

import java.util.List;

import fr.pantheonsorbonne.miage.game.classes.cards.Card;




public class PlayerHand {
	private List<Card> hand;
	
	public PlayerHand(List<Card> cards) {
		this.hand=cards;
	}
	public List<Card> getHand() {
		return this.hand;
	}
	
	public void add(Card card) {
		this.hand.add(card);
	}
	public void removeRandomCard() {
		this.hand.remove((int) (Math.random() * this.hand.size()));
	}
	public void showRandomCard() {
		int i;
		do {
			i=(int) (Math.random() * this.hand.size());
		}
		while(this.hand.get(i).isFaceUp());
		this.hand.get(i).show();
	}

	
	
}
