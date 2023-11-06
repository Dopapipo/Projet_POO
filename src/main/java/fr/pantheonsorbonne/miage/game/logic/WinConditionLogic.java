package fr.pantheonsorbonne.miage.game.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.pantheonsorbonne.miage.game.classes.Card;
import fr.pantheonsorbonne.miage.game.classes.CardColor;
import fr.pantheonsorbonne.miage.game.classes.CardValue;
import fr.pantheonsorbonne.miage.game.classes.DealerHand;
import fr.pantheonsorbonne.miage.game.classes.PlayerHand;
import fr.pantheonsorbonne.miage.game.classes.WinCondition;
import fr.pantheonsorbonne.miage.game.classes.WinningCombination;

public class WinConditionLogic {

	/**
	 * Finds the highest <WinningCombination> for a hand. Will be used on any hand
	 * of length >=2 (by default each player gets 2 cards.)
	 * 
	 * @param dealerHand : The dealerHand is updated after the turn,river and flop
	 * @param playerHand : The playerHand is updated when cards are dealt
	 * @return a <WinningCombination> of the highest value in a hand
	 */

	public static WinningCombination findWinningCombination(DealerHand dealerHand, PlayerHand playerHand) {
		List<Card> consideredHand = new ArrayList<>();

		for (Card card : dealerHand.getDealerHand()) {
			consideredHand.add(card);
		}
		for (Card card : playerHand.getPlayerHand()) {
			consideredHand.add(card);
		}
		// We will check for the highest values of WinningCombination first.

		// check for royal flush and straight flush :
		WinningCombination straight = straight(consideredHand);
		WinningCombination flush = flush(consideredHand);
		if (flush != null && straight != null) {
			// check for royal flush:
			if (flush.getCardValue() == CardValue.ACE) {
				return new WinningCombination(WinCondition.ROYAL_FLUSH, CardValue.ACE);
			}
			// else we only have a straight flush
			return new WinningCombination(WinCondition.STRAIGHT_FLUSH, flush.getCardValue());
		}
		// check for a fullHouse :
		WinningCombination multipleCards = findMultipleCardsAndFullHouse(consideredHand);
		if (multipleCards != null && multipleCards.getWinCondition() == WinCondition.FULL_HOUSE) {
			return multipleCards;
		}
		// check for a flush
		if (flush != null) {
			return flush;
		}
		// check for a straight
		if (straight != null) {
			return straight;
		}
		// check for Three of a kind, two pairs, pair (multipleCards has the highest of
		// those contained as an attribute)
		if (multipleCards != null) {
			return multipleCards;
		}
		// If we have none of the above, we only have a High Card :(
		return new WinningCombination(WinCondition.HIGH_CARD, findHighestCardInCardList(consideredHand));
	}

	/**
	 * Checks for a flush in an List of cards. To use after merging dealer and
	 * player hands
	 * 
	 * @param hand
	 * @return null if there's no flush, and a WinningCombination with the highest
	 *         card from the flush if there is a flush.
	 */
	private static WinningCombination flush(List<Card> hand) {
		Map<CardColor, Integer> colors = new HashMap<>();
		// count the cards for each color
		for (Card card : hand) {
			Integer hasBeenPut = colors.putIfAbsent(card.getCardColor(), 1);
			if (hasBeenPut != null) {
				colors.put(card.getCardColor(), colors.get(card.getCardColor()) + 1);
			}
		}
		// check every color that we have a card from
		for (CardColor color : colors.keySet()) {
			// if we have at least 5 cards of the same color
			if (colors.get(color) >= 5) {
				// find the highest card of that color
				CardColor flushColor = color;
				CardValue maxCard = CardValue.TWO;
				for (Card card : hand) {
					if (card.getCardColor() == flushColor && card.getCardValue().compare(maxCard) > 0) {
						maxCard = card.getCardValue();
					}
				}
				// return as soon as we find the right color
				return new WinningCombination(WinCondition.FLUSH, maxCard);
			}
		}
		return null;

	}

	/**
	 * Finds wether or not our hand has a straight. This method needs to be updated
	 * to support ACE,TWO,THREE,FOUR,FIVE straights.
	 * 
	 * @param hand : dealer and player hand merged
	 * @return <null> if we have no straight, a <WinningCombination> with a straight
	 *         <WinCondition> and the highest <CardValue> from the straight
	 *         otherwise.
	 */
	private static WinningCombination straight(List<Card> hand) {
		boolean flag = false;
		int buffer = 1;
		// sort hand by increasing cardValue 
		Comparator<Card> comparator = (Card c1, Card c2) -> c1.getCardValue().compare(c2.getCardValue());
		Collections.sort(hand, comparator);

		// There's up to 7 cards in a hand, but a straight is 5 cards. We have to be
		// careful about how we proceed.
		int highestCardIndex = 0;
		for (int i = 0; i < hand.size() - 1; i++) {
			// check if the next CardValue is equal to the value of the next card
			if (hand.get(i).getCardValue().getNext() == hand.get(i + 1).getCardValue()) {
				buffer++;
			} else { // reset the buffer if it's not.
				buffer = 1;
			}
			// Since we sorted the hand beforehand, when buffer>=5, the highest card from
			// the straight will be at index i+1. It works even if we have 7 straight cards,
			// this is why we use buffer>=5 instead of buffer==5.
			if (buffer >= 5) {
				flag = true;
				highestCardIndex = i + 1;
				//
			}
		}

		// flag tells us if we found a straight. We can't use buffer as a flag
		// because of some cases (e.g. a 2 3 4 5 6 QUEEN KING sorted hand)
		if (flag) {
			return new WinningCombination(WinCondition.STRAIGHT, hand.get(highestCardIndex).getCardValue());
		}
		return null;
	}

