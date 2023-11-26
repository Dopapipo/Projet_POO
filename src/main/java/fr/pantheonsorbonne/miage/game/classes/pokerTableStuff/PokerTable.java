package fr.pantheonsorbonne.miage.game.classes.pokerTableStuff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import fr.pantheonsorbonne.miage.game.classes.cards.Card;
import fr.pantheonsorbonne.miage.game.classes.cards.CardColor;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.PlayerHand;
import fr.pantheonsorbonne.miage.game.classes.superpowers.SuperpowerAdd;
import fr.pantheonsorbonne.miage.game.classes.superpowers.SuperpowerAddHidden;
import fr.pantheonsorbonne.miage.game.classes.superpowers.SuperpowerDestroy;
import fr.pantheonsorbonne.miage.game.classes.superpowers.SuperpowerOther;
import fr.pantheonsorbonne.miage.game.classes.superpowers.SuperpowerSelf;
import fr.pantheonsorbonne.miage.game.classes.superpowers.SuperpowerShow;
import fr.pantheonsorbonne.miage.game.logic.WinConditionLogic;

/*
 * This abstract class represents a poker table. It holds all the functionnalities of a poker table.
 * The bots play using a class that inherits from this one.
 */
public abstract class PokerTable {
	protected CardColor invertedColor;
	protected List<Player> playerList;
	protected List<Player> currentlyPlaying;
	protected Queue<Player> playerQueue; // used for turn order
	// Code refactoring would be too big if I had to change currentlyPlaying to a
	// queue
	// So i'll just use a queue for turn order and keep currentlyPlaying as a list
	protected Dealer dealer;
	protected Deck deck;
	protected int totalBets;
	protected int highestBet;
	protected int numberOfTurns;
	protected Blind bigBlind;
	protected Blind smallBlind;
	protected Blind donor;
	protected static final int DEFAULT_BLIND = 10;
	protected static final int TURNS_FOR_BLIND_INCREASE = 5;
	protected SuperpowerSelf superpowerAdd;
	protected SuperpowerOther superpowerShow;
	protected SuperpowerOther superpowerDestroy;
	protected SuperpowerSelf superpowerAddHidden;
	protected Scanner scanner = new Scanner(System.in);

	protected List<Pot> pots = new ArrayList<>();

	public PokerTable() {
		this.playerList = new ArrayList<>();
		this.currentlyPlaying = new ArrayList<>();
		this.deck = new Deck();
		this.dealer = new Dealer(deck);
		totalBets = 0;
		this.highestBet = 0;
		this.initializeSuperpowers();
		this.playerQueue = new LinkedList<Player>();
	}

	public PokerTable(List<Player> players) {
		this();
		this.playerList = players;
		for (Player player : this.playerList) {
			if (player.isPlaying()) {
				this.currentlyPlaying.add(player);
			}
		}
		this.initializePlayerHands();
		this.updateShownCards(); // will initialize player's maps

	}

	private void buildQueue() {
		Player first = this.bigBlind.getPlayer();
		int firstIndex = this.currentlyPlaying.indexOf(first);
		while (this.playerQueue.size() < this.currentlyPlaying.size()) {
			this.playerQueue.add(this.currentlyPlaying.get(firstIndex));
			firstIndex = (firstIndex + 1) % this.currentlyPlaying.size();
		}
	}

	private void initializePlayerHands() {
		for (Player player : this.currentlyPlaying) {
			player.setHand(new PlayerHand(new ArrayList<Card>()));
		}
	}

	protected void initializeSuperpowers() {
		this.superpowerShow = new SuperpowerShow();
		this.superpowerDestroy = new SuperpowerDestroy();
		this.superpowerAdd = new SuperpowerAdd();
		this.superpowerAddHidden = new SuperpowerAddHidden();
	}

	protected int howManyAreStillPlaying() {
		return this.currentlyPlaying.size();
	}

	public int getDefaultBlind() {
		return DEFAULT_BLIND;
	}

