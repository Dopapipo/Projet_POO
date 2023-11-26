package fr.pantheonsorbonne.miage.game.logic;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import fr.pantheonsorbonne.miage.game.classes.cards.Card;
import fr.pantheonsorbonne.miage.game.classes.cards.CardColor;
import fr.pantheonsorbonne.miage.game.classes.cards.CardValue;
import fr.pantheonsorbonne.miage.game.classes.cards.WinCondition;
import fr.pantheonsorbonne.miage.game.classes.cards.WinningCombination;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.PlayerHand;
import fr.pantheonsorbonne.miage.game.classes.pokerTableStuff.Dealer;

class WinConditionLogicTest {
	/**
	 * Tests for high card
	 */
	@Test
	void testHighCard() {
		Card card1 = new Card(CardValue.TWO, CardColor.CLOVER);
		Card card2 = new Card(CardValue.THREE, CardColor.SPADE);
		Card card3 = new Card(CardValue.FIVE, CardColor.CLOVER);
		Card card4 = new Card(CardValue.SEVEN, CardColor.HEART);
		Card card5 = new Card(CardValue.KING, CardColor.DIAMOND);
		Card card6 = new Card(CardValue.ACE, CardColor.CLOVER);
		Card card7 = new Card(CardValue.EIGHT, CardColor.HEART);
		PlayerHand hand = new PlayerHand(Arrays.asList(card1, card2));
		Dealer dealerHand = new Dealer(Arrays.asList(card3, card4, card5, card6, card7));
		WinningCombination result = WinConditionLogic.findWinningCombination(dealerHand, hand);
		assertEquals(new WinningCombination(WinCondition.HIGH_CARD, CardValue.ACE), result);

	}

	/*
	 * Tests for a pair
	 */
	@Test
	void testPair() {
		Card card1 = new Card(CardValue.ACE, CardColor.CLOVER);
		Card card2 = new Card(CardValue.THREE, CardColor.SPADE);
		Card card3 = new Card(CardValue.FIVE, CardColor.CLOVER);
		Card card4 = new Card(CardValue.SEVEN, CardColor.HEART);
		Card card5 = new Card(CardValue.KING, CardColor.DIAMOND);
		Card card6 = new Card(CardValue.ACE, CardColor.HEART);
		Card card7 = new Card(CardValue.EIGHT, CardColor.HEART);
		PlayerHand hand = new PlayerHand(Arrays.asList(card1, card2));
		Dealer dealerHand = new Dealer(Arrays.asList(card3, card4, card5, card6, card7));
		WinningCombination result = WinConditionLogic.findWinningCombination(dealerHand, hand);
		assertEquals(new WinningCombination(WinCondition.PAIR, CardValue.ACE), result);

	}

	@Test
	void testTwoPairs() {
		Card card1 = new Card(CardValue.ACE, CardColor.CLOVER);
		Card card2 = new Card(CardValue.THREE, CardColor.SPADE);
		Card card3 = new Card(CardValue.FIVE, CardColor.CLOVER);
		Card card4 = new Card(CardValue.FIVE, CardColor.HEART);
		Card card5 = new Card(CardValue.KING, CardColor.DIAMOND);
		Card card6 = new Card(CardValue.ACE, CardColor.DIAMOND);
		Card card7 = new Card(CardValue.EIGHT, CardColor.HEART);
		PlayerHand hand = new PlayerHand(Arrays.asList(card1, card2));
		Dealer dealerHand = new Dealer(Arrays.asList(card3, card4, card5, card6, card7));
		WinningCombination result = WinConditionLogic.findWinningCombination(dealerHand, hand);
		assertEquals(new WinningCombination(WinCondition.TWO_PAIR, CardValue.ACE), result);

	}

