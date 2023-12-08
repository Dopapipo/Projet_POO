package fr.pantheonsorbonne.miage.game.classes.playerStuff;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.pantheonsorbonne.miage.game.classes.cards.Card;

public class PlayerHand {
	private List<Card> hand;
	private Random random = new Random();

	public PlayerHand(List<Card> cards) {
		this.hand = cards;
	}
	public PlayerHand() {
		this(new ArrayList<>());
	}
	protected int size() {
		return this.hand.size();
	}
	protected List<Card> getHand() {
		return this.hand;
	}

	protected void add(Card card) {
		this.hand.add(card);
	}

	protected Card removeRandomCard() {
		if (!this.hand.isEmpty())
			return this.hand.remove(random.nextInt(this.hand.size()));
		return null;
	}
	protected void showCard(Card card) {
		card.show();
	}
	protected Card remove(Card card) {
		if (this.hand.remove(card)) {
			return card;
		}
		return null;
	}
	protected Card getCardAtIndex(int i) {
		return this.hand.get(i);
	}
	protected void clear() {
		this.hand.clear();
	}
	protected void showRandomCard() {
		int i;
		if (this.hand.isEmpty()|| this.allCardsAreShown()) {
			return;
		}
		do {
			i = random.nextInt(this.hand.size());
		} while (this.hand.get(i).isFaceUp());
		this.hand.get(i).show();
	}

	protected boolean allCardsAreShown() {
		for (Card card : this.hand) {
			if (!card.isFaceUp()) {
				return false;
			}
		}
		return true;
	}

}