	protected void initializeBlinds() {
		int n = this.howManyAreStillPlaying();
		if (n <= 1) {
			return;
		}
		// last player at the table will be first big blind, the one before him
		// will be small blind, and the one before that will be donor.
		// if there's only two players, one player will always be donor and small blind.
		if (this.bigBlind == null) {
			this.bigBlind = new Blind(this.DEFAULT_BLIND, this.currentlyPlaying.get(n - 1));
			this.smallBlind = new Blind(this.DEFAULT_BLIND / 2, this.currentlyPlaying.get(n - 2));
			this.donor = new Blind(0, this.currentlyPlaying.get(Math.max(0, n - 3)));
		}

	}

	/**
	 * Switches each blind to the next player in the table
	 */
	protected void switchBlinds() {
		int n = this.currentlyPlaying.size();
		int bigBlindIndex = 0, smallBlindIndex = 0, donorIndex = 0;
		// find the index of players that hold the blinds
		for (int i = 0; i < n; i++) {
			if (this.currentlyPlaying.get(i) == this.bigBlind.getPlayer()) {
				bigBlindIndex = i;
			} else if (this.currentlyPlaying.get(i) == this.smallBlind.getPlayer()) {
				smallBlindIndex = i;
			} else if (this.currentlyPlaying.get(i) == this.donor.getPlayer()) {
				donorIndex = i;
			}
		}
		// increment the blinds mod n (if last player has blind, it goes to the first)
		bigBlindIndex = (bigBlindIndex + 1) % n;
		smallBlindIndex = (smallBlindIndex + 1) % n;
		donorIndex = (donorIndex + 1) % n;
		// set players to the blinds
		this.bigBlind.setPlayer(this.currentlyPlaying.get(bigBlindIndex));
		this.smallBlind.setPlayer(this.currentlyPlaying.get(smallBlindIndex));
		this.donor.setPlayer(this.currentlyPlaying.get(donorIndex));
	}

	/**
	 * Kicks every <Player> with no money left
	 */
	protected List<String> kickBrokePlayers() {
		List<String> removedNames = new ArrayList<>();
		for (Player player : this.playerList) {
			if (player.isPlaying() && player.getChipStack() == 0) {
				player.setPlaying(false);
				removedNames.add(player.getName());
				this.currentlyPlaying.remove(player);
			}
		}
		return removedNames;
	}

	/**
	 * Adds a player to the current <PokerTable>
	 * This method is here for convenience, we could always initialize a pokerTable
	 * from a list if we
	 * wanted to. I left it public because it's used in pokerTableTest.
	 * 
	 * @param player
	 */
	public void addPlayer(Player player) {
		this.playerList.add(player);
		if (player.isPlaying()) {
			this.currentlyPlaying.add(player);
		}
	}

	/**
	 * Deals 2 new cards to every player from the current deck
	 */
	protected void giveCards() {
		for (Player player : this.currentlyPlaying) {
			player.setHand(new PlayerHand(this.deck.getRandomCards(2)));
		}
	}

	public Deck getDeck() {
		return this.deck;
	}

	/**
	 * Checks what <Player> won the current round. Also supports draws
	 * 
	 * @param players
	 * @return
	 */
	protected List<Player> checkWhoWins(List<Player> players) {
		List<Player> playersThatWon = new ArrayList<>();
		if (players.isEmpty()) {
			return playersThatWon;
		}
		for (Player player : players) {
			player.setWinCombination(
					WinConditionLogic.findWinningCombination(this.invertedColor, dealer, player.getPlayerHand()));
		}
		Collections.sort(players);
		int index = players.size() - 1;
		Player wonForSure = players.get(index);
		// Look for the strongest player that hasn't folded
		while (index > 0 && !wonForSure.hasNotFolded()) {
			index--;
			wonForSure = players.get(index);
		}
		playersThatWon.add(wonForSure);
		// check for other players that haven't folded with similar strength hands
		for (Player player : players) {
			if (player.hasNotFolded() && !playersThatWon.contains(player) && wonForSure.compareTo(player) == 0) {
				playersThatWon.add(player);
			}
		}
		return playersThatWon;
	}