	/**
	 * Finds the highest WinningCombination in our hand among : A pair, Two pairs,
	 * Three of a kind, FullHouse, Four of a kind
	 * 
	 * @param hand : the combination of a player hand and a dealer hand
	 * @return null if we have neither of the above, or the highest available
	 *         WinningCombination.
	 */
	private static WinningCombination findMultipleCardsAndFullHouse(List<Card> hand) {
		Map<CardValue, Integer> multipleCards = new HashMap<>();
		List<CardValue> hasBeenChecked = new ArrayList<>();
		for (int i = 0; i < hand.size(); i++) {
			// Iterate on the cards after the card we're checking
			for (Card card : hand.subList(i + 1, hand.size())) {
				// check if we already checked for multiple cards on the current card
				// before checking for equality for optimisation
				if (!hasBeenChecked.contains(card.getCardValue()) && card.equals(hand.get(i))) {
					Integer hasBeenPut = multipleCards.putIfAbsent(hand.get(i).getCardValue(), 2);
					if (hasBeenPut != null) {
						multipleCards.put(hand.get(i).getCardValue(),
								multipleCards.get(hand.get(i).getCardValue()) + 1);
					}
				}

			}
			// After we check every card for hand.get(i), we add hand.get(i) to the
			// list of already checked cards.
			hasBeenChecked.add(hand.get(i).getCardValue());
		}

		// Now multipleCards has every pair, three of a kind, and four of a kind,
		// along with their values. The key is the CardValue, and the value associated
		// is how many cards of the same value there are in the hand.
		// Now let's build a a list of WinningCombinations for pairs and threes.
		List<WinningCombination> pairs = new ArrayList<>();
		List<WinningCombination> threes = new ArrayList<>();
		for (CardValue cardValue : multipleCards.keySet()) {
			switch (multipleCards.get(cardValue)) {
			case 2:
				pairs.add(new WinningCombination(WinCondition.PAIR, cardValue));
				break;
			case 3:
				threes.add(new WinningCombination(WinCondition.THREE_OF_A_KIND, cardValue));
				break;
			case 4: // if we found 4 cards that are the same, we found the highest
					// possible hand in the set of WinConditions we're checking for.
				return new WinningCombination(WinCondition.FOUR_OF_A_KIND, cardValue);

			}
		}
		// if maxPair or maxThrees is null, then there's no pair or three of a kind
		// in our hand, because the lists associated are empty
		CardValue maxPair = findHighestCard(pairs);
		CardValue maxThrees = findHighestCard(threes);
		// if there's a pair and a three of a kind, we have a fullhouse!
		if (maxPair != null && maxThrees != null) {
			CardValue maxCard = maxPair.max(maxThrees);
			return new WinningCombination(WinCondition.FULL_HOUSE, maxCard);
		}
		// else, the next highest hand could be a THREE OF A KIND!
		if (maxThrees != null) {
			return new WinningCombination(WinCondition.THREE_OF_A_KIND, maxThrees);
		}
		// else, it could be TWO PAIRS!
		if (maxPair != null && pairs.size() >= 2) {
			return new WinningCombination(WinCondition.TWO_PAIR, maxPair);
		}
		// else it could be a PAIR!
		if (maxPair != null) {
			return new WinningCombination(WinCondition.PAIR, maxPair);
		}
		// else, we don't have a pair, two pairs, three of a kind,
		// fullhouse, or four of a kind.
		return null;
	}

	/**
	 * Finds the highest card in a List of WinningCombination
	 * 
	 * @param winningHands : a List of pairs,threes,etc...
	 * @return null if the list is empty, the maximum card if it's not
	 */
	private static CardValue findHighestCard(List<WinningCombination> winningHands) {
		CardValue maxValue = null;
		for (WinningCombination wc : winningHands) {
			if (wc.getCardValue().compare(maxValue) > 0) {
				maxValue = wc.getCardValue();
			}
		}
		return maxValue;
	}

	/**
	 * Finds the highest card in a <Card> list.
	 * 
	 * @param cardList a <Card> list
	 * @return the highest <CardValue> of the cards in the list
	 */
	private static CardValue findHighestCardInCardList(List<Card> cardList) {
		CardValue maxValue = null;
		for (Card card : cardList) {
			if (card.getCardValue().compare(maxValue) > 0) {
				maxValue = card.getCardValue();
			}
		}
		return maxValue;
	}
}
