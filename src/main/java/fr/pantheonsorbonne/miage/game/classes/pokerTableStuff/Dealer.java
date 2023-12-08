package fr.pantheonsorbonne.miage.game.classes.pokerTableStuff;

import java.util.ArrayList;
import java.util.List;

import fr.pantheonsorbonne.miage.game.classes.cards.Card;

public class Dealer {
	private List<Card> hand;
	private Deck deck;

	public Dealer(Deck deck) {
		this.hand = new ArrayList<>();
		this.deck = deck;
	}

	public Dealer(List<Card> cards) {
		this.hand = cards;
		this.deck = new Deck();
	}

	protected void flop() {
		this.deck.discard();
		this.addToDealerHand(deck.getRandomCards(3));
	}

	protected void turn() {
		this.deck.discard();
		this.addToDealerHand(deck.draw());
	}

	protected void river() {
		this.turn();
	}

	private void addToDealerHand(Card card) {
		this.hand.add(card);
	}

	private void addToDealerHand(List<Card> cards) {
		for (Card card : cards) {
			addToDealerHand(card);
		}
	}

	public List<Card> getDealerHand() {
		return this.hand;
	}


	protected void clear() {
		this.hand.clear();
	}

	// This method is protected for unit testing purposes
	protected void setHand(List<Card> cards) {
		this.hand = cards;
	}
}