	/**
	 * Gives their money to the players that won. If there's a draw between n
	 * players, splits the pot n ways.
	 * 
	 * @param playersThatWon
	 */

	protected void resetSuperpowerUsage() {
		this.superpowerAdd.resetUsage();
		this.superpowerAddHidden.resetUsage();
		this.superpowerDestroy.resetUsage();
		this.superpowerShow.resetUsage();
	}

	/**
	 * Resets all player's raise state
	 */
	protected void resetPlayersRaise() {
		for (Player player : this.currentlyPlaying) {
			player.setCurrentlyRaising(false);
		}
	}

	protected void flop() {
		this.dealer.flop();
		this.updateDealerHandForPlayers();
	}

	protected void updateDealerHandForPlayers() {
		for (Player player : this.currentlyPlaying) {
			player.setDealerHand(this.dealer.getDealerHand());
		}
	}

	protected void turn() {
		this.dealer.turn();
		this.updateDealerHandForPlayers();
	}

	protected void river() {
		this.dealer.river();
		this.updateDealerHandForPlayers();
	}

	/*
	 * Resets all player's status for the next turn
	 */
	protected void resetPlayers() {
		for (Player player : this.currentlyPlaying) {
			player.setCurrentlyRaising(false);
			player.setHasNotFolded(true);
			player.resetBet();
			player.setHand(null);
		}
	}

	protected int getNumberOfPots() {
		return this.pots.size();
	}

	/**
	 * Gets payment for blinds from the associated players
	 */
	protected void askBlindPayment() {
		this.bigBlind.getPlayer().bet(this.bigBlind.getValue());
		this.smallBlind.getPlayer().bet(this.smallBlind.getValue());
		this.findHighestBet();
	}

	/**
	 * Finds the highest bet in the current turn
	 */
	protected void findHighestBet() {
		for (Player player : this.currentlyPlaying) {
			if (player.getBet() > this.highestBet) {
				this.highestBet = player.getBet();
			}
		}
	}

	public int getHighestBet() {
		return this.highestBet;
	}

	/**
	 * Used to increase blinds
	 */
	protected void increaseBlinds() {
		this.bigBlind.increase(DEFAULT_BLIND);
		this.smallBlind.increase(DEFAULT_BLIND / 2);
	}

	/**
	 * Used to check if it makes sense to create a new AllIn Pot or not. Creating a
	 * new AllInPot makes sense if a player is all-in with less chips than other
	 * players, or if at least 2 more players have the possibility to bet after the
	 * player we're making a pot for is all-in.
	 */
	protected boolean checkIfAnyoneCanStillBet() {
		int numberOfPlayersStillBetting = this.currentlyPlaying.size();
		boolean flag = false;
		int bet = this.currentlyPlaying.get(0).getBet();
		for (Player player : this.currentlyPlaying) {
			if (player.isAllIn() || !player.hasNotFolded()) {
				numberOfPlayersStillBetting--;
			}
			if (player.getBet() != bet) {
				flag = true;
			}
		}
		return numberOfPlayersStillBetting >= 2 || flag;
	}
	// pot implementation
	// -------------------------------------------------------------------

	/**
	 * Creates a new pot, used for pot initialization of the base pot
	 */
	protected void createPot() {
		this.pots.add(new Pot());
	}

	/**
	 * Updates the base pot's value.
	 */
	protected void updatePotValue(Pot pot) {
		// set value to 0 then add every player bet
		pot.setValue(0);
		for (Player player : this.currentlyPlaying) {
			pot.addBet(player.getBet());
		}

	}

	/**
	 * Add every player to the pot, even folded ones (they won't be able to win the
	 * pot, but their bets will still be in it!)
	 * 
	 * @param player that needs a new pot because he is all in now
	 */
	protected void makePotForAllInPlayer(Player player) {
		this.pots.add(new AllInPot(player.getBet()));
		// work on the pot we just created
		AllInPot pot = (AllInPot) this.pots.get(this.pots.size() - 1);
		int playerBet = player.getBet();
		for (Player playa : this.currentlyPlaying) {
			// if a player is all in with less chips, we add his bet value to the pot
			// else we add the value of the all in player
			if (playa.getBet() > 0) {

				pot.addBet(Math.min(playa.getBet(), playerBet));
				if (playa.getBet() >= playerBet) {
					pot.addPlayer(playa);
				}
			}
		}
	}

