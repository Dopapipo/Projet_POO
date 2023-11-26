package fr.pantheonsorbonne.miage.game.classes.pokerTableStuff;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.pantheonsorbonne.miage.game.classes.cards.Card;
import fr.pantheonsorbonne.miage.game.classes.cards.CardColor;
import fr.pantheonsorbonne.miage.game.classes.cards.CardValue;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.PlayerBot;
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
		PokerTable table = new PokerTableAutomatisee(Arrays.asList(new Player[] { player1, player2 }));
		List<Card> p1Hand = new ArrayList<>();
		p1Hand.add(new Card(CardValue.TEN, CardColor.CLOVER));
		p1Hand.add(new Card(CardValue.TEN, CardColor.DIAMOND));
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
		player1.bet(40);
		player2.bet(40);
		table.turnPots();

		Assertions.assertEquals(90, player1.getChipStack());
		Assertions.assertEquals(10, player2.getChipStack());
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
		PokerTable table = new PokerTableAutomatisee(Arrays.asList(new Player[] { player1, player2 }));
		List<Card> p1Hand = new ArrayList<>();
		p1Hand.add(new Card(CardValue.TEN, CardColor.CLOVER));
		p1Hand.add(new Card(CardValue.TEN, CardColor.DIAMOND));
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

		// Both players bet their whole chipstack
		player1.bet(50);
		player2.bet(50);
		table.turnPots();

		// Assertions
		Assertions.assertEquals(100, player1.getChipStack());
		Assertions.assertEquals(0, player2.getChipStack());
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
		PokerTable table = new PokerTableAutomatisee(Arrays.asList(new Player[] { player1, player2 }));
		List<Card> p1Hand = new ArrayList<>();
		p1Hand.add(new Card(CardValue.TEN, CardColor.CLOVER));
		p1Hand.add(new Card(CardValue.TEN, CardColor.DIAMOND));
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

		// Both players all-in but p1 has more chips.
		player1.bet(100);
		player2.bet(50);
		table.turnPots();

		// Assertions
		Assertions.assertEquals(150, player1.getChipStack());
		Assertions.assertEquals(0, player2.getChipStack());
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
		PokerTable table = new PokerTableAutomatisee(Arrays.asList(new Player[] { player1, player2 }));
		List<Card> p1Hand = new ArrayList<>();
		p1Hand.add(new Card(CardValue.TEN, CardColor.CLOVER));
		p1Hand.add(new Card(CardValue.TEN, CardColor.DIAMOND));
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

		// Both players all-in but p1 has more chips.
		player1.bet(100);
		player2.bet(50);
		table.turnPots();

		// Assertions
		Assertions.assertEquals(100, player1.getChipStack());
		Assertions.assertEquals(50, player2.getChipStack());
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
		PokerTable table = new PokerTableAutomatisee(Arrays.asList(new Player[] { player1, player2, player3 }));
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
		List<Card> dealerHand = new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE, CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX, CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN, CardColor.SPADE));
		table.getDealer().setHand(dealerHand);

		// Both players all-in but p1 has more chips.
		player1.bet(100);
		player2.bet(100);
		player3.bet(100);
		table.turnPots();

		// Assertions
		Assertions.assertEquals(0, player1.getChipStack());
		Assertions.assertEquals(0, player2.getChipStack());
		Assertions.assertEquals(300, player3.getChipStack());
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
		PokerTable table = new PokerTableAutomatisee(Arrays.asList(new Player[] { player1, player2, player3 }));
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
		List<Card> dealerHand = new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE, CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX, CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN, CardColor.SPADE));
		table.getDealer().setHand(dealerHand);

		// Both players all-in but p1 has more chips.
		player1.bet(100);
		player2.bet(100);
		player3.bet(100);
		table.turnPots();

		// Assertions
		Assertions.assertEquals(0, player1.getChipStack());
		Assertions.assertEquals(0, player2.getChipStack());
		Assertions.assertEquals(170, player3.getChipStack());
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
		PokerTable table = new PokerTableAutomatisee(Arrays.asList(new Player[] { player1, player2, player3 }));
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
		List<Card> dealerHand = new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE, CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX, CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN, CardColor.SPADE));
		table.getDealer().setHand(dealerHand);

		// Both players all-in but p1 has more chips.
		player1.bet(100);
		player2.bet(100);
		player3.bet(100);
		table.turnPots();

		// Assertions
		Assertions.assertEquals(0, player1.getChipStack());
		Assertions.assertEquals(40, player2.getChipStack());
		Assertions.assertEquals(170, player3.getChipStack());
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
		PokerTable table = new PokerTableAutomatisee(
				Arrays.asList(new Player[] { player1, player2, player3, player4 }));
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
		List<Card> dealerHand = new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE, CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX, CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN, CardColor.SPADE));
		table.getDealer().setHand(dealerHand);

		player1.bet(200);
		player2.bet(200);
		player3.bet(200);
		player4.bet(200);
		table.turnPots();

		// assert
		assertEquals(0, player1.getChipStack());
		assertEquals(0, player2.getChipStack());
		assertEquals(0, player3.getChipStack());
		assertEquals(800, player4.getChipStack());
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
		PokerTable table = new PokerTableAutomatisee(
				Arrays.asList(new Player[] { player1, player2, player3, player4 }));
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
		List<Card> dealerHand = new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE, CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX, CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN, CardColor.SPADE));
		table.getDealer().setHand(dealerHand);

		player1.bet(200);
		player2.bet(200);
		player3.bet(200);
		player4.bet(200);
		table.turnPots();

		// assert
		assertEquals(0, player1.getChipStack());
		assertEquals(0, player2.getChipStack());
		assertEquals(800, player3.getChipStack());
		assertEquals(0, player4.getChipStack());
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
		PokerTable table = new PokerTableAutomatisee(
				Arrays.asList(new Player[] { player1, player2, player3, player4 }));
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
		List<Card> dealerHand = new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE, CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX, CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN, CardColor.SPADE));
		table.getDealer().setHand(dealerHand);

		player1.bet(200);
		player2.bet(200);
		player3.bet(200);
		player4.bet(200);
		table.turnPots();

		// assert
		assertEquals(0, player1.getChipStack());
		assertEquals(800, player2.getChipStack());
		assertEquals(0, player3.getChipStack());
		assertEquals(0, player4.getChipStack());
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
		PokerTable table = new PokerTableAutomatisee(
				Arrays.asList(new Player[] { player1, player2, player3, player4 }));
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
		List<Card> dealerHand = new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE, CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX, CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN, CardColor.SPADE));
		table.getDealer().setHand(dealerHand);

		player1.bet(200);
		player2.bet(200);
		player3.bet(200);
		player4.bet(200);
		table.turnPots();

		// assert
		assertEquals(800, player1.getChipStack());
		assertEquals(0, player2.getChipStack());
		assertEquals(0, player3.getChipStack());
		assertEquals(0, player4.getChipStack());
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
		PokerTable table = new PokerTableAutomatisee(
				Arrays.asList(new Player[] { player1, player2, player3, player4 }));
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
		List<Card> dealerHand = new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE, CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE, CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX, CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN, CardColor.SPADE));
		table.getDealer().setHand(dealerHand);

		player1.bet(200);
		player3.bet(200);
		table.turnPots();

		// assert
		assertEquals(600, player1.getChipStack());
		assertEquals(0, player2.getChipStack());
		assertEquals(200, player3.getChipStack());
		assertEquals(0, player4.getChipStack());
	}

	@Test
	void testPlay() {
		Player player1 = new PlayerBot("Flavio", 400);
		Player player2 = new PlayerBot("Pablo", 400);
		Player player3 = new PlayerBot("Mingo", 400);
		Player player4 = new PlayerBot("Oslo", 400);
		List<Player> playerList = Arrays.asList(player1, player2, player3, player4);
		PokerTableAutomatisee table = new PokerTableAutomatisee(playerList);
		table.play();
		assertEquals(false, table.gameContinues());// game ended
		assertEquals(1, table.getPlayers().size());// 1 player won
	}

	@Test
	void testInvertedColors() {
		Player player1 = new PlayerBot("Flavio", 400);
		Player player2 = new PlayerBot("Pablo", 400);
		Player player3 = new PlayerBot("Mingo", 400);
		Player player4 = new PlayerBot("Oslo", 400);
		List<Player> playerList = Arrays.asList(player1, player2, player3, player4);
		PokerTableAutomatisee table = new PokerTableAutomatisee(playerList);
		table.setInvertedColor(0);
		assertEquals(CardColor.SPADE, table.getInvertedColor());
		table.setInvertedColor(1);
		assertEquals(CardColor.HEART, table.getInvertedColor());
		table.setInvertedColor(2);
		assertEquals(CardColor.DIAMOND, table.getInvertedColor());
		table.setInvertedColor(3);
		assertEquals(CardColor.CLOVER, table.getInvertedColor());
	}

	@Test
	void testCallRaiseFold() {
		Player player1 = new PlayerBot("Flavio", 400);
		Player player2 = new PlayerBot("Pablo", 400);
		Player player3 = new PlayerBot("Mingo", 400);
		Player player4 = new PlayerBot("Oslo", 400);
		List<Player> playerList = Arrays.asList(player1, player2, player3, player4);
		PokerTableAutomatisee table = new PokerTableAutomatisee(playerList);
		table.raise(player1, 100);
		assertEquals(100, player1.getBet());
		assertEquals(true, player1.isCurrentlyRaising());
		table.call(player2);
		assertEquals(100, player2.getBet());
		table.fold(player3);
		assertEquals(false, player3.hasNotFolded());
		table.raise(player4, -50);
		assertEquals(100, player4.getBet());

	}

	@Test
	void testUpdateShownCards() {
		Player player1 = new PlayerBot("Flavio", 400);
		Player player2 = new PlayerBot("Pablo", 400);
		Player player3 = new PlayerBot("Mingo", 400);
		Player player4 = new PlayerBot("Oslo", 400);
		List<Player> playerList = Arrays.asList(player1, player2, player3, player4);
		PokerTableAutomatisee table = new PokerTableAutomatisee(playerList);
		table.giveCards();
		// map is initialized in constructor
		assertEquals(3, player1.getCardsKnownFromOtherPlayers().size());
		assertEquals(3, player2.getCardsKnownFromOtherPlayers().size());
		assertEquals(3, player3.getCardsKnownFromOtherPlayers().size());
		assertEquals(3, player4.getCardsKnownFromOtherPlayers().size());
		table.updateShownCards(); // no cards should be shown
		// Each player knows about the existence of 3 other players...
		assertEquals(3, player1.getCardsKnownFromOtherPlayers().size());
		assertEquals(3, player2.getCardsKnownFromOtherPlayers().size());
		assertEquals(3, player3.getCardsKnownFromOtherPlayers().size());
		assertEquals(3, player4.getCardsKnownFromOtherPlayers().size());
		// But no card is known
		assertEquals(0, player1.getCardsKnownFromOtherPlayers().get(player2).size());
		assertEquals(0, player1.getCardsKnownFromOtherPlayers().get(player3).size());
		assertEquals(0, player1.getCardsKnownFromOtherPlayers().get(player4).size());
		assertEquals(0, player2.getCardsKnownFromOtherPlayers().get(player1).size());
		assertEquals(0, player2.getCardsKnownFromOtherPlayers().get(player3).size());
		assertEquals(0, player2.getCardsKnownFromOtherPlayers().get(player4).size());
		assertEquals(0, player3.getCardsKnownFromOtherPlayers().get(player1).size());
		assertEquals(0, player3.getCardsKnownFromOtherPlayers().get(player2).size());
		assertEquals(0, player3.getCardsKnownFromOtherPlayers().get(player4).size());
		assertEquals(0, player4.getCardsKnownFromOtherPlayers().get(player1).size());
		assertEquals(0, player4.getCardsKnownFromOtherPlayers().get(player2).size());
		assertEquals(0, player4.getCardsKnownFromOtherPlayers().get(player3).size());
		player1.showCard(player1.getCardAtIndex(0));
		player2.showCard(player2.getCardAtIndex(0));
		player3.showCard(player3.getCardAtIndex(0));
		player4.showCard(player4.getCardAtIndex(0));
		table.updateShownCards(); // each player should know 1 card from 3 other players
		assertEquals(3, player1.getCardsKnownFromOtherPlayers().size());
		assertEquals(3, player2.getCardsKnownFromOtherPlayers().size());
		assertEquals(3, player3.getCardsKnownFromOtherPlayers().size());
		assertEquals(3, player4.getCardsKnownFromOtherPlayers().size());
		assertEquals(1, player4.getCardsKnownFromOtherPlayers().get(player1).size());
		assertEquals(1, player4.getCardsKnownFromOtherPlayers().get(player2).size());
		assertEquals(1, player4.getCardsKnownFromOtherPlayers().get(player3).size());
		assertEquals(1, player3.getCardsKnownFromOtherPlayers().get(player1).size());
		assertEquals(1, player3.getCardsKnownFromOtherPlayers().get(player2).size());
		assertEquals(1, player3.getCardsKnownFromOtherPlayers().get(player4).size());
		assertEquals(1, player2.getCardsKnownFromOtherPlayers().get(player1).size());
		assertEquals(1, player2.getCardsKnownFromOtherPlayers().get(player3).size());
		assertEquals(1, player2.getCardsKnownFromOtherPlayers().get(player4).size());
		assertEquals(1, player1.getCardsKnownFromOtherPlayers().get(player4).size());
		assertEquals(1, player1.getCardsKnownFromOtherPlayers().get(player3).size());
		assertEquals(1, player1.getCardsKnownFromOtherPlayers().get(player2).size());
		player1.showCard(player1.getCardAtIndex(1));
		player2.showCard(player2.getCardAtIndex(1));
		player3.showCard(player3.getCardAtIndex(1));
		player4.showCard(player4.getCardAtIndex(1));
		table.updateShownCards(); // each player should know 2 cards from 3 other players
		assertEquals(3, player1.getCardsKnownFromOtherPlayers().size());
		assertEquals(3, player2.getCardsKnownFromOtherPlayers().size());
		assertEquals(3, player3.getCardsKnownFromOtherPlayers().size());
		assertEquals(3, player4.getCardsKnownFromOtherPlayers().size());

		assertEquals(2, player4.getCardsKnownFromOtherPlayers().get(player1).size());
		assertEquals(2, player4.getCardsKnownFromOtherPlayers().get(player2).size());
		assertEquals(2, player4.getCardsKnownFromOtherPlayers().get(player3).size());
		assertEquals(2, player3.getCardsKnownFromOtherPlayers().get(player1).size());
		assertEquals(2, player3.getCardsKnownFromOtherPlayers().get(player2).size());
		assertEquals(2, player3.getCardsKnownFromOtherPlayers().get(player4).size());
		assertEquals(2, player2.getCardsKnownFromOtherPlayers().get(player1).size());
		assertEquals(2, player2.getCardsKnownFromOtherPlayers().get(player3).size());
		assertEquals(2, player2.getCardsKnownFromOtherPlayers().get(player4).size());
		assertEquals(2, player1.getCardsKnownFromOtherPlayers().get(player4).size());
		assertEquals(2, player1.getCardsKnownFromOtherPlayers().get(player3).size());
		assertEquals(2, player1.getCardsKnownFromOtherPlayers().get(player2).size());

	}

	@Test
	void testSuperpowerAdd() {
		Player player1 = new PlayerBot("Flavio", 400);
		Player player2 = new PlayerBot("Pablo", 400);
		Player player3 = new PlayerBot("Mingo", 400);
		Player player4 = new PlayerBot("Oslo", 400);
		Player player5 = new PlayerBot("Rodrigo", 10);
		List<Player> playerList = Arrays.asList(player1, player2, player3, player4, player5);
		PokerTableAutomatisee table = new PokerTableAutomatisee(playerList);
		table.initializeSuperpowers();
		// Test that the superpower is initialized and never used
		assertEquals(0, table.getSuperpowerUseNumber("add"));
		assertEquals(-1, table.getSuperpowerUseNumber("randomNonsense"));
		table.giveCards();
		table.useSuperpower(player1, "add");
		table.useSuperpower(player2, "add");
		table.useSuperpower(player3, "add");
		table.useSuperpower(player4, "add");
		table.useSuperpower(player5, "add");
		assertEquals(4, table.getSuperpowerUseNumber("add")); // p5 didn't use it (no chips)
		assertEquals(400 - SuperpowerAdd.getCost(), player1.getChipStack());
		assertEquals(400 - SuperpowerAdd.getCost(), player2.getChipStack());
		assertEquals(400 - SuperpowerAdd.getCost(), player3.getChipStack());
		assertEquals(400 - SuperpowerAdd.getCost(), player4.getChipStack());
		assertEquals(10, player5.getChipStack());
		// check that the cards are known by the bots
		table.updateShownCards();
		assertEquals(1, player1.getCardsKnownFromOtherPlayers().get(player2).size());
		assertEquals(1, player1.getCardsKnownFromOtherPlayers().get(player3).size());
		assertEquals(1, player1.getCardsKnownFromOtherPlayers().get(player4).size());
		assertEquals(1, player2.getCardsKnownFromOtherPlayers().get(player1).size());
		assertEquals(1, player2.getCardsKnownFromOtherPlayers().get(player3).size());
		assertEquals(1, player2.getCardsKnownFromOtherPlayers().get(player4).size());
		assertEquals(1, player3.getCardsKnownFromOtherPlayers().get(player1).size());
		assertEquals(1, player3.getCardsKnownFromOtherPlayers().get(player2).size());
		assertEquals(1, player3.getCardsKnownFromOtherPlayers().get(player4).size());
		assertEquals(1, player4.getCardsKnownFromOtherPlayers().get(player1).size());
		assertEquals(1, player4.getCardsKnownFromOtherPlayers().get(player2).size());
		assertEquals(1, player4.getCardsKnownFromOtherPlayers().get(player3).size());

		// check that the players all have 3 cards
		assertEquals(3, player1.handSize());
		assertEquals(3, player2.handSize());
		assertEquals(3, player3.handSize());
		assertEquals(3, player4.handSize());

	}

	@Test
	void testSuperpowerAddHidden() {
		Player player1 = new PlayerBot("Flavio", 400);
		Player player2 = new PlayerBot("Pablo", 400);
		Player player3 = new PlayerBot("Mingo", 400);
		Player player4 = new PlayerBot("Oslo", 400);
		Player player5 = new PlayerBot("Rodrigo", 10);
		List<Player> playerList = Arrays.asList(player1, player2, player3, player4, player5);
		PokerTableAutomatisee table = new PokerTableAutomatisee(playerList);
		table.initializeSuperpowers();
		assertEquals(0, table.getSuperpowerUseNumber("addHidden"));
		table.giveCards();
		table.useSuperpower(player1, "addHidden");
		table.useSuperpower(player2, "addHidden");
		table.useSuperpower(player3, "addHidden");
		table.useSuperpower(player4, "addHidden");
		table.useSuperpower(player5, "addHidden");
		assertEquals(4, table.getSuperpowerUseNumber("addHidden"));
		assertEquals(400 - SuperpowerAddHidden.getCost(), player1.getChipStack());
		assertEquals(400 - SuperpowerAddHidden.getCost(), player2.getChipStack());
		assertEquals(400 - SuperpowerAddHidden.getCost(), player3.getChipStack());
		assertEquals(400 - SuperpowerAddHidden.getCost(), player4.getChipStack());
		assertEquals(10, player5.getChipStack());
		assertEquals(3, player1.handSize());
		assertEquals(3, player2.handSize());
		assertEquals(3, player3.handSize());
		assertEquals(3, player4.handSize());
		assertEquals(2, table.getPlayers().get(4).handSize());

		table.updateShownCards();
		assertEquals(0, player1.getCardsKnownFromOtherPlayers().get(player2).size());
		assertEquals(0, player1.getCardsKnownFromOtherPlayers().get(player3).size());
		assertEquals(0, player1.getCardsKnownFromOtherPlayers().get(player4).size());
		assertEquals(0, player2.getCardsKnownFromOtherPlayers().get(player1).size());
		assertEquals(0, player2.getCardsKnownFromOtherPlayers().get(player3).size());
		assertEquals(0, player2.getCardsKnownFromOtherPlayers().get(player4).size());
		assertEquals(0, player3.getCardsKnownFromOtherPlayers().get(player1).size());
		assertEquals(0, player3.getCardsKnownFromOtherPlayers().get(player2).size());
		assertEquals(0, player3.getCardsKnownFromOtherPlayers().get(player4).size());
		assertEquals(0, player4.getCardsKnownFromOtherPlayers().get(player1).size());
		assertEquals(0, player4.getCardsKnownFromOtherPlayers().get(player2).size());
		assertEquals(0, player4.getCardsKnownFromOtherPlayers().get(player3).size());
	}

	@Test
	void testSuperpowerDestroy() {
		Player player1 = new PlayerBot("Flavio", 400);
		Player player2 = new PlayerBot("Pablo", 400);
		Player player3 = new PlayerBot("Mingo", 400);
		Player player4 = new PlayerBot("Oslo", 400);
		Player player5 = new PlayerBot("Rodrigo", 10);
		List<Player> playerList = Arrays.asList(player1, player2, player3, player4, player5);
		PokerTableAutomatisee table = new PokerTableAutomatisee(playerList);
		table.initializeSuperpowers();
		assertEquals(0, table.getSuperpowerUseNumber("destroy"));
		table.giveCards();
		// Player that gets destroyed is random so let's handle that!
		Player removed1 = table.useSuperpower(player1, "destroy");
		Player removed2 = table.useSuperpower(player2, "destroy");
		Player removed3 = table.useSuperpower(player3, "destroy");
		Player removed4 = table.useSuperpower(player4, "destroy");
		Player removed5 = table.useSuperpower(player5, "destroy");
		// We know that 4 players used that superpoweer, and the last couldn't use it
		assertEquals(4, table.getSuperpowerUseNumber("destroy"));
		assertEquals(400 - SuperpowerDestroy.getCost(), player1.getChipStack());
		assertEquals(400 - SuperpowerDestroy.getCost(), player2.getChipStack());
		assertEquals(400 - SuperpowerDestroy.getCost(), player3.getChipStack());
		assertEquals(400 - SuperpowerDestroy.getCost(), player4.getChipStack());
		assertEquals(10, player5.getChipStack());
		List<Player> players = new ArrayList<>();
		assertEquals(null, removed5);
		players.addAll(Arrays.asList(removed1, removed2, removed3, removed4));
		Map<Player, Integer> map = new HashMap<>();
		// count how many times each player had a card destroyed
		for (Player p : players) {
			map.putIfAbsent(p, 0);
			map.put(p, map.get(p) + 1);
		}
		assertEquals(Math.max(0, 2 - map.get(removed1)), removed1.handSize());
		assertEquals(Math.max(0, 2 - map.get(removed2)), removed2.handSize());
		assertEquals(Math.max(0, 2 - map.get(removed3)), removed3.handSize());
		assertEquals(Math.max(0, 2 - map.get(removed4)), removed4.handSize());
	}

	@Test
	void testSuperpowerShow() {
		Player player1 = new PlayerBot("Flavio", 400);
		Player player2 = new PlayerBot("Pablo", 400);
		Player player3 = new PlayerBot("Mingo", 400);
		Player player4 = new PlayerBot("Oslo", 400);
		Player player5 = new PlayerBot("Rodrigo", 10);
		List<Player> playerList = Arrays.asList(player1, player2, player3, player4, player5);
		PokerTableAutomatisee table = new PokerTableAutomatisee(playerList);
		table.initializeSuperpowers();
		assertEquals(0, table.getSuperpowerUseNumber("show"));
		table.giveCards();
		Player shown1 = table.useSuperpower(player1, "show");
		Player shown2 = table.useSuperpower(player2, "show");
		Player shown3 = table.useSuperpower(player3, "show");
		Player shown4 = table.useSuperpower(player4, "show");
		table.useSuperpower(player5, "show");
		assertEquals(4, table.getSuperpowerUseNumber("show"));
		assertEquals(400 - SuperpowerShow.getCost(),player1.getChipStack());
		assertEquals(400 - SuperpowerShow.getCost(),player2.getChipStack());
		assertEquals(400 - SuperpowerShow.getCost(),player3.getChipStack());
		assertEquals(400 - SuperpowerShow.getCost(),player4.getChipStack());
		assertEquals(10, player5.getChipStack());
		table.updateShownCards();
		assertEquals(1, player1.getCardsKnownFromOtherPlayers().get(shown1).size());
		assertEquals(1, player2.getCardsKnownFromOtherPlayers().get(shown2).size());
		assertEquals(1, player3.getCardsKnownFromOtherPlayers().get(shown3).size());
		assertEquals(1, player4.getCardsKnownFromOtherPlayers().get(shown4).size());
		Player showAgain = table.useSuperpower(player1, "show");
		// couldn't use the superpower because already used!
		assertEquals(4, table.getSuperpowerUseNumber("show"));
		assertEquals(400 - SuperpowerShow.getCost(),player1.getChipStack());
		assertEquals(null, showAgain);
		 table.resetSuperpowerUsage();
		 table.useSuperpower(player1, "show");
		 assertEquals(5,table.getSuperpowerUseNumber("show"));
		 assertEquals(400 - SuperpowerShow.getCost()*2,player1.getChipStack());
	}

}
