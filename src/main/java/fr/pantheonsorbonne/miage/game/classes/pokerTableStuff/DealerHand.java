package fr.pantheonsorbonne.miage.game.classes.pokerTableStuff;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fr.pantheonsorbonne.miage.game.classes.cards.Card;

public class DealerHand {
	private List<Card> dealerHand;
	private Deck deck;
	public DealerHand(Deck deck) {
		this.dealerHand=new ArrayList<>();
		this.deck = deck;
	}
	
	public void flop() {
		this.addToDealerHand(deck.getRandomCards(3));
	}
	public void turn() {
		this.addToDealerHand(deck.draw());
	}
	public void river() {
		this.addToDealerHand(deck.draw());
	}
	
	public void addToDealerHand(Card card) {
		this.dealerHand.add(card);
	}
	
	public void addToDealerHand(List<Card> cards) {
		for (Card card : cards) {
			addToDealerHand(card);
		}
	}
	public List<Card> getDealerHand() {
		return this.dealerHand;
	}
	public void printHand() {
		System.out.println("Dealer"+ " has the following hand :");
		
		System.out.print(this.dealerHand.stream().map(c -> c.toFancyString()).collect(Collectors.joining(" ")));

		System.out.println();
	}
	public void clear() {
		this.dealerHand.clear();
	}
	public void setHand(List<Card> cards) {
		this.dealerHand=cards;
	}
}