	/**
	 * Updates an all-in pot with the value that a <Player> would win
	 * if he were to win the pot.
	 * 
	 * @param pot : the <AllInPot> associated with the <Player>
	 */
	protected void updateAllInPot(AllInPot pot) {
		pot.setValue(0);
		for (Player player : this.currentlyPlaying) {
			pot.addBet(Math.min(pot.getThresholdBet(), player.getBet()));
		}
	}

	public Blind getBigBlind() {
		return this.bigBlind;
	}

	public Blind getSmallBlind() {
		return this.smallBlind;
	}

	// Clears pots for next round
	protected void clearPots() {
		this.pots.clear();
	}

	// Updates the value contained in all the pots
	protected void updateAllPotsValues() {
		for (Pot pot : this.pots) {
			if (pot instanceof AllInPot) {
				this.updateAllInPot((AllInPot) pot);
			} else {
				this.updatePotValue(pot);
			}
		}
	}

	// Asks for bets ("with pots" because this function used to not support pots)
	protected abstract int askForBetsWithPots(int playersInRound);

	protected int makeAllInPotIfNecessary(int playersInRound) {
		for (Player player : this.currentlyPlaying) {

			if (player.hasNotFolded() && player.isAllIn()) {
				playersInRound--;
				if (this.checkIfAnyoneCanStillBet())
					this.makePotForAllInPlayer(player);

			}
		}
		return playersInRound;
	}

	/**
	 * A player is still competing for the base pot when he managed to pay through
	 * every raise without being all in OR if he's all in but with the most chips
	 */
	protected void addCompetingPlayersToBasePot() {
		for (Player player : this.currentlyPlaying) {
			if (player.hasNotFolded() && player.getBet() >= this.pots.get(0).getThresholdBet()) {
				this.pots.get(0).addPlayer(player);
			}
		}
	}

	protected void setThresholdForBasePot() {
		this.pots.get(0).setThresholdBet(this.highestBet);
	}

	// public for unit testing
	public void resetTable() {
		kickBrokePlayers();
		deck.resetDeck();
		dealer.clear();
		this.switchBlinds();
		this.invertedColor = null; // reset inverted color
		this.numberOfTurns++;
		this.totalBets = 0;
		this.highestBet = 0;
		if (this.numberOfTurns % TURNS_FOR_BLIND_INCREASE == 0) {
			increaseBlinds();
		}
		clearPots();
		resetSuperpowerUsage();
		resetPlayers();
	}

	/**
	 * Pays a pot to the players that won it
	 * If there's a draw between n players, splits the pot n ways
	 * With our implementation, if an all-in player wins, he will get paid
	 * accordingly, then
	 * the rest of the players will compete for the remainder
	 * 
	 * @param pot
	 */
	protected void payoutPot(Pot pot) {
		int value = pot.getValue();
		if (value <= 0) {
			return;
		}
		List<Player> winners = checkWhoWins(pot.getPlayers());
		if (winners == null) {
			return;
		}
		this.distributeGains(winners, value);

		for (Pot aPot : this.pots) {
			aPot.setValue(aPot.getValue() - value);
		}
	}

	protected void distributeGains(List<Player> winners, int value) {
		for (Player player : winners) {
			this.won(player, value / winners.size());
		}
	}

	protected void won(Player player, int value) {
		player.won(value);
	}


	public int getSuperpowerUseNumber(String name) {
		if (name.equals("add")) {
			return this.superpowerAdd.getNumberOfUses();
		}
		if (name.equals("addHidden")) {
			return this.superpowerAddHidden.getNumberOfUses();
		}
		if (name.equals("destroy")) {
			return this.superpowerDestroy.getNumberOfUses();
		}
		if (name.equals("show")) {
			return this.superpowerShow.getNumberOfUses();
		}
		return -1;
	}