	@Test
	void testThreeOfAKind() {
		Card card1 = new Card(CardValue.FIVE, CardColor.CLOVER);
		Card card2 = new Card(CardValue.THREE, CardColor.SPADE);
		Card card3 = new Card(CardValue.FIVE, CardColor.SPADE);
		Card card4 = new Card(CardValue.FIVE, CardColor.HEART);
		Card card5 = new Card(CardValue.KING, CardColor.DIAMOND);
		Card card6 = new Card(CardValue.ACE, CardColor.CLOVER);
		Card card7 = new Card(CardValue.EIGHT, CardColor.HEART);
		PlayerHand hand = new PlayerHand(Arrays.asList(card1, card2));
		Dealer dealerHand = new Dealer(Arrays.asList(card3, card4, card5, card6, card7));
		WinningCombination result = WinConditionLogic.findWinningCombination(dealerHand, hand);
		assertEquals(new WinningCombination(WinCondition.THREE_OF_A_KIND, CardValue.FIVE), result);

	}

	/**
	 * Tests for a straight with no low ace, i.e. 2,3,4,5,6;
	 * Also tests that if we have a pair of a card from the straight, we still get a
	 * straight
	 */

	@Test
	void testStraightNoLowAce() {
		Card card1 = new Card(CardValue.FIVE, CardColor.CLOVER);
		Card card2 = new Card(CardValue.THREE, CardColor.SPADE);
		Card card3 = new Card(CardValue.FOUR, CardColor.CLOVER);
		Card card4 = new Card(CardValue.FIVE, CardColor.HEART);
		Card card5 = new Card(CardValue.KING, CardColor.DIAMOND);
		Card card6 = new Card(CardValue.TWO, CardColor.CLOVER);
		Card card7 = new Card(CardValue.SIX, CardColor.HEART);
		PlayerHand hand = new PlayerHand(Arrays.asList(card1, card2));
		Dealer dealerHand = new Dealer(Arrays.asList(card3, card4, card5, card6, card7));
		WinningCombination result = WinConditionLogic.findWinningCombination(dealerHand, hand);
		assertEquals(new WinningCombination(WinCondition.STRAIGHT, CardValue.SIX), result);

	}

	/*
	 * Tests that we get a straight when we have a low ace straight
	 * Also tests that we get it even if we have a pair of one of the straight cards
	 */
	@Test
	void testStraightWithLowAce() {
		Card card1 = new Card(CardValue.ACE, CardColor.CLOVER);
		Card card2 = new Card(CardValue.QUEEN, CardColor.SPADE);
		Card card3 = new Card(CardValue.THREE, CardColor.CLOVER);
		Card card4 = new Card(CardValue.FOUR, CardColor.HEART);
		Card card5 = new Card(CardValue.FIVE, CardColor.DIAMOND);
		Card card6 = new Card(CardValue.TWO, CardColor.CLOVER);
		Card card7 = new Card(CardValue.THREE, CardColor.HEART);
		PlayerHand hand = new PlayerHand(Arrays.asList(card1, card2));
		Dealer dealerHand = new Dealer(Arrays.asList(card3, card4, card5, card6, card7));
		WinningCombination result = WinConditionLogic.findWinningCombination(dealerHand, hand);
		assertEquals(new WinningCombination(WinCondition.STRAIGHT, CardValue.FIVE), result);

	}

	/*
	 * Tests for flush
	 */
	@Test
	void testFlush() {
		Card card1 = new Card(CardValue.ACE, CardColor.CLOVER);
		Card card2 = new Card(CardValue.QUEEN, CardColor.SPADE);
		Card card3 = new Card(CardValue.THREE, CardColor.CLOVER);
		Card card4 = new Card(CardValue.FOUR, CardColor.CLOVER);
		Card card5 = new Card(CardValue.NINE, CardColor.CLOVER);
		Card card6 = new Card(CardValue.TWO, CardColor.CLOVER);
		Card card7 = new Card(CardValue.THREE, CardColor.HEART);
		PlayerHand hand = new PlayerHand(Arrays.asList(card1, card2));
		Dealer dealerHand = new Dealer(Arrays.asList(card3, card4, card5, card6, card7));
		WinningCombination result = WinConditionLogic.findWinningCombination(dealerHand, hand);
		assertEquals(new WinningCombination(WinCondition.FLUSH, CardValue.ACE), result);

	}

