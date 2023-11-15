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
	public DealerHand(List<Card> cards) {
		this.dealerHand=cards;
		this.deck = new Deck();
	}
	
	public void flop() {
		this.deck.discard();
		this.addToDealerHand(deck.getRandomCards(3));
	}
	public void turn() {
		this.deck.discard();
		this.addToDealerHand(deck.draw());
	}
	public void river() {
		this.deck.discard();
		this.addToDealerHand(deck.draw());
	}
	
	private void addToDealerHand(Card card) {
		this.dealerHand.add(card);
	}
	
	private void addToDealerHand(List<Card> cards) {
		for (Card card : cards) {
			addToDealerHand(card);
		}
	}
	public List<Card> getDealerHand() {
		return this.dealerHand;
	}
	public void printHand() {
		System.out.println("Dealer"+ " has the following hand :");
		for (Card card: this.dealerHand) {
			System.out.println(card);
		}
		//System.out.print(this.dealerHand.stream().map(c -> c.toFancyString()).collect(Collectors.joining(" ")));

		//System.out.println();
	}
	
	public void clear() {
		this.dealerHand.clear();
	}
	//This method is public for unit testing purposes
	public void setHand(List<Card> cards) {
		this.dealerHand=cards;
	}
}
