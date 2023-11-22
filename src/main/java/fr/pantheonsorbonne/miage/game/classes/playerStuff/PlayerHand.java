package fr.pantheonsorbonne.miage.game.classes.playerStuff;

import java.util.List;

import fr.pantheonsorbonne.miage.game.classes.cards.Card;

public class PlayerHand {
	private List<Card> hand;

	public PlayerHand(List<Card> cards) {
		this.hand = cards;
	}

	public List<Card> getHand() {
		return this.hand;
	}

	public void add(Card card) {
		this.hand.add(card);
	}

	public Card removeRandomCard() {
		if (this.hand.size() > 0)
			return this.hand.remove((int) (Math.random() * this.hand.size()));
		return null;
	}

	public Card remove(Card card) {
		if (this.hand.contains(card)) {
			this.hand.remove(card);
			return card;
		}
		return null;
	}

	public void showRandomCard() {
		int i;
		if (this.hand.size() < 1 || this.allCardsAreShown()) {
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