	@Test
	/*
	 * Tests for full house
	 */
	void testFullHouse() {
		Card card1 = new Card(CardValue.ACE, CardColor.CLOVER);
		Card card2 = new Card(CardValue.ACE, CardColor.SPADE);
		Card card3 = new Card(CardValue.THREE, CardColor.CLOVER);
		Card card4 = new Card(CardValue.THREE, CardColor.DIAMOND);
		Card card5 = new Card(CardValue.NINE, CardColor.SPADE);
		Card card6 = new Card(CardValue.TWO, CardColor.CLOVER);
		Card card7 = new Card(CardValue.THREE, CardColor.HEART);
		PlayerHand hand = new PlayerHand(Arrays.asList(card1, card2));
		Dealer dealerHand = new Dealer(Arrays.asList(card3, card4, card5, card6, card7));
		WinningCombination result = WinConditionLogic.findWinningCombination(dealerHand, hand);
		assertEquals(new WinningCombination(WinCondition.FULL_HOUSE, CardValue.ACE), result);

	}

	/*
	 * Tests for four of a kind
	 */
	@Test
	void testFourOfAKind() {
		Card card1 = new Card(CardValue.ACE, CardColor.CLOVER);
		Card card2 = new Card(CardValue.ACE, CardColor.SPADE);
		Card card3 = new Card(CardValue.ACE, CardColor.HEART);
		Card card4 = new Card(CardValue.ACE, CardColor.DIAMOND);
		Card card5 = new Card(CardValue.NINE, CardColor.SPADE);
		Card card6 = new Card(CardValue.TWO, CardColor.CLOVER);
		Card card7 = new Card(CardValue.THREE, CardColor.HEART);
		PlayerHand hand = new PlayerHand(Arrays.asList(card1, card2));
		Dealer dealerHand = new Dealer(Arrays.asList(card3, card4, card5, card6, card7));
		WinningCombination result = WinConditionLogic.findWinningCombination(dealerHand, hand);
		assertEquals(new WinningCombination(WinCondition.FOUR_OF_A_KIND, CardValue.ACE), result);

	}

	/*
	 * Tests for straight flush
	 */
	@Test
	void testStraightFlush() {
		Card card1 = new Card(CardValue.THREE, CardColor.CLOVER);
		Card card2 = new Card(CardValue.FOUR, CardColor.CLOVER);
		Card card3 = new Card(CardValue.FIVE, CardColor.CLOVER);
		Card card4 = new Card(CardValue.SIX, CardColor.CLOVER);
		Card card5 = new Card(CardValue.SEVEN, CardColor.HEART);
		Card card6 = new Card(CardValue.TWO, CardColor.CLOVER);
		Card card7 = new Card(CardValue.THREE, CardColor.HEART);
		PlayerHand hand = new PlayerHand(Arrays.asList(card1, card2));
		Dealer dealerHand = new Dealer(Arrays.asList(card3, card4, card5, card6, card7));
		WinningCombination result = WinConditionLogic.findWinningCombination(dealerHand, hand);
		assertEquals(new WinningCombination(WinCondition.STRAIGHT_FLUSH, CardValue.SIX), result);

	}

	@Test
	void testNoStraightFlush() {
		Card card1 = new Card(CardValue.THREE, CardColor.CLOVER);
		Card card2 = new Card(CardValue.FOUR, CardColor.CLOVER);
		Card card3 = new Card(CardValue.FIVE, CardColor.CLOVER);
		Card card4 = new Card(CardValue.SIX, CardColor.CLOVER);
		Card card5 = new Card(CardValue.SEVEN, CardColor.SPADE);
		Card card6 = new Card(CardValue.TWO, CardColor.HEART);
		Card card7 = new Card(CardValue.THREE, CardColor.CLOVER);
		PlayerHand hand = new PlayerHand(Arrays.asList(card1, card2));
		Dealer dealerHand = new Dealer(Arrays.asList(card3, card4, card5, card6, card7));
		WinningCombination result = WinConditionLogic.findWinningCombination(dealerHand, hand);
		assertEquals(new WinningCombination(WinCondition.FLUSH, CardValue.SIX), result);

	}

	@Test
	void testRoyalFlush() {
		Card card1 = new Card(CardValue.TEN, CardColor.CLOVER);
		Card card2 = new Card(CardValue.JACK, CardColor.CLOVER);
		Card card3 = new Card(CardValue.QUEEN, CardColor.CLOVER);
		Card card4 = new Card(CardValue.KING, CardColor.CLOVER);
		Card card5 = new Card(CardValue.ACE, CardColor.CLOVER);
		Card card6 = new Card(CardValue.TWO, CardColor.SPADE);
		Card card7 = new Card(CardValue.THREE, CardColor.HEART);
		PlayerHand hand = new PlayerHand(Arrays.asList(card1, card2));
		Dealer dealerHand = new Dealer(Arrays.asList(card3, card4, card5, card6, card7));
		WinningCombination result = WinConditionLogic.findWinningCombination(dealerHand, hand);
		assertEquals(new WinningCombination(WinCondition.ROYAL_FLUSH, CardValue.ACE), result);

	}
	// ---------------------Testing for inverted cards logic --------------

	@Test
	void testHighCardInverted() {
		Card card1 = new Card(CardValue.TWO, CardColor.CLOVER);
		Card card2 = new Card(CardValue.THREE, CardColor.SPADE);
		Card card3 = new Card(CardValue.FIVE, CardColor.CLOVER);
		Card card4 = new Card(CardValue.SEVEN, CardColor.HEART);
		Card card5 = new Card(CardValue.KING, CardColor.DIAMOND);
		Card card6 = new Card(CardValue.SIX, CardColor.CLOVER);
		Card card7 = new Card(CardValue.EIGHT, CardColor.HEART);
		PlayerHand hand = new PlayerHand(Arrays.asList(card1, card2));
		Dealer dealerHand = new Dealer(Arrays.asList(card3, card4, card5, card6, card7));
		WinningCombination result = WinConditionLogic.findWinningCombination(CardColor.CLOVER, dealerHand, hand);
		assertEquals(new WinningCombination(WinCondition.HIGH_CARD, CardValue.ACE), result);

	}

	@Test
	void testPairInverted() {
		Card card1 = new Card(CardValue.TWO, CardColor.CLOVER);
		Card card2 = new Card(CardValue.THREE, CardColor.SPADE);
		Card card3 = new Card(CardValue.FIVE, CardColor.CLOVER);
		Card card4 = new Card(CardValue.SEVEN, CardColor.HEART);
		Card card5 = new Card(CardValue.KING, CardColor.DIAMOND);
		Card card6 = new Card(CardValue.SIX, CardColor.CLOVER);
		Card card7 = new Card(CardValue.EIGHT, CardColor.HEART);
		PlayerHand hand = new PlayerHand(Arrays.asList(card1, card2));
		Dealer dealerHand = new Dealer(Arrays.asList(card3, card4, card5, card6, card7));
		WinningCombination result = WinConditionLogic.findWinningCombination(CardColor.SPADE, dealerHand, hand);
		assertEquals(result, new WinningCombination(WinCondition.PAIR, CardValue.KING));

	}