	// used in a turn for card & interaction related functionalities (dealing,
	// betting,superpowers)
	protected abstract void turnCards();

	// Handles functionalities related to pots in a turn
	// This function is called after the dealer has dealt all the cards
	// This method is public because it's used in unit testing
	public void turnPots() {
		createPot();
		makeAllInPotIfNecessary(0);
		this.updateAllPotsValues();
		// find highest bet (useful for unit testing and double checking)
		this.findHighestBet();
		// threshold for base pot is the highest bet
		this.setThresholdForBasePot();
		// only players that have paid the threshold bet will be added to base pot
		this.addCompetingPlayersToBasePot();
		// sort the pots by increasing order
		this.pots.sort(null);
		for (int i = 0; i < this.pots.size(); i++) {
			payoutPot(this.pots.get(i));
		}
	}

	// Starts a turn (a round)
	public void startTurnWithPots() {
		this.initializeBlinds();
		this.buildQueue();
		turnCards();
		turnPots();
	}

	public Player play() {
		this.initializeShownCards();
		while (this.gameContinues()) {
			this.startTurnWithPots();
			this.resetTable();
		}
		return this.currentlyPlaying.get(0);
	}

	// unit testing purposes
	protected Dealer getDealer() {
		return this.dealer;
	}

	// unit testing purposes
	protected List<Player> getPlayers() {
		return this.currentlyPlaying;
	}

	protected boolean gameContinues() {
		return this.howManyAreStillPlaying() > 1;
	}

	protected abstract int askForSuperpowerUse(Player player);

	protected void call(Player player) {
		player.call(this.highestBet - player.getBet());
	}

	protected void fold(Player player) {
		player.fold();
	}

	protected void raise(Player player, int x) {
		if (x > 0) {
			for (Player aPlayer : this.currentlyPlaying) {
				aPlayer.setCurrentlyRaising(false);
			}
			player.setCurrentlyRaising((true));
			player.bet(this.highestBet - player.getBet() + x);
			this.highestBet = player.getBet();
		} else {
			player.call(this.highestBet - player.getBet());
		}
	}

	protected void askAndUseSuperpower(Player player) {
		int answer = askForSuperpowerUse(player);
		useSuperpower(player, answer);
	}

	public Player strToPlayer(String name) {
		for (Player player : this.currentlyPlaying) {
			if (player.getName().equals(name)) {
				return player;
			}
		}
		return null;
	}

	protected abstract Player useSuperpower(Player player, int answer);

	protected abstract Player useSuperpower(Player player, String name);

	protected void updateShownCards() {
		for (Player player : this.currentlyPlaying) { // update cards that each player knows
			for (Player otherPlayer : this.currentlyPlaying) { // from all the other player hands
				if (!player.equals(otherPlayer)) {
					for (Card card : otherPlayer.getPlayerHand().getHand()) {
						if (card.isFaceUp()) {
							player.getCardsKnownFromOtherPlayers().get(otherPlayer).add(card);
						}
					}
				}
			}
		}
	}
	protected void initializeShownCards() {
		for (Player player : this.currentlyPlaying) { 
			for (Player otherPlayer : this.currentlyPlaying) { 
				if (!player.equals(otherPlayer)) {
					player.getCardsKnownFromOtherPlayers().putIfAbsent(otherPlayer, new HashSet<>());
				}
			}
		}
	}

	protected abstract void askAndSetInvertedColor();

	protected void setInvertedColor(int answer) {
		CardColor toInvertTo = null;
		switch (answer) {
			case 0:
				toInvertTo = CardColor.SPADE;
				break;
			case 1:
				toInvertTo = CardColor.HEART;
				break;
			case 2:
				toInvertTo = CardColor.DIAMOND;
				break;
			case 3:
				toInvertTo = CardColor.CLOVER;
				break;
			default:
				break;
		}
		this.invertedColor = toInvertTo;
		for (Player player : this.currentlyPlaying) {
			player.setInvertedColor(toInvertTo);
		}
	}
	//Used for unit testing
	public CardColor getInvertedColor() {
		return this.invertedColor;
	}
}
