package fr.pantheonsorbonne.miage.game.classes.pokerTableStuff;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.pantheonsorbonne.miage.game.classes.cards.Card;
import fr.pantheonsorbonne.miage.game.classes.cards.CardColor;
import fr.pantheonsorbonne.miage.game.classes.cards.CardValue;

public class Deck {
	private List<Card> cards;
	private Random random = new Random();

	/**
	 * A deck contains all cards from 2 to ace, of all 4 colors, and each card only
	 * appears once. Drawing from the deck removes the card from the deck.
	 */

	public Deck() {
		this.resetDeck();
	}

	/**
	 * draws cards from the deck
	 * 
	 * @param deckSize : number of cards to draw
	 * @return list of drawn cards
	 */
	public List<Card> getRandomCards(int deckSize) {

		List<Card> cardss = new ArrayList<>(deckSize);
		for (int i = 0; i < deckSize; i++) {
			cardss.add(i, this.draw());
		}
		return cardss;

	};
	public void remove(Card card) {
		this.cards.remove(card);
	}
	/**
	 * draws a card and then removes it from the deck
	 * 
	 * @return card drawn
	 */
	public Card draw() {
		if (this.cards.isEmpty()) {
			return null;
		}
		int i = random.nextInt(this.cards.size());
		Card toReturn = this.cards.get(i);
		this.cards.remove(i);
		return toReturn;
	}

	
	
	public void resetDeck() {
		this.cards = new ArrayList<>(52);
		int k = 0;
		for (int i = 0; i < 13; i++) {
			this.cards.add(i, new Card(CardValue.values()[k], CardColor.CLOVER));
			k++;
		}
		k = 0;
		for (int i = 13; i < 26; i++) {
			this.cards.add(i, new Card(CardValue.values()[k], CardColor.DIAMOND));
			k++;
		}
		k = 0;
		for (int i = 26; i < 39; i++) {
			this.cards.add(i, new Card(CardValue.values()[k], CardColor.SPADE));
			k++;
		}
		k = 0;
		for (int i = 39; i < 52; i++) {
			this.cards.add(i, new Card(CardValue.values()[k], CardColor.HEART));
			k++;
		}
	}
	
	public void discard() {
		this.cards.remove(random.nextInt(this.cards.size()));
	}
}