package fr.pantheonsorbonne.miage.game.classes.pokerTableStuff;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.pantheonsorbonne.miage.game.classes.cards.Card;
import fr.pantheonsorbonne.miage.game.classes.cards.CardColor;
import fr.pantheonsorbonne.miage.game.classes.cards.CardValue;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.PlayerBotSmarter;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.PlayerHand;
import fr.pantheonsorbonne.miage.game.classes.superpowers.SuperpowerAdd;
import fr.pantheonsorbonne.miage.game.classes.superpowers.SuperpowerAddHidden;
import fr.pantheonsorbonne.miage.game.classes.superpowers.SuperpowerDestroy;
import fr.pantheonsorbonne.miage.game.classes.superpowers.SuperpowerShow;


/*
 * Tests for PokerTable global functions(betting,giving cards,handling pots & wins,different player counts, kicking broke players,superpowers...)
 */
class PokerTableTest {

	@Test
	/**
	 * Tests that the base pot from <PokerTable> works correctly when no players
	 * are all in, with 2 players.
	 */
	void noAllInTest() {

		// Arrange

		Player player1 = new Player("Flavio", 50);
		Player player2 = new Player("Pablo", 50);
		PokerTable table = new PokerTableAutomatisee();
		List<Card> p1Hand = new ArrayList<>();
		p1Hand.add(new Card(CardValue.TEN, CardColor.CLOVER));
		p1Hand.add(new Card(CardValue.TEN, CardColor.DIAMOND));
		table.addPlayer(player1);
		table.addPlayer(player2);
		player1.setHand(new PlayerHand(p1Hand));
		List<Card> p2Hand = new ArrayList<>();
		p2Hand.add(new Card(CardValue.NINE, CardColor.CLOVER));
		p2Hand.add(new Card(CardValue.NINE, CardColor.HEART));
		player2.setHand(new PlayerHand(p2Hand));
		List<Card> dealerHand = new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE, CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX, CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN, CardColor.SPADE));
		table.getDealer().setHand(dealerHand);
		table.getPlayers().get(0).bet(40);
		table.getPlayers().get(1).bet(40);
		table.turnPots();

		Assertions.assertEquals(player1.getChipStack(), 90);
		Assertions.assertEquals(player2.getChipStack(), 10);
	}

	@Test
	/**
	 * Tests that <PokerTable> works correctly when both players are all-in with
	 * the same bet
	 */
	void bothAllInTest() {

		// arrange
		Player player1 = new Player("Flavio", 50);
		Player player2 = new Player("Pablo", 50);
		PokerTable table = new PokerTableAutomatisee();
		ArrayList<Card> p1Hand = new ArrayList<>();
		p1Hand.add(new Card(CardValue.TEN, CardColor.CLOVER));
		p1Hand.add(new Card(CardValue.TEN, CardColor.DIAMOND));
		table.addPlayer(player1);
		table.addPlayer(player2);
		player1.setHand(new PlayerHand(p1Hand));
		ArrayList<Card> p2Hand = new ArrayList<>();
		p2Hand.add(new Card(CardValue.NINE, CardColor.CLOVER));
		p2Hand.add(new Card(CardValue.NINE, CardColor.HEART));
		player2.setHand(new PlayerHand(p2Hand));
		ArrayList<Card> dealerHand = new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE, CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX, CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN, CardColor.SPADE));
		table.getDealer().setHand(dealerHand);

		// Both players bet their whole chipstack
		table.getPlayers().get(0).bet(50);
		table.getPlayers().get(1).bet(50);
		table.turnPots();

		// Assertions
		Assertions.assertEquals(player1.getChipStack(), 100);
		Assertions.assertEquals(player2.getChipStack(), 0);
	}

	@Test
	/**
	 * Tests if <PokerTable> works correctly when only one player is all-in,
	 * while the other has more chips. In that case, the winning player
	 * has more chips.
	 */
	void oneAllInTest() {
		// arrange
		Player player1 = new Player("Flavio", 100);
		Player player2 = new Player("Pablo", 50);
		PokerTable table = new PokerTableAutomatisee();
		ArrayList<Card> p1Hand = new ArrayList<>();
		p1Hand.add(new Card(CardValue.TEN, CardColor.CLOVER));
		p1Hand.add(new Card(CardValue.TEN, CardColor.DIAMOND));
		table.addPlayer(player1);
		table.addPlayer(player2);
		player1.setHand(new PlayerHand(p1Hand));
		ArrayList<Card> p2Hand = new ArrayList<>();
		p2Hand.add(new Card(CardValue.NINE, CardColor.CLOVER));
		p2Hand.add(new Card(CardValue.NINE, CardColor.HEART));
		player2.setHand(new PlayerHand(p2Hand));
		ArrayList<Card> dealerHand = new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE, CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX, CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN, CardColor.SPADE));
		table.getDealer().setHand(dealerHand);

		// Both players all-in but p1 has more chips.
		table.getPlayers().get(0).bet(100);
		table.getPlayers().get(1).bet(50);
		table.turnPots();

		// Assertions
		Assertions.assertEquals(player1.getChipStack(), 150);
		Assertions.assertEquals(player2.getChipStack(), 0);
	}

	@Test
	/**
	 * Tests if the <AllInPot> implementation of <PokerTable> is working fine.
	 * p1 all-ins with less chips than p2 and wins.
	 */
	void oneAllInSidePotTest() {
		// arrange
		Player player1 = new Player("Flavio", 50);
		Player player2 = new Player("Pablo", 100);
		PokerTable table = new PokerTableAutomatisee();
		ArrayList<Card> p1Hand = new ArrayList<>();
		p1Hand.add(new Card(CardValue.TEN, CardColor.CLOVER));
		p1Hand.add(new Card(CardValue.TEN, CardColor.DIAMOND));
		table.addPlayer(player1);
		table.addPlayer(player2);
		player1.setHand(new PlayerHand(p1Hand));
		ArrayList<Card> p2Hand = new ArrayList<>();
		p2Hand.add(new Card(CardValue.NINE, CardColor.CLOVER));
		p2Hand.add(new Card(CardValue.NINE, CardColor.HEART));
		player2.setHand(new PlayerHand(p2Hand));
		ArrayList<Card> dealerHand = new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE, CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX, CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN, CardColor.SPADE));
		table.getDealer().setHand(dealerHand);

		// Both players all-in but p1 has more chips.
		table.getPlayers().get(0).bet(100);
		table.getPlayers().get(1).bet(50);
		table.turnPots();

		// Assertions
		Assertions.assertEquals(player1.getChipStack(), 100);
		Assertions.assertEquals(player2.getChipStack(), 50);
	}

	// have different chipstacks.
	@Test
	/**
	 * Tests <PokerTable> pot system when 3 players are all-in with the same chips
	 */
	void threePlayersAllInSameBet() {
		Player player1 = new Player("Flavio", 100);
		Player player2 = new Player("Pablo", 100);
		Player player3 = new Player("Mingo", 100);
		PokerTable table = new PokerTableAutomatisee();
		ArrayList<Card> p1Hand = new ArrayList<>();
		p1Hand.add(new Card(CardValue.TEN, CardColor.CLOVER));
		p1Hand.add(new Card(CardValue.TEN, CardColor.DIAMOND));
		player1.setHand(new PlayerHand(p1Hand));
		ArrayList<Card> p2Hand = new ArrayList<>();
		p2Hand.add(new Card(CardValue.NINE, CardColor.CLOVER));
		p2Hand.add(new Card(CardValue.NINE, CardColor.HEART));
		player2.setHand(new PlayerHand(p2Hand));
		ArrayList<Card> p3Hand = new ArrayList<>();
		p3Hand.add(new Card(CardValue.JACK, CardColor.CLOVER));
		p3Hand.add(new Card(CardValue.JACK, CardColor.SPADE));
		player3.setHand(new PlayerHand(p3Hand));
		table.addPlayer(player1);
		table.addPlayer(player2);
		table.addPlayer(player3);
		ArrayList<Card> dealerHand = new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE, CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX, CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN, CardColor.SPADE));
		table.getDealer().setHand(dealerHand);

		// Both players all-in but p1 has more chips.
		table.getPlayers().get(0).bet(100);
		table.getPlayers().get(1).bet(100);
		table.getPlayers().get(2).bet(100);
		table.turnPots();

		// Assertions
		Assertions.assertEquals(player1.getChipStack(), 0);
		Assertions.assertEquals(player2.getChipStack(), 0);
		Assertions.assertEquals(player3.getChipStack(), 300);
	}

	@Test
	/**
	 * Flavio has less chips than Pablo and Mingo. Mingo wins, he should get
	 * 50+60+60
	 * Pablo and Flavio should be broke.
	 */
	void ThreePlayersDifferentChipsAllIn() {
		Player player1 = new Player("Flavio", 50);
		Player player2 = new Player("Pablo", 60);
		Player player3 = new Player("Mingo", 60);
		PokerTable table = new PokerTableAutomatisee();
		ArrayList<Card> p1Hand = new ArrayList<>();
		p1Hand.add(new Card(CardValue.TEN, CardColor.CLOVER));
		p1Hand.add(new Card(CardValue.TEN, CardColor.DIAMOND));
		player1.setHand(new PlayerHand(p1Hand));
		ArrayList<Card> p2Hand = new ArrayList<>();
		p2Hand.add(new Card(CardValue.NINE, CardColor.CLOVER));
		p2Hand.add(new Card(CardValue.NINE, CardColor.HEART));
		player2.setHand(new PlayerHand(p2Hand));
		ArrayList<Card> p3Hand = new ArrayList<>();
		p3Hand.add(new Card(CardValue.JACK, CardColor.CLOVER));
		p3Hand.add(new Card(CardValue.JACK, CardColor.SPADE));
		player3.setHand(new PlayerHand(p3Hand));
		table.addPlayer(player1);
		table.addPlayer(player2);
		table.addPlayer(player3);
		ArrayList<Card> dealerHand = new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE, CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX, CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN, CardColor.SPADE));
		table.getDealer().setHand(dealerHand);

		// Both players all-in but p1 has more chips.
		table.getPlayers().get(0).bet(100);
		table.getPlayers().get(1).bet(100);
		table.getPlayers().get(2).bet(100);
		table.turnPots();

		// Assertions
		Assertions.assertEquals(player1.getChipStack(), 0);
		Assertions.assertEquals(player2.getChipStack(), 0);
		Assertions.assertEquals(player3.getChipStack(), 170);
	}

	@Test
	/**
	 * Flavio and Pablo lose, so Mingo should get 50+60+60 and Pablo should get 40
	 * back.
	 */
	void ThreePlayersDifferentChipsTwoAreAllIn() {
		Player player1 = new Player("Flavio", 50);
		Player player2 = new Player("Pablo", 100);
		Player player3 = new Player("Mingo", 60);
		PokerTable table = new PokerTableAutomatisee();
		ArrayList<Card> p1Hand = new ArrayList<>();
		p1Hand.add(new Card(CardValue.TEN, CardColor.CLOVER));
		p1Hand.add(new Card(CardValue.TEN, CardColor.DIAMOND));
		player1.setHand(new PlayerHand(p1Hand));
		ArrayList<Card> p2Hand = new ArrayList<>();
		p2Hand.add(new Card(CardValue.NINE, CardColor.CLOVER));
		p2Hand.add(new Card(CardValue.NINE, CardColor.HEART));
		player2.setHand(new PlayerHand(p2Hand));
		ArrayList<Card> p3Hand = new ArrayList<>();
		p3Hand.add(new Card(CardValue.JACK, CardColor.CLOVER));
		p3Hand.add(new Card(CardValue.JACK, CardColor.SPADE));
		player3.setHand(new PlayerHand(p3Hand));
		table.addPlayer(player1);
		table.addPlayer(player2);
		table.addPlayer(player3);
		ArrayList<Card> dealerHand = new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE, CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX, CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN, CardColor.SPADE));
		table.getDealer().setHand(dealerHand);

		// Both players all-in but p1 has more chips.
		table.getPlayers().get(0).bet(100);
		table.getPlayers().get(1).bet(100);
		table.getPlayers().get(2).bet(100);
		table.turnPots();

		// Assertions
		Assertions.assertEquals(player1.getChipStack(), 0);
		Assertions.assertEquals(player2.getChipStack(), 40);
		Assertions.assertEquals(player3.getChipStack(), 170);
	}

	@Test
	/**
	 * Fourth player wins
	 */
	void FourPlayersLastWins() {
		Player player1 = new Player("Flavio", 200);
		Player player2 = new Player("Pablo", 200);
		Player player3 = new Player("Mingo", 200);
		Player player4 = new Player("Oslo", 200);
		PokerTableAutomatisee table = new PokerTableAutomatisee();
		List<Card> p1Hand = new ArrayList<>();
		p1Hand.add(new Card(CardValue.TEN, CardColor.CLOVER));
		p1Hand.add(new Card(CardValue.TEN, CardColor.DIAMOND));
		player1.setHand(new PlayerHand(p1Hand));
		List<Card> p2Hand = new ArrayList<>();
		p2Hand.add(new Card(CardValue.NINE, CardColor.CLOVER));
		p2Hand.add(new Card(CardValue.NINE, CardColor.HEART));
		player2.setHand(new PlayerHand(p2Hand));
		List<Card> p3Hand = new ArrayList<>();
		p3Hand.add(new Card(CardValue.JACK, CardColor.CLOVER));
		p3Hand.add(new Card(CardValue.JACK, CardColor.SPADE));
		player3.setHand(new PlayerHand(p3Hand));
		List<Card> p4Hand = new ArrayList<>();
		p4Hand.add(new Card(CardValue.QUEEN, CardColor.CLOVER));
		p4Hand.add(new Card(CardValue.QUEEN, CardColor.SPADE));
		player4.setHand(new PlayerHand(p4Hand));
		table.addPlayer(player1);
		table.addPlayer(player2);
		table.addPlayer(player3);
		table.addPlayer(player4);
		List<Card> dealerHand = new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE, CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX, CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN, CardColor.SPADE));
		table.getDealer().setHand(dealerHand);

		table.getPlayers().get(0).bet(200);
		table.getPlayers().get(1).bet(200);
		table.getPlayers().get(2).bet(200);
		table.getPlayers().get(3).bet(200);
		table.turnPots();

		// assert
		assertEquals(player1.getChipStack(), 0);
		assertEquals(player2.getChipStack(), 0);
		assertEquals(player3.getChipStack(), 0);
		assertEquals(player4.getChipStack(), 800);
	}

	@Test
	/**
	 * Third player wins
	 */
	void FourPlayersThirdWins() {
		Player player1 = new Player("Flavio", 200);
		Player player2 = new Player("Pablo", 200);
		Player player3 = new Player("Mingo", 200);
		Player player4 = new Player("Oslo", 200);
		PokerTableAutomatisee table = new PokerTableAutomatisee();
		List<Card> p1Hand = new ArrayList<>();
		p1Hand.add(new Card(CardValue.TEN, CardColor.CLOVER));
		p1Hand.add(new Card(CardValue.TEN, CardColor.DIAMOND));
		player1.setHand(new PlayerHand(p1Hand));
		List<Card> p2Hand = new ArrayList<>();
		p2Hand.add(new Card(CardValue.NINE, CardColor.CLOVER));
		p2Hand.add(new Card(CardValue.NINE, CardColor.HEART));
		player2.setHand(new PlayerHand(p2Hand));
		List<Card> p3Hand = new ArrayList<>();
		p3Hand.add(new Card(CardValue.QUEEN, CardColor.CLOVER));
		p3Hand.add(new Card(CardValue.QUEEN, CardColor.SPADE));
		player3.setHand(new PlayerHand(p3Hand));
		List<Card> p4Hand = new ArrayList<>();
		p4Hand.add(new Card(CardValue.JACK, CardColor.CLOVER));
		p4Hand.add(new Card(CardValue.JACK, CardColor.SPADE));
		player4.setHand(new PlayerHand(p4Hand));
		table.addPlayer(player1);
		table.addPlayer(player2);
		table.addPlayer(player3);
		table.addPlayer(player4);
		List<Card> dealerHand = new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE, CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX, CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN, CardColor.SPADE));
		table.getDealer().setHand(dealerHand);

		table.getPlayers().get(0).bet(200);
		table.getPlayers().get(1).bet(200);
		table.getPlayers().get(2).bet(200);
		table.getPlayers().get(3).bet(200);
		table.turnPots();

		// assert
		assertEquals(player1.getChipStack(), 0);
		assertEquals(player2.getChipStack(), 0);
		assertEquals(player3.getChipStack(), 800);
		assertEquals(player4.getChipStack(), 0);
	}

	@Test
	/**
	 * Second player wins
	 */
	void FourPlayersSecondWins() {
		Player player1 = new Player("Flavio", 200);
		Player player2 = new Player("Pablo", 200);
		Player player3 = new Player("Mingo", 200);
		Player player4 = new Player("Oslo", 200);
		PokerTableAutomatisee table = new PokerTableAutomatisee();
		List<Card> p1Hand = new ArrayList<>();
		p1Hand.add(new Card(CardValue.TEN, CardColor.CLOVER));
		p1Hand.add(new Card(CardValue.TEN, CardColor.DIAMOND));
		player1.setHand(new PlayerHand(p1Hand));
		List<Card> p2Hand = new ArrayList<>();
		p2Hand.add(new Card(CardValue.QUEEN, CardColor.CLOVER));
		p2Hand.add(new Card(CardValue.QUEEN, CardColor.HEART));
		player2.setHand(new PlayerHand(p2Hand));
		List<Card> p3Hand = new ArrayList<>();
		p3Hand.add(new Card(CardValue.NINE, CardColor.CLOVER));
		p3Hand.add(new Card(CardValue.NINE, CardColor.SPADE));
		player3.setHand(new PlayerHand(p3Hand));
		List<Card> p4Hand = new ArrayList<>();
		p4Hand.add(new Card(CardValue.JACK, CardColor.CLOVER));
		p4Hand.add(new Card(CardValue.JACK, CardColor.SPADE));
		player4.setHand(new PlayerHand(p4Hand));
		table.addPlayer(player1);
		table.addPlayer(player2);
		table.addPlayer(player3);
		table.addPlayer(player4);
		List<Card> dealerHand = new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE, CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX, CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN, CardColor.SPADE));
		table.getDealer().setHand(dealerHand);

		table.getPlayers().get(0).bet(200);
		table.getPlayers().get(1).bet(200);
		table.getPlayers().get(2).bet(200);
		table.getPlayers().get(3).bet(200);
		table.turnPots();

		// assert
		assertEquals(player1.getChipStack(), 0);
		assertEquals(player2.getChipStack(), 800);
		assertEquals(player3.getChipStack(), 0);
		assertEquals(player4.getChipStack(), 0);
	}

	@Test
	/**
	 * First player wins
	 */
	void FourPlayersFirstWins() {
		Player player1 = new Player("Flavio", 200);
		Player player2 = new Player("Pablo", 200);
		Player player3 = new Player("Mingo", 200);
		Player player4 = new Player("Oslo", 200);
		PokerTableAutomatisee table = new PokerTableAutomatisee();
		List<Card> p1Hand = new ArrayList<>();
		p1Hand.add(new Card(CardValue.QUEEN, CardColor.CLOVER));
		p1Hand.add(new Card(CardValue.QUEEN, CardColor.DIAMOND));
		player1.setHand(new PlayerHand(p1Hand));
		List<Card> p2Hand = new ArrayList<>();
		p2Hand.add(new Card(CardValue.TEN, CardColor.CLOVER));
		p2Hand.add(new Card(CardValue.TEN, CardColor.HEART));
		player2.setHand(new PlayerHand(p2Hand));
		List<Card> p3Hand = new ArrayList<>();
		p3Hand.add(new Card(CardValue.NINE, CardColor.CLOVER));
		p3Hand.add(new Card(CardValue.NINE, CardColor.SPADE));
		player3.setHand(new PlayerHand(p3Hand));
		List<Card> p4Hand = new ArrayList<>();
		p4Hand.add(new Card(CardValue.JACK, CardColor.CLOVER));
		p4Hand.add(new Card(CardValue.JACK, CardColor.SPADE));
		player4.setHand(new PlayerHand(p4Hand));
		table.addPlayer(player1);
		table.addPlayer(player2);
		table.addPlayer(player3);
		table.addPlayer(player4);
		List<Card> dealerHand = new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE, CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX, CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN, CardColor.SPADE));
		table.getDealer().setHand(dealerHand);

		table.getPlayers().get(0).bet(200);
		table.getPlayers().get(1).bet(200);
		table.getPlayers().get(2).bet(200);
		table.getPlayers().get(3).bet(200);
		table.turnPots();

		// assert
		assertEquals(player1.getChipStack(), 800);
		assertEquals(player2.getChipStack(), 0);
		assertEquals(player3.getChipStack(), 0);
		assertEquals(player4.getChipStack(), 0);
	}

	@Test
	/**
	 * Only 2 players left when started with 4
	 */
	void twoLeftOutOfFour() {
		Player player1 = new Player("Flavio", 400);
		Player player2 = new Player("Pablo", 0);
		Player player3 = new Player("Mingo", 400);
		Player player4 = new Player("Oslo", 0);
		PokerTableAutomatisee table = new PokerTableAutomatisee();
		List<Card> p1Hand = new ArrayList<>();
		p1Hand.add(new Card(CardValue.QUEEN, CardColor.CLOVER));
		p1Hand.add(new Card(CardValue.QUEEN, CardColor.DIAMOND));
		player1.setHand(new PlayerHand(p1Hand));
		List<Card> p2Hand = new ArrayList<>();
		p2Hand.add(new Card(CardValue.TEN, CardColor.CLOVER));
		p2Hand.add(new Card(CardValue.TEN, CardColor.HEART));
		player2.setHand(new PlayerHand(p2Hand));
		List<Card> p3Hand = new ArrayList<>();
		p3Hand.add(new Card(CardValue.NINE, CardColor.CLOVER));
		p3Hand.add(new Card(CardValue.NINE, CardColor.SPADE));
		player3.setHand(new PlayerHand(p3Hand));
		List<Card> p4Hand = new ArrayList<>();
		p4Hand.add(new Card(CardValue.JACK, CardColor.CLOVER));
		p4Hand.add(new Card(CardValue.JACK, CardColor.SPADE));
		player4.setHand(new PlayerHand(p4Hand));
		table.addPlayer(player1);
		table.addPlayer(player2);
		table.addPlayer(player3);
		table.addPlayer(player4);
		List<Card> dealerHand = new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE, CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX, CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN, CardColor.SPADE));
		table.getDealer().setHand(dealerHand);

		table.getPlayers().get(0).bet(200);
		table.getPlayers().get(1).bet(200);
		table.turnPots();

		// assert
		assertEquals(player1.getChipStack(), 600);
		assertEquals(player2.getChipStack(), 0);
		assertEquals(player3.getChipStack(), 200);
		assertEquals(player4.getChipStack(), 0);
	}

	@Test
	void superpowerTestAdd() {
		// PlayerBotSmarter always uses the add superpower
		Player player1 = new PlayerBotSmarter("Flavio", 400);
		Player player2 = new PlayerBotSmarter("Pablo", 400);
		Player player3 = new PlayerBotSmarter("Mingo", 400);
		Player player4 = new PlayerBotSmarter("Oslo", 400);
		List<Player> playerList = Arrays.asList(player1, player2, player3, player4);
		PokerTableAutomatisee table = new PokerTableAutomatisee(playerList);
		table.startTurnWithPots();
		// testing that every player was able to use the superpower, and that the card
		// was added
		assertEquals(table.getSuperpowerUseNumber("add"), 4);
		assertEquals(player1.getPlayerHand().getHand().size(), 3);
	}

	@Test
	void superpowerTest() {
		// PlayerBotSmarter always uses the add superpower
		Player player1 = new PlayerBotSmarter("Flavio", 400);
		Player player2 = new PlayerBotSmarter("Pablo", 400);
		Player player3 = new PlayerBotSmarter("Mingo", 400);
		Player player4 = new PlayerBotSmarter("Oslo", 400);
		List<Player> playerList = Arrays.asList(player1, player2, player3, player4);
		PokerTableAutomatisee table = new PokerTableAutomatisee(playerList);
		table.startTurnWithPots();
		assertEquals(table.getSuperpowerUseNumber("add"), 4);
		assertEquals(player1.getPlayerHand().getHand().size(), 3);
		assertEquals(player2.getPlayerHand().getHand().size(), 3);
		assertEquals(player3.getPlayerHand().getHand().size(), 3);
		assertEquals(player4.getPlayerHand().getHand().size(), 3);
		// Test that table is reset
		table.resetTable();
		assertEquals(table.getNumberOfPots(), 0);
		// play till the end
		table.play();
		// test that theres no weird bug and only one player is left after playing
		assertEquals(table.gameContinues(), false);
	}

	@Test
	void testPlay() {
		Player player1 = new PlayerBotSmarter("Flavio", 400);
		Player player2 = new PlayerBotSmarter("Pablo", 400);
		Player player3 = new PlayerBotSmarter("Mingo", 400);
		Player player4 = new PlayerBotSmarter("Oslo", 400);
		List<Player> playerList = Arrays.asList(player1, player2, player3, player4);
		PokerTableAutomatisee table = new PokerTableAutomatisee(playerList);
		table.play();
		assertEquals(table.gameContinues(), false);// game ended
		assertEquals(table.getPlayers().size(), 1);// 1 player won
	}

	@Test
	void testInvertedColors() {
		Player player1 = new PlayerBotSmarter("Flavio", 400);
		Player player2 = new PlayerBotSmarter("Pablo", 400);
		Player player3 = new PlayerBotSmarter("Mingo", 400);
		Player player4 = new PlayerBotSmarter("Oslo", 400);
		List<Player> playerList = Arrays.asList(player1, player2, player3, player4);
		PokerTableAutomatisee table = new PokerTableAutomatisee(playerList);
		table.setInvertedColor(0);
		assertEquals(table.getInvertedColor(), CardColor.SPADE);
		table.setInvertedColor(1);
		assertEquals(table.getInvertedColor(), CardColor.HEART);
		table.setInvertedColor(2);
		assertEquals(table.getInvertedColor(), CardColor.DIAMOND);
		table.setInvertedColor(3);
		assertEquals(table.getInvertedColor(), CardColor.CLOVER);
	}

	@Test
	void testCallRaiseFold() {
		Player player1 = new PlayerBotSmarter("Flavio", 400);
		Player player2 = new PlayerBotSmarter("Pablo", 400);
		Player player3 = new PlayerBotSmarter("Mingo", 400);
		Player player4 = new PlayerBotSmarter("Oslo", 400);
		List<Player> playerList = Arrays.asList(player1, player2, player3, player4);
		PokerTableAutomatisee table = new PokerTableAutomatisee(playerList);
		table.raise(player1, 100);
		assertEquals(table.getPlayers().get(0).getBet(), 100);
		assertEquals(true, player1.isCurrentlyRaising());
		table.call(player2);
		assertEquals(table.getPlayers().get(1).getBet(), 100);
		table.fold(player3);
		assertEquals(table.getPlayers().get(2).hasNotFolded(), false);
		table.raise(player4, -50);
		assertEquals(table.getPlayers().get(3).getBet(), 100);

	}

	@Test
	void testUpdateShownCards() {
		Player player1 = new PlayerBotSmarter("Flavio", 400);
		Player player2 = new PlayerBotSmarter("Pablo", 400);
		Player player3 = new PlayerBotSmarter("Mingo", 400);
		Player player4 = new PlayerBotSmarter("Oslo", 400);
		List<Player> playerList = Arrays.asList(player1, player2, player3, player4);
		PokerTableAutomatisee table = new PokerTableAutomatisee(playerList);
		table.giveCards();
		assertEquals(table.getPlayers().get(0).getCardsKnownFromOtherPlayers().size(), 0);// Map is empty bc not
																							// initialized
		assertEquals(table.getPlayers().get(1).getCardsKnownFromOtherPlayers().size(), 0);
		assertEquals(table.getPlayers().get(2).getCardsKnownFromOtherPlayers().size(), 0);
		assertEquals(table.getPlayers().get(3).getCardsKnownFromOtherPlayers().size(), 0);
		table.updateShownCards(); // no cards should be shown
		// Each player knows about the existence of 3 other players...
		assertEquals(table.getPlayers().get(0).getCardsKnownFromOtherPlayers().size(), 3);
		assertEquals(table.getPlayers().get(1).getCardsKnownFromOtherPlayers().size(), 3);
		assertEquals(table.getPlayers().get(2).getCardsKnownFromOtherPlayers().size(), 3);
		assertEquals(table.getPlayers().get(3).getCardsKnownFromOtherPlayers().size(), 3);
		// But no card is known
		assertEquals(table.getPlayers().get(0).getCardsKnownFromOtherPlayers().get(player2).size(), 0);
		assertEquals(table.getPlayers().get(0).getCardsKnownFromOtherPlayers().get(player3).size(), 0);
		assertEquals(table.getPlayers().get(0).getCardsKnownFromOtherPlayers().get(player4).size(), 0);
		assertEquals(table.getPlayers().get(1).getCardsKnownFromOtherPlayers().get(player1).size(), 0);
		assertEquals(table.getPlayers().get(1).getCardsKnownFromOtherPlayers().get(player3).size(), 0);
		assertEquals(table.getPlayers().get(1).getCardsKnownFromOtherPlayers().get(player4).size(), 0);
		assertEquals(table.getPlayers().get(2).getCardsKnownFromOtherPlayers().get(player1).size(), 0);
		assertEquals(table.getPlayers().get(2).getCardsKnownFromOtherPlayers().get(player2).size(), 0);
		assertEquals(table.getPlayers().get(2).getCardsKnownFromOtherPlayers().get(player4).size(), 0);
		assertEquals(table.getPlayers().get(3).getCardsKnownFromOtherPlayers().get(player1).size(), 0);
		assertEquals(table.getPlayers().get(3).getCardsKnownFromOtherPlayers().get(player2).size(), 0);
		assertEquals(table.getPlayers().get(3).getCardsKnownFromOtherPlayers().get(player3).size(), 0);
		table.getPlayers().get(0).showRandomCard();
		table.getPlayers().get(1).showRandomCard();
		table.getPlayers().get(2).showRandomCard();
		table.getPlayers().get(3).showRandomCard();
		table.updateShownCards(); // each player should know 1 card from 3 other players
		assertEquals(table.getPlayers().get(0).getCardsKnownFromOtherPlayers().size(), 3);
		assertEquals(table.getPlayers().get(1).getCardsKnownFromOtherPlayers().size(), 3);
		assertEquals(table.getPlayers().get(2).getCardsKnownFromOtherPlayers().size(), 3);
		assertEquals(table.getPlayers().get(3).getCardsKnownFromOtherPlayers().size(), 3);
		assertEquals(table.getPlayers().get(3).getCardsKnownFromOtherPlayers().get(player1).size(), 1);
		assertEquals(table.getPlayers().get(3).getCardsKnownFromOtherPlayers().get(player2).size(), 1);
		assertEquals(table.getPlayers().get(3).getCardsKnownFromOtherPlayers().get(player3).size(), 1);
		assertEquals(table.getPlayers().get(2).getCardsKnownFromOtherPlayers().get(player1).size(), 1);
		assertEquals(table.getPlayers().get(2).getCardsKnownFromOtherPlayers().get(player2).size(), 1);
		assertEquals(table.getPlayers().get(2).getCardsKnownFromOtherPlayers().get(player4).size(), 1);
		assertEquals(table.getPlayers().get(1).getCardsKnownFromOtherPlayers().get(player1).size(), 1);
		assertEquals(table.getPlayers().get(1).getCardsKnownFromOtherPlayers().get(player3).size(), 1);
		assertEquals(table.getPlayers().get(1).getCardsKnownFromOtherPlayers().get(player4).size(), 1);
		assertEquals(table.getPlayers().get(0).getCardsKnownFromOtherPlayers().get(player4).size(), 1);
		assertEquals(table.getPlayers().get(0).getCardsKnownFromOtherPlayers().get(player3).size(), 1);
		assertEquals(table.getPlayers().get(0).getCardsKnownFromOtherPlayers().get(player2).size(), 1);
		table.getPlayers().get(0).showRandomCard();
		table.getPlayers().get(1).showRandomCard();
		table.getPlayers().get(2).showRandomCard();
		table.getPlayers().get(3).showRandomCard();
		table.updateShownCards(); // each player should know 2 cards from 3 other players
		assertEquals(table.getPlayers().get(0).getCardsKnownFromOtherPlayers().size(), 3);
		assertEquals(table.getPlayers().get(1).getCardsKnownFromOtherPlayers().size(), 3);
		assertEquals(table.getPlayers().get(2).getCardsKnownFromOtherPlayers().size(), 3);
		assertEquals(table.getPlayers().get(3).getCardsKnownFromOtherPlayers().size(), 3);
		assertEquals(table.getPlayers().get(3).getCardsKnownFromOtherPlayers().get(player1).size(), 2);
		assertEquals(table.getPlayers().get(3).getCardsKnownFromOtherPlayers().get(player2).size(), 2);
		assertEquals(table.getPlayers().get(3).getCardsKnownFromOtherPlayers().get(player3).size(), 2);
		assertEquals(table.getPlayers().get(2).getCardsKnownFromOtherPlayers().get(player1).size(), 2);
		assertEquals(table.getPlayers().get(2).getCardsKnownFromOtherPlayers().get(player2).size(), 2);
		assertEquals(table.getPlayers().get(2).getCardsKnownFromOtherPlayers().get(player4).size(), 2);
		assertEquals(table.getPlayers().get(1).getCardsKnownFromOtherPlayers().get(player1).size(), 2);
		assertEquals(table.getPlayers().get(1).getCardsKnownFromOtherPlayers().get(player3).size(), 2);
		assertEquals(table.getPlayers().get(1).getCardsKnownFromOtherPlayers().get(player4).size(), 2);
		assertEquals(table.getPlayers().get(0).getCardsKnownFromOtherPlayers().get(player4).size(), 2);
		assertEquals(table.getPlayers().get(0).getCardsKnownFromOtherPlayers().get(player3).size(), 2);
		assertEquals(table.getPlayers().get(0).getCardsKnownFromOtherPlayers().get(player2).size(), 2);

	}

	@Test
	void testSuperpowerAdd() {
		Player player1 = new PlayerBotSmarter("Flavio", 400);
		Player player2 = new PlayerBotSmarter("Pablo", 400);
		Player player3 = new PlayerBotSmarter("Mingo", 400);
		Player player4 = new PlayerBotSmarter("Oslo", 400);
		Player player5 = new PlayerBotSmarter("Rodrigo", 10);
		List<Player> playerList = Arrays.asList(player1, player2, player3, player4, player5);
		PokerTableAutomatisee table = new PokerTableAutomatisee(playerList);
		table.initializeSuperpowers();
		// Test that the superpower is initialized and never used
		assertEquals(table.getSuperpowerUseNumber("add"), 0);
		assertEquals(table.getSuperpowerUseNumber("randomNonsense"), -1);
		table.giveCards();
		table.useSuperpower(player1, "add");
		table.useSuperpower(player2, "add");
		table.useSuperpower(player3, "add");
		table.useSuperpower(player4, "add");
		table.useSuperpower(player5, "add");
		assertEquals(table.getSuperpowerUseNumber("add"), 4); // p5 didn't use it (no chips)
		assertEquals(player1.getChipStack(), 400 - SuperpowerAdd.getCost());
		assertEquals(player2.getChipStack(), 400 - SuperpowerAdd.getCost());
		assertEquals(player3.getChipStack(), 400 - SuperpowerAdd.getCost());
		assertEquals(player4.getChipStack(), 400 - SuperpowerAdd.getCost());
		assertEquals(player5.getChipStack(), 10);
		// check that the cards are known by the bots
		table.updateShownCards();
		assertEquals(table.getPlayers().get(0).getCardsKnownFromOtherPlayers().get(player2).size(), 1);
		assertEquals(table.getPlayers().get(0).getCardsKnownFromOtherPlayers().get(player3).size(), 1);
		assertEquals(table.getPlayers().get(0).getCardsKnownFromOtherPlayers().get(player4).size(), 1);
		assertEquals(table.getPlayers().get(1).getCardsKnownFromOtherPlayers().get(player1).size(), 1);
		assertEquals(table.getPlayers().get(1).getCardsKnownFromOtherPlayers().get(player3).size(), 1);
		assertEquals(table.getPlayers().get(1).getCardsKnownFromOtherPlayers().get(player4).size(), 1);
		assertEquals(table.getPlayers().get(2).getCardsKnownFromOtherPlayers().get(player1).size(), 1);
		assertEquals(table.getPlayers().get(2).getCardsKnownFromOtherPlayers().get(player2).size(), 1);
		assertEquals(table.getPlayers().get(2).getCardsKnownFromOtherPlayers().get(player4).size(), 1);
		assertEquals(table.getPlayers().get(3).getCardsKnownFromOtherPlayers().get(player1).size(), 1);
		assertEquals(table.getPlayers().get(3).getCardsKnownFromOtherPlayers().get(player2).size(), 1);
		assertEquals(table.getPlayers().get(3).getCardsKnownFromOtherPlayers().get(player3).size(), 1);

		// check that the players all have 3 cards
		assertEquals(table.getPlayers().get(0).getPlayerHand().getHand().size(), 3);
		assertEquals(table.getPlayers().get(1).getPlayerHand().getHand().size(), 3);
		assertEquals(table.getPlayers().get(2).getPlayerHand().getHand().size(), 3);
		assertEquals(table.getPlayers().get(3).getPlayerHand().getHand().size(), 3);

	}

	@Test
	void testSuperpowerAddHidden() {
		Player player1 = new PlayerBotSmarter("Flavio", 400);
		Player player2 = new PlayerBotSmarter("Pablo", 400);
		Player player3 = new PlayerBotSmarter("Mingo", 400);
		Player player4 = new PlayerBotSmarter("Oslo", 400);
		Player player5 = new PlayerBotSmarter("Rodrigo", 10);
		List<Player> playerList = Arrays.asList(player1, player2, player3, player4, player5);
		PokerTableAutomatisee table = new PokerTableAutomatisee(playerList);
		table.initializeSuperpowers();
		assertEquals(table.getSuperpowerUseNumber("addHidden"), 0);
		assertEquals(table.getSuperpowerUseNumber("randomNonsense"), -1);
		table.giveCards();
		table.useSuperpower(player1, "addHidden");
		table.useSuperpower(player2, "addHidden");
		table.useSuperpower(player3, "addHidden");
		table.useSuperpower(player4, "addHidden");
		table.useSuperpower(player5, "addHidden");
		assertEquals(table.getSuperpowerUseNumber("addHidden"), 4);
		assertEquals(player1.getChipStack(), 400 - SuperpowerAddHidden.getCost());
		assertEquals(player2.getChipStack(), 400 - SuperpowerAddHidden.getCost());
		assertEquals(player3.getChipStack(), 400 - SuperpowerAddHidden.getCost());
		assertEquals(player4.getChipStack(), 400 - SuperpowerAddHidden.getCost());
		assertEquals(player5.getChipStack(), 10);
		assertEquals(table.getPlayers().get(0).getPlayerHand().getHand().size(), 3);
		assertEquals(table.getPlayers().get(1).getPlayerHand().getHand().size(), 3);
		assertEquals(table.getPlayers().get(2).getPlayerHand().getHand().size(), 3);
		assertEquals(table.getPlayers().get(3).getPlayerHand().getHand().size(), 3);
		assertEquals(table.getPlayers().get(4).getPlayerHand().getHand().size(), 2);

		table.updateShownCards();
		assertEquals(table.getPlayers().get(0).getCardsKnownFromOtherPlayers().get(player2).size(), 0);
		assertEquals(table.getPlayers().get(0).getCardsKnownFromOtherPlayers().get(player3).size(), 0);
		assertEquals(table.getPlayers().get(0).getCardsKnownFromOtherPlayers().get(player4).size(), 0);
		assertEquals(table.getPlayers().get(1).getCardsKnownFromOtherPlayers().get(player1).size(), 0);
		assertEquals(table.getPlayers().get(1).getCardsKnownFromOtherPlayers().get(player3).size(), 0);
		assertEquals(table.getPlayers().get(1).getCardsKnownFromOtherPlayers().get(player4).size(), 0);
		assertEquals(table.getPlayers().get(2).getCardsKnownFromOtherPlayers().get(player1).size(), 0);
		assertEquals(table.getPlayers().get(2).getCardsKnownFromOtherPlayers().get(player2).size(), 0);
		assertEquals(table.getPlayers().get(2).getCardsKnownFromOtherPlayers().get(player4).size(), 0);
		assertEquals(table.getPlayers().get(3).getCardsKnownFromOtherPlayers().get(player1).size(), 0);
		assertEquals(table.getPlayers().get(3).getCardsKnownFromOtherPlayers().get(player2).size(), 0);
		assertEquals(table.getPlayers().get(3).getCardsKnownFromOtherPlayers().get(player3).size(), 0);
	}

	@Test
	void testSuperpowerDestroy() {
		Player player1 = new PlayerBotSmarter("Flavio", 400);
		Player player2 = new PlayerBotSmarter("Pablo", 400);
		Player player3 = new PlayerBotSmarter("Mingo", 400);
		Player player4 = new PlayerBotSmarter("Oslo", 400);
		Player player5 = new PlayerBotSmarter("Rodrigo", 10);
		List<Player> playerList = Arrays.asList(player1, player2, player3, player4, player5);
		PokerTableAutomatisee table = new PokerTableAutomatisee(playerList);
		table.initializeSuperpowers();
		assertEquals(table.getSuperpowerUseNumber("destroy"), 0);
		assertEquals(table.getSuperpowerUseNumber("randomNonsense"), -1);
		table.giveCards();
		//Player that gets destroyed is random so let's handle that!
		Player removed1 = table.useSuperpower(player1, "destroy");
		Player removed2 = table.useSuperpower(player2, "destroy");
		Player removed3 = table.useSuperpower(player3, "destroy");
		Player removed4 = table.useSuperpower(player4, "destroy");
		Player removed5 = table.useSuperpower(player5, "destroy");
		//We know that 4 players used that superpoweer, and the last couldn't use it
		assertEquals(table.getSuperpowerUseNumber("destroy"), 4);
		assertEquals(player1.getChipStack(), 400 - SuperpowerDestroy.getCost());
		assertEquals(player2.getChipStack(), 400 - SuperpowerDestroy.getCost());
		assertEquals(player3.getChipStack(), 400 - SuperpowerDestroy.getCost());
		assertEquals(player4.getChipStack(), 400 - SuperpowerDestroy.getCost());
		assertEquals(player5.getChipStack(), 10);
		List<Player> players = new ArrayList<>();
		assertEquals(removed5, null);
		players.addAll(Arrays.asList(removed1, removed2, removed3, removed4));
		Map<Player, Integer> map = new HashMap<>();
		//count how many times each player had a card destroyed
		for (Player p : players) {
			map.putIfAbsent(p, 0);
			map.put(p, map.get(p) + 1);
		}
		assertEquals(removed1.getPlayerHand().getHand().size(), Math.max(0, 2 - map.get(removed1)));
		assertEquals(removed2.getPlayerHand().getHand().size(), Math.max(0, 2 - map.get(removed2)));
		assertEquals(removed3.getPlayerHand().getHand().size(), Math.max(0, 2 - map.get(removed3)));
		assertEquals(removed4.getPlayerHand().getHand().size(), Math.max(0, 2 - map.get(removed4)));
	}
	@Test
	void testSuperpowerShow() {
		Player player1 = new PlayerBotSmarter("Flavio", 400);
		Player player2 = new PlayerBotSmarter("Pablo", 400);
		Player player3 = new PlayerBotSmarter("Mingo", 400);
		Player player4 = new PlayerBotSmarter("Oslo", 400);
		Player player5 = new PlayerBotSmarter("Rodrigo", 10);
		List<Player> playerList = Arrays.asList(player1, player2, player3, player4, player5);
		PokerTableAutomatisee table = new PokerTableAutomatisee(playerList);
		table.initializeSuperpowers();
		assertEquals(table.getSuperpowerUseNumber("show"), 0);
		assertEquals(table.getSuperpowerUseNumber("randomNonsense"), -1);
		table.giveCards();
		Player shown1 = table.useSuperpower(player1, "show");
		Player shown2=table.useSuperpower(player2, "show");
		Player shown3=table.useSuperpower(player3, "show");
		Player shown4=table.useSuperpower(player4, "show");
		table.useSuperpower(player5, "show");
		assertEquals(table.getSuperpowerUseNumber("show"), 4);
		assertEquals(player1.getChipStack(), 400 - SuperpowerShow.getCost());
		assertEquals(player2.getChipStack(), 400 - SuperpowerShow.getCost());
		assertEquals(player3.getChipStack(), 400 - SuperpowerShow.getCost());
		assertEquals(player4.getChipStack(), 400 - SuperpowerShow.getCost());
		assertEquals(player5.getChipStack(), 10);
		table.updateShownCards();
		assertEquals(table.getPlayers().get(0).getCardsKnownFromOtherPlayers().get(shown1).size(), 1);
		assertEquals(table.getPlayers().get(1).getCardsKnownFromOtherPlayers().get(shown2).size(), 1);
		assertEquals(table.getPlayers().get(2).getCardsKnownFromOtherPlayers().get(shown3).size(), 1);
		assertEquals(table.getPlayers().get(3).getCardsKnownFromOtherPlayers().get(shown4).size(), 1);
		Player showAgain = table.useSuperpower(player1, "show");
		//couldn't use the superpower because already used!
		assertEquals(table.getSuperpowerUseNumber("show"),4);
		assertEquals(player1.getChipStack(), 400 - SuperpowerShow.getCost());
		assertEquals(showAgain, null);
		table.resetSuperpowerUsage();
		/*
		table.useSuperpower(player1, "show");
		assertEquals(table.getSuperpowerUseNumber("show"),5);
		assertEquals(player1.getChipStack(), 400 - SuperpowerShow.getCost()*2);*/
	}
	
}
