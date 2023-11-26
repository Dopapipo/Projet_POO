package fr.pantheonsorbonne.miage.game.classes.pokerTableStuff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import fr.pantheonsorbonne.miage.game.classes.cards.Card;
import fr.pantheonsorbonne.miage.game.classes.cards.CardColor;
import fr.pantheonsorbonne.miage.game.classes.cards.CardValue;

public class Deck {
	private Deque<Card> cards;
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

	}
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
		return this.cards.removeFirst();
	}

	
	
	public void resetDeck() {
		List<Card> cardss = new ArrayList<>();
		int k = 0;
		for (int i = 0; i < 13; i++) {
			cardss.add(new Card(CardValue.values()[k], CardColor.CLOVER));
			k++;
		}
		k = 0;
		for (int i = 13; i < 26; i++) {
			cardss.add(new Card(CardValue.values()[k], CardColor.DIAMOND));
			k++;
		}
		k = 0;
		for (int i = 26; i < 39; i++) {
			cardss.add( new Card(CardValue.values()[k], CardColor.SPADE));
			k++;
		}
		k = 0;
		for (int i = 39; i < 52; i++) {
			cardss.add( new Card(CardValue.values()[k], CardColor.HEART));
			k++;
		}
		Collections.shuffle(cardss);
		this.cards = new LinkedList<>(cardss);
	}
	
	public void discard() {
		this.cards.remove();
	}
}