package fr.pantheonsorbonne.miage.game.classes.playerStuff;

import java.util.List;

import fr.pantheonsorbonne.miage.game.classes.cards.Card;

public class PlayerHand {
	private List<Card> hand;

	public PlayerHand(List<Card> cards) {
		this.hand = cards;
	}
	public int size() {
		return this.hand.size();
	}
	public List<Card> getHand() {
		return this.hand;
	}

	public void add(Card card) {
		this.hand.add(card);
	}

	public Card removeRandomCard() {
		if (!this.hand.isEmpty())
			return this.hand.remove((int) (Math.random() * this.hand.size()));
		return null;
	}
	public void showCard(Card card) {
		card.show();
	}
	public Card remove(Card card) {
		if (this.hand.remove(card)) {
			return card;
		}
		return null;
	}
	public Card getCardAtIndex(int i) {
		return this.hand.get(i);
	}
	public void clear() {
		this.hand.clear();
	}
	public void showRandomCard() {
		int i;
		if (this.hand.isEmpty()|| this.allCardsAreShown()) {
			return;
		}
		do {
			i = (int) (Math.random() * this.hand.size());
		} while (this.hand.get(i).isFaceUp());
		this.hand.get(i).show();
	}

	public boolean allCardsAreShown() {
		for (Card card : this.hand) {
			if (!card.isFaceUp()) {
				return false;
			}
		}
		return true;
	}

}
