package fr.pantheonsorbonne.miage.game.classes.pokerTableStuff;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.pantheonsorbonne.miage.game.classes.cards.Card;
import fr.pantheonsorbonne.miage.game.classes.cards.CardColor;
import fr.pantheonsorbonne.miage.game.classes.cards.CardValue;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.PlayerBotSmarter;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.PlayerHand;

class PokerTableTest {

	@Test
	/**
	 * Tests that the base pot from <PokerTable> works correctly when no players
	 * are all in, with 2 players.
	 */
	void noAllInTest() {
		
		//Arrange
		
		Player player1 = new Player("Flavio",50);
		Player player2 = new Player ("Pablo", 50);
		PokerTable table = new PokerTable();
		ArrayList<Card> p1Hand = new ArrayList<>();
		p1Hand.add(new Card(CardValue.TEN,CardColor.CLOVER));
		p1Hand.add(new Card(CardValue.TEN,CardColor.DIAMOND));
		table.addPlayer(player1);
		table.addPlayer(player2);
		player1.setHand(new PlayerHand(p1Hand));
		ArrayList<Card> p2Hand = new ArrayList<>();
		p2Hand.add(new Card(CardValue.NINE,CardColor.CLOVER));
		p2Hand.add(new Card(CardValue.NINE,CardColor.HEART));
		player2.setHand(new PlayerHand(p2Hand));
		ArrayList <Card> dealerHand= new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO,CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE,CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE,CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX,CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN,CardColor.SPADE));
		table.getDealer().setHand(dealerHand);
		table.getPlayers().get(0).bet(40);
		table.getPlayers().get(1).bet(40);
		table.turnPots();
		
		Assertions.assertEquals(player1.getChipStack(),90);
		Assertions.assertEquals(player2.getChipStack(), 10);
	}
	
	@Test
	/**
	 * Tests that <PokerTable> works correctly when both players are all-in with
	 * the same bet
	 */
	void bothAllInTest() {
		
		//arrange
		Player player1 = new Player("Flavio",50);
		Player player2 = new Player ("Pablo", 50);
		PokerTable table = new PokerTable();
		ArrayList<Card> p1Hand = new ArrayList<>();
		p1Hand.add(new Card(CardValue.TEN,CardColor.CLOVER));
		p1Hand.add(new Card(CardValue.TEN,CardColor.DIAMOND));
		table.addPlayer(player1);
		table.addPlayer(player2);
		player1.setHand(new PlayerHand(p1Hand));
		ArrayList<Card> p2Hand = new ArrayList<>();
		p2Hand.add(new Card(CardValue.NINE,CardColor.CLOVER));
		p2Hand.add(new Card(CardValue.NINE,CardColor.HEART));
		player2.setHand(new PlayerHand(p2Hand));
		ArrayList <Card> dealerHand= new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO,CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE,CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE,CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX,CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN,CardColor.SPADE));
		table.getDealer().setHand(dealerHand);
		
		//Both players bet their whole chipstack
		table.getPlayers().get(0).bet(50);
		table.getPlayers().get(1).bet(50);
		table.turnPots();
		
		//Assertions
		Assertions.assertEquals(player1.getChipStack(),100);
		Assertions.assertEquals(player2.getChipStack(), 0);
	}
	@Test
	/**
	 * Tests if <PokerTable> works correctly when only one player is all-in,
	 * while the other has more chips. In that case, the winning player
	 * has more chips.
	 */
	void oneAllInTest() {
		//arrange
				Player player1 = new Player("Flavio",100);
				Player player2 = new Player ("Pablo", 50);
				PokerTable table = new PokerTable();
				ArrayList<Card> p1Hand = new ArrayList<>();
				p1Hand.add(new Card(CardValue.TEN,CardColor.CLOVER));
				p1Hand.add(new Card(CardValue.TEN,CardColor.DIAMOND));
				table.addPlayer(player1);
				table.addPlayer(player2);
				player1.setHand(new PlayerHand(p1Hand));
				ArrayList<Card> p2Hand = new ArrayList<>();
				p2Hand.add(new Card(CardValue.NINE,CardColor.CLOVER));
				p2Hand.add(new Card(CardValue.NINE,CardColor.HEART));
				player2.setHand(new PlayerHand(p2Hand));
				ArrayList <Card> dealerHand= new ArrayList<>();
				dealerHand.add(new Card(CardValue.TWO,CardColor.CLOVER));
				dealerHand.add(new Card(CardValue.THREE,CardColor.CLOVER));
				dealerHand.add(new Card(CardValue.FIVE,CardColor.DIAMOND));
				dealerHand.add(new Card(CardValue.SIX,CardColor.HEART));
				dealerHand.add(new Card(CardValue.SEVEN,CardColor.SPADE));
				table.getDealer().setHand(dealerHand);
				
				//Both players all-in but p1 has more chips.
				table.getPlayers().get(0).bet(100);
				table.getPlayers().get(1).bet(50);
				table.turnPots();
				
				//Assertions
				Assertions.assertEquals(player1.getChipStack(),150);
				Assertions.assertEquals(player2.getChipStack(), 0);
	}
	@Test
	/**
	 * Tests if the <AllInPot> implementation of <PokerTable> is working fine.
	 * p1 all-ins with less chips than p2 and wins.
	 */
	void oneAllInSidePotTest() {
		//arrange
				Player player1 = new Player("Flavio",50);
				Player player2 = new Player ("Pablo", 100);
				PokerTable table = new PokerTable();
				ArrayList<Card> p1Hand = new ArrayList<>();
				p1Hand.add(new Card(CardValue.TEN,CardColor.CLOVER));
				p1Hand.add(new Card(CardValue.TEN,CardColor.DIAMOND));
				table.addPlayer(player1);
				table.addPlayer(player2);
				player1.setHand(new PlayerHand(p1Hand));
				ArrayList<Card> p2Hand = new ArrayList<>();
				p2Hand.add(new Card(CardValue.NINE,CardColor.CLOVER));
				p2Hand.add(new Card(CardValue.NINE,CardColor.HEART));
				player2.setHand(new PlayerHand(p2Hand));
				ArrayList <Card> dealerHand= new ArrayList<>();
				dealerHand.add(new Card(CardValue.TWO,CardColor.CLOVER));
				dealerHand.add(new Card(CardValue.THREE,CardColor.CLOVER));
				dealerHand.add(new Card(CardValue.FIVE,CardColor.DIAMOND));
				dealerHand.add(new Card(CardValue.SIX,CardColor.HEART));
				dealerHand.add(new Card(CardValue.SEVEN,CardColor.SPADE));
				table.getDealer().setHand(dealerHand);
				
				//Both players all-in but p1 has more chips.
				table.getPlayers().get(0).bet(100);
				table.getPlayers().get(1).bet(50);
				table.turnPots();
				
				//Assertions
				Assertions.assertEquals(player1.getChipStack(),100);
				Assertions.assertEquals(player2.getChipStack(), 50);
	}
	
	//have different chipstacks.
	@Test
	/**
	 * Tests <PokerTable> pot system when 3 players are all-in with the same chips
	 */
	void threePlayersAllInSameBet() {
		Player player1 = new Player("Flavio",100);
		Player player2 = new Player ("Pablo", 100);
		Player player3 = new Player("Mingo", 100);
		PokerTable table = new PokerTable();
		ArrayList<Card> p1Hand = new ArrayList<>();
		p1Hand.add(new Card(CardValue.TEN,CardColor.CLOVER));
		p1Hand.add(new Card(CardValue.TEN,CardColor.DIAMOND));
		player1.setHand(new PlayerHand(p1Hand));
		ArrayList<Card> p2Hand = new ArrayList<>();
		p2Hand.add(new Card(CardValue.NINE,CardColor.CLOVER));
		p2Hand.add(new Card(CardValue.NINE,CardColor.HEART));
		player2.setHand(new PlayerHand(p2Hand));
		ArrayList<Card> p3Hand = new ArrayList<>();
		p3Hand.add(new Card (CardValue.JACK,CardColor.CLOVER));
		p3Hand.add(new Card (CardValue.JACK,CardColor.SPADE));
		player3.setHand(new PlayerHand(p3Hand));
		table.addPlayer(player1);
		table.addPlayer(player2);
		table.addPlayer(player3);
		ArrayList <Card> dealerHand= new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO,CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE,CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE,CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX,CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN,CardColor.SPADE));
		table.getDealer().setHand(dealerHand);
		
		//Both players all-in but p1 has more chips.
		table.getPlayers().get(0).bet(100);
		table.getPlayers().get(1).bet(100);
		table.getPlayers().get(2).bet(100);
		table.turnPots();
		
		//Assertions
		Assertions.assertEquals(player1.getChipStack(),0);
		Assertions.assertEquals(player2.getChipStack(),0);
		Assertions.assertEquals(player3.getChipStack(), 300);
	}
	@Test
	/**
	 * Flavio has less chips than Pablo and Mingo. Mingo wins, he should get 50+60+60
	 * Pablo and Flavio should be broke.
	 */
	void ThreePlayersDifferentChipsAllIn() {
		Player player1 = new Player("Flavio",50);
		Player player2 = new Player ("Pablo", 60);
		Player player3 = new Player("Mingo", 60);
		PokerTable table = new PokerTable();
		ArrayList<Card> p1Hand = new ArrayList<>();
		p1Hand.add(new Card(CardValue.TEN,CardColor.CLOVER));
		p1Hand.add(new Card(CardValue.TEN,CardColor.DIAMOND));
		player1.setHand(new PlayerHand(p1Hand));
		ArrayList<Card> p2Hand = new ArrayList<>();
		p2Hand.add(new Card(CardValue.NINE,CardColor.CLOVER));
		p2Hand.add(new Card(CardValue.NINE,CardColor.HEART));
		player2.setHand(new PlayerHand(p2Hand));
		ArrayList<Card> p3Hand = new ArrayList<>();
		p3Hand.add(new Card (CardValue.JACK,CardColor.CLOVER));
		p3Hand.add(new Card (CardValue.JACK,CardColor.SPADE));
		player3.setHand(new PlayerHand(p3Hand));
		table.addPlayer(player1);
		table.addPlayer(player2);
		table.addPlayer(player3);
		ArrayList <Card> dealerHand= new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO,CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE,CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE,CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX,CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN,CardColor.SPADE));
		table.getDealer().setHand(dealerHand);
		
		//Both players all-in but p1 has more chips.
		table.getPlayers().get(0).bet(100);
		table.getPlayers().get(1).bet(100);
		table.getPlayers().get(2).bet(100);
		table.turnPots();
		
		//Assertions
		Assertions.assertEquals(player1.getChipStack(),0);
		Assertions.assertEquals(player2.getChipStack(),0);
		Assertions.assertEquals(player3.getChipStack(), 170);
	}
	@Test
	/**
	 * Flavio and Pablo lose, so Mingo should get 50+60+60 and Pablo should get 40 back.
	 */
	void ThreePlayersDifferentChipsTwoAreAllIn() {
		Player player1 = new Player("Flavio",50);
		Player player2 = new Player ("Pablo", 100);
		Player player3 = new Player("Mingo", 60);
		PokerTable table = new PokerTable();
		ArrayList<Card> p1Hand = new ArrayList<>();
		p1Hand.add(new Card(CardValue.TEN,CardColor.CLOVER));
		p1Hand.add(new Card(CardValue.TEN,CardColor.DIAMOND));
		player1.setHand(new PlayerHand(p1Hand));
		ArrayList<Card> p2Hand = new ArrayList<>();
		p2Hand.add(new Card(CardValue.NINE,CardColor.CLOVER));
		p2Hand.add(new Card(CardValue.NINE,CardColor.HEART));
		player2.setHand(new PlayerHand(p2Hand));
		ArrayList<Card> p3Hand = new ArrayList<>();
		p3Hand.add(new Card (CardValue.JACK,CardColor.CLOVER));
		p3Hand.add(new Card (CardValue.JACK,CardColor.SPADE));
		player3.setHand(new PlayerHand(p3Hand));
		table.addPlayer(player1);
		table.addPlayer(player2);
		table.addPlayer(player3);
		ArrayList <Card> dealerHand= new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO,CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE,CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE,CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX,CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN,CardColor.SPADE));
		table.getDealer().setHand(dealerHand);
		
		//Both players all-in but p1 has more chips.
		table.getPlayers().get(0).bet(100);
		table.getPlayers().get(1).bet(100);
		table.getPlayers().get(2).bet(100);
		table.turnPots();
		
		//Assertions
		Assertions.assertEquals(player1.getChipStack(),0);
		Assertions.assertEquals(player2.getChipStack(),40);
		Assertions.assertEquals(player3.getChipStack(), 170);
	}
	@Test
	/**
	 * Fourth player wins
	 */
	void FourPlayersLastWins() {
		Player player1 = new Player("Flavio",200);
		Player player2 = new Player ("Pablo", 200);
		Player player3 = new Player("Mingo", 200);
		Player player4 = new Player("Oslo",200);
		PokerTableAutomatisee table = new PokerTableAutomatisee();
		List<Card> p1Hand = new ArrayList<>();
		p1Hand.add(new Card(CardValue.TEN,CardColor.CLOVER));
		p1Hand.add(new Card(CardValue.TEN,CardColor.DIAMOND));
		player1.setHand(new PlayerHand(p1Hand));
		List<Card> p2Hand = new ArrayList<>();
		p2Hand.add(new Card(CardValue.NINE,CardColor.CLOVER));
		p2Hand.add(new Card(CardValue.NINE,CardColor.HEART));
		player2.setHand(new PlayerHand(p2Hand));
		List<Card> p3Hand = new ArrayList<>();
		p3Hand.add(new Card (CardValue.JACK,CardColor.CLOVER));
		p3Hand.add(new Card (CardValue.JACK,CardColor.SPADE));
		player3.setHand(new PlayerHand(p3Hand));
		List <Card> p4Hand = new ArrayList<>();
		p4Hand.add(new Card (CardValue.QUEEN,CardColor.CLOVER));
		p4Hand.add(new Card (CardValue.QUEEN,CardColor.SPADE));
		player4.setHand(new PlayerHand(p4Hand));
		table.addPlayer(player1);
		table.addPlayer(player2);
		table.addPlayer(player3);
		table.addPlayer(player4);
		List<Card> dealerHand= new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO,CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE,CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE,CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX,CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN,CardColor.SPADE));
		table.getDealer().setHand(dealerHand);
		
		table.getPlayers().get(0).bet(200);
		table.getPlayers().get(1).bet(200);
		table.getPlayers().get(2).bet(200);
		table.getPlayers().get(3).bet(200);
		table.turnPots();
		
		//assert
		assertEquals(player1.getChipStack(),0);
		assertEquals(player2.getChipStack(),0);
		assertEquals(player3.getChipStack(), 0);
		assertEquals(player4.getChipStack(),800);
	}
	
	
	@Test
	/**
	 * Third player wins
	 */
	void FourPlayersThirdWins() {
		Player player1 = new Player("Flavio",200);
		Player player2 = new Player ("Pablo", 200);
		Player player3 = new Player("Mingo", 200);
		Player player4 = new Player("Oslo",200);
		PokerTableAutomatisee table = new PokerTableAutomatisee();
		List<Card> p1Hand = new ArrayList<>();
		p1Hand.add(new Card(CardValue.TEN,CardColor.CLOVER));
		p1Hand.add(new Card(CardValue.TEN,CardColor.DIAMOND));
		player1.setHand(new PlayerHand(p1Hand));
		List<Card> p2Hand = new ArrayList<>();
		p2Hand.add(new Card(CardValue.NINE,CardColor.CLOVER));
		p2Hand.add(new Card(CardValue.NINE,CardColor.HEART));
		player2.setHand(new PlayerHand(p2Hand));
		List<Card> p3Hand = new ArrayList<>();
		p3Hand.add(new Card (CardValue.QUEEN,CardColor.CLOVER));
		p3Hand.add(new Card (CardValue.QUEEN,CardColor.SPADE));
		player3.setHand(new PlayerHand(p3Hand));
		List <Card> p4Hand = new ArrayList<>();
		p4Hand.add(new Card (CardValue.JACK,CardColor.CLOVER));
		p4Hand.add(new Card (CardValue.JACK,CardColor.SPADE));
		player4.setHand(new PlayerHand(p4Hand));
		table.addPlayer(player1);
		table.addPlayer(player2);
		table.addPlayer(player3);
		table.addPlayer(player4);
		List<Card> dealerHand= new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO,CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE,CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE,CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX,CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN,CardColor.SPADE));
		table.getDealer().setHand(dealerHand);
		
		table.getPlayers().get(0).bet(200);
		table.getPlayers().get(1).bet(200);
		table.getPlayers().get(2).bet(200);
		table.getPlayers().get(3).bet(200);
		table.turnPots();
		
		//assert
		assertEquals(player1.getChipStack(),0);
		assertEquals(player2.getChipStack(),0);
		assertEquals(player3.getChipStack(), 800);
		assertEquals(player4.getChipStack(),0);
	}
	@Test
	/**
	 * Second player wins
	 */
	void FourPlayersSecondWins() {
		Player player1 = new Player("Flavio",200);
		Player player2 = new Player ("Pablo", 200);
		Player player3 = new Player("Mingo", 200);
		Player player4 = new Player("Oslo",200);
		PokerTableAutomatisee table = new PokerTableAutomatisee();
		List<Card> p1Hand = new ArrayList<>();
		p1Hand.add(new Card(CardValue.TEN,CardColor.CLOVER));
		p1Hand.add(new Card(CardValue.TEN,CardColor.DIAMOND));
		player1.setHand(new PlayerHand(p1Hand));
		List<Card> p2Hand = new ArrayList<>();
		p2Hand.add(new Card(CardValue.QUEEN,CardColor.CLOVER));
		p2Hand.add(new Card(CardValue.QUEEN,CardColor.HEART));
		player2.setHand(new PlayerHand(p2Hand));
		List<Card> p3Hand = new ArrayList<>();
		p3Hand.add(new Card (CardValue.NINE,CardColor.CLOVER));
		p3Hand.add(new Card (CardValue.NINE,CardColor.SPADE));
		player3.setHand(new PlayerHand(p3Hand));
		List <Card> p4Hand = new ArrayList<>();
		p4Hand.add(new Card (CardValue.JACK,CardColor.CLOVER));
		p4Hand.add(new Card (CardValue.JACK,CardColor.SPADE));
		player4.setHand(new PlayerHand(p4Hand));
		table.addPlayer(player1);
		table.addPlayer(player2);
		table.addPlayer(player3);
		table.addPlayer(player4);
		List<Card> dealerHand= new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO,CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE,CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE,CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX,CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN,CardColor.SPADE));
		table.getDealer().setHand(dealerHand);
		
		table.getPlayers().get(0).bet(200);
		table.getPlayers().get(1).bet(200);
		table.getPlayers().get(2).bet(200);
		table.getPlayers().get(3).bet(200);
		table.turnPots();
		
		//assert
		assertEquals(player1.getChipStack(),0);
		assertEquals(player2.getChipStack(),800);
		assertEquals(player3.getChipStack(), 0);
		assertEquals(player4.getChipStack(),0);
	}
	
	@Test
	/**
	 * First player wins
	 */
	void FourPlayersFirstWins() {
		Player player1 = new Player("Flavio",200);
		Player player2 = new Player ("Pablo", 200);
		Player player3 = new Player("Mingo", 200);
		Player player4 = new Player("Oslo",200);
		PokerTableAutomatisee table = new PokerTableAutomatisee();
		List<Card> p1Hand = new ArrayList<>();
		p1Hand.add(new Card(CardValue.QUEEN,CardColor.CLOVER));
		p1Hand.add(new Card(CardValue.QUEEN,CardColor.DIAMOND));
		player1.setHand(new PlayerHand(p1Hand));
		List<Card> p2Hand = new ArrayList<>();
		p2Hand.add(new Card(CardValue.TEN,CardColor.CLOVER));
		p2Hand.add(new Card(CardValue.TEN,CardColor.HEART));
		player2.setHand(new PlayerHand(p2Hand));
		List<Card> p3Hand = new ArrayList<>();
		p3Hand.add(new Card (CardValue.NINE,CardColor.CLOVER));
		p3Hand.add(new Card (CardValue.NINE,CardColor.SPADE));
		player3.setHand(new PlayerHand(p3Hand));
		List <Card> p4Hand = new ArrayList<>();
		p4Hand.add(new Card (CardValue.JACK,CardColor.CLOVER));
		p4Hand.add(new Card (CardValue.JACK,CardColor.SPADE));
		player4.setHand(new PlayerHand(p4Hand));
		table.addPlayer(player1);
		table.addPlayer(player2);
		table.addPlayer(player3);
		table.addPlayer(player4);
		List<Card> dealerHand= new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO,CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE,CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE,CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX,CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN,CardColor.SPADE));
		table.getDealer().setHand(dealerHand);
		
		table.getPlayers().get(0).bet(200);
		table.getPlayers().get(1).bet(200);
		table.getPlayers().get(2).bet(200);
		table.getPlayers().get(3).bet(200);
		table.turnPots();
		
		//assert
		assertEquals(player1.getChipStack(),800);
		assertEquals(player2.getChipStack(),0);
		assertEquals(player3.getChipStack(), 0);
		assertEquals(player4.getChipStack(),0);
	}
	@Test
	/**
	 * Only 2 players left when started with 4
	 */
	void twoLeftOutOfFour() {
		Player player1 = new Player("Flavio",400);
		Player player2 = new Player ("Pablo", 0);
		Player player3 = new Player("Mingo", 400);
		Player player4 = new Player("Oslo",0);
		PokerTableAutomatisee table = new PokerTableAutomatisee();
		List<Card> p1Hand = new ArrayList<>();
		p1Hand.add(new Card(CardValue.QUEEN,CardColor.CLOVER));
		p1Hand.add(new Card(CardValue.QUEEN,CardColor.DIAMOND));
		player1.setHand(new PlayerHand(p1Hand));
		List<Card> p2Hand = new ArrayList<>();
		p2Hand.add(new Card(CardValue.TEN,CardColor.CLOVER));
		p2Hand.add(new Card(CardValue.TEN,CardColor.HEART));
		player2.setHand(new PlayerHand(p2Hand));
		List<Card> p3Hand = new ArrayList<>();
		p3Hand.add(new Card (CardValue.NINE,CardColor.CLOVER));
		p3Hand.add(new Card (CardValue.NINE,CardColor.SPADE));
		player3.setHand(new PlayerHand(p3Hand));
		List <Card> p4Hand = new ArrayList<>();
		p4Hand.add(new Card (CardValue.JACK,CardColor.CLOVER));
		p4Hand.add(new Card (CardValue.JACK,CardColor.SPADE));
		player4.setHand(new PlayerHand(p4Hand));
		table.addPlayer(player1);
		table.addPlayer(player2);
		table.addPlayer(player3);
		table.addPlayer(player4);
		List<Card> dealerHand= new ArrayList<>();
		dealerHand.add(new Card(CardValue.TWO,CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.THREE,CardColor.CLOVER));
		dealerHand.add(new Card(CardValue.FIVE,CardColor.DIAMOND));
		dealerHand.add(new Card(CardValue.SIX,CardColor.HEART));
		dealerHand.add(new Card(CardValue.SEVEN,CardColor.SPADE));
		table.getDealer().setHand(dealerHand);
		
		table.getPlayers().get(0).bet(200);
		table.getPlayers().get(1).bet(200);
		table.turnPots();
		
		//assert
		assertEquals(player1.getChipStack(),600);
		assertEquals(player2.getChipStack(),0);
		assertEquals(player3.getChipStack(), 200);
		assertEquals(player4.getChipStack(),0);
	}

	@Test
	void superpowerTestAdd() {
		//PlayerBotSmarter always uses the add superpower
		Player player1 = new PlayerBotSmarter("Flavio",400);
		Player player2 = new PlayerBotSmarter ("Pablo", 400);
		Player player3 = new PlayerBotSmarter("Mingo", 400);
		Player player4 = new PlayerBotSmarter("Oslo",400);
		List<Player> playerList = Arrays.asList(player1,player2,player3,player4);
		PokerTableAutomatisee table = new PokerTableAutomatisee(playerList);
		table.startTurnWithPots();
		//testing that every player was able to use the superpower, and that the card was added
		assertEquals(table.getSuperpowerUseNumber("add"),4);
		assertEquals(player1.getPlayerHand().getHand().size(),3);
	}

	@Test
	void superpowerTest() {
		//PlayerBotSmarter always uses the add superpower
		Player player1 = new PlayerBotSmarter("Flavio",400);
		Player player2 = new PlayerBotSmarter ("Pablo", 400);
		Player player3 = new PlayerBotSmarter("Mingo", 400);
		Player player4 = new PlayerBotSmarter("Oslo",400);
		List<Player> playerList = Arrays.asList(player1,player2,player3,player4);
		PokerTableAutomatisee table = new PokerTableAutomatisee(playerList);
		table.startTurnWithPots();
		assertEquals(table.getSuperpowerUseNumber("add"),4);
		assertEquals(player1.getPlayerHand().getHand().size(),3);
		assertEquals(player2.getPlayerHand().getHand().size(),3);
		assertEquals(player3.getPlayerHand().getHand().size(),3);
		assertEquals(player4.getPlayerHand().getHand().size(),3);
		//Test that table is reset
		table.resetTable();
		assertEquals(table.getNumberOfPots(),0);
		//play till the end
		table.play();
		//test that theres no weird bug and only one player is left after playing
		assertEquals(table.gameContinues(),false);
	}

	// @Test
	// void superpowerTestShow() {
	// 	//PlayerBot 
	// 	Player player1 = new PlayerBot("Flavio",400);
	// 	Player player2 = new PlayerBot ("Pablo", 400);
	// 	Player player3 = new PlayerBot("Mingo", 400);
	// 	Player player4 = new PlayerBot("Oslo",10);
	// 	List<Player> playerList = Arrays.asList(player1,player2,player3,player4);
	// 	PokerTableAutomatisee table = new PokerTableAutomatisee(playerList);
	// 	table.giveCards();
	// 	table.askAndUseSuperpower(player1, 1);
	// 	table.askAndUseSuperpower(player2, 1);
	// 	table.askAndUseSuperpower(player3, 1);
	// 	table.askAndUseSuperpower(player4, 1);
	// 	assertEquals(player1.getCardsKnownFromOtherPlayers().values().iterator().next().size(),1);
	// 	assertEquals(player2.getCardsKnownFromOtherPlayers().values().iterator().next().size(),1);
	// 	assertEquals(player3.getCardsKnownFromOtherPlayers().values().iterator().next().size(),1);
	// 	assertEquals(player4.getCardsKnownFromOtherPlayers().values().iterator().next().size(),0);
	// 	assertEquals(player1.getChipStack(), 400-SuperpowerShow.getCost());
	// 	assertEquals(player2.getChipStack(), 400-SuperpowerShow.getCost());
	// 	assertEquals(player3.getChipStack(), 400-SuperpowerShow.getCost());
	// 	assertEquals(player4.getChipStack(), 10);
		
	// }

	
}