	@Test
	void testTwoPairInverted() {
		Card card1 = new Card(CardValue.TWO, CardColor.CLOVER);
		Card card2 = new Card(CardValue.THREE, CardColor.SPADE);
		Card card3 = new Card(CardValue.NINE, CardColor.SPADE);
		Card card4 = new Card(CardValue.SEVEN, CardColor.HEART);
		Card card5 = new Card(CardValue.KING, CardColor.DIAMOND);
		Card card6 = new Card(CardValue.SIX, CardColor.CLOVER);
		Card card7 = new Card(CardValue.EIGHT, CardColor.HEART);
		PlayerHand hand = new PlayerHand(Arrays.asList(card1, card2));
		Dealer dealerHand = new Dealer(Arrays.asList(card3, card4, card5, card6, card7));
		WinningCombination result = WinConditionLogic.findWinningCombination(CardColor.SPADE, dealerHand, hand);
		assertEquals(result, new WinningCombination(WinCondition.TWO_PAIR, CardValue.KING));

	}

	@Test
	void testTOAKInverted() {
		Card card1 = new Card(CardValue.TWO, CardColor.CLOVER);
		Card card2 = new Card(CardValue.THREE, CardColor.SPADE);
		Card card3 = new Card(CardValue.KING, CardColor.SPADE);// this one will be inverted!
		Card card4 = new Card(CardValue.SEVEN, CardColor.HEART);
		Card card5 = new Card(CardValue.KING, CardColor.DIAMOND);
		Card card6 = new Card(CardValue.KING, CardColor.CLOVER);
		Card card7 = new Card(CardValue.EIGHT, CardColor.HEART);
		PlayerHand hand = new PlayerHand(Arrays.asList(card1, card2));
		Dealer dealerHand = new Dealer(Arrays.asList(card3, card4, card5, card6, card7));
		WinningCombination result = WinConditionLogic.findWinningCombination(CardColor.SPADE, dealerHand, hand);
		assertEquals(result, new WinningCombination(WinCondition.THREE_OF_A_KIND, CardValue.KING));

	}

	@Test
	void testNoCardChangeOnLogicUse() {
		Card card1 = new Card(CardValue.TWO, CardColor.SPADE);
		Card card2 = new Card(CardValue.THREE, CardColor.SPADE);
		Card card3 = new Card(CardValue.KING, CardColor.SPADE);// this one will be inverted!
		Card card4 = new Card(CardValue.SEVEN, CardColor.HEART);
		Card card5 = new Card(CardValue.KING, CardColor.DIAMOND);
		Card card6 = new Card(CardValue.KING, CardColor.CLOVER);
		Card card7 = new Card(CardValue.EIGHT, CardColor.HEART);
		PlayerHand hand = new PlayerHand(Arrays.asList(card1, card2));
		Dealer dealerHand = new Dealer(Arrays.asList(card3, card4, card5, card6, card7));
		WinConditionLogic.findWinningCombination(CardColor.SPADE, dealerHand, hand); // use logic
		// check that cards in player hand haven't been inverted and are still the same
		assertEquals(card1, new Card(CardValue.TWO, CardColor.SPADE));
		assertEquals(card2, new Card(CardValue.THREE, CardColor.SPADE));

	}

	@Test
	void testStraightNoLowAceInverted() {
		Card card1 = new Card(CardValue.NINE, CardColor.CLOVER);
		Card card2 = new Card(CardValue.FIVE, CardColor.SPADE);// inverted: jack
		Card card3 = new Card(CardValue.SIX, CardColor.SPADE);// inverted : 10
		Card card4 = new Card(CardValue.TWO, CardColor.HEART);
		Card card5 = new Card(CardValue.KING, CardColor.DIAMOND);
		Card card6 = new Card(CardValue.QUEEN, CardColor.CLOVER);
		Card card7 = new Card(CardValue.EIGHT, CardColor.HEART); // 8 9 10 J Q straight
		PlayerHand hand = new PlayerHand(Arrays.asList(card1, card2));
		Dealer dealerHand = new Dealer(Arrays.asList(card3, card4, card5, card6, card7));
		WinningCombination result = WinConditionLogic.findWinningCombination(CardColor.SPADE, dealerHand, hand);
		assertEquals(result, new WinningCombination(WinCondition.STRAIGHT, CardValue.KING));
	}
	// -----------------------------Everything seems to be working
	// fine!---------------------------------
}
