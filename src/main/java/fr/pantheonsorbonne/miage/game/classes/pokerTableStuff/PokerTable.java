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
import fr.pantheonsorbonne.miage.game.classes.superpowers.SuperpowerChoice;
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

	protected PokerTable() {
		this.playerList = new ArrayList<>();
		this.currentlyPlaying = new ArrayList<>();
		this.deck = new Deck();
		this.dealer = new Dealer(deck);
		totalBets = 0;
		this.highestBet = 0;
		this.initializeSuperpowers();
		this.playerQueue = new LinkedList<Player>();
	}

	protected PokerTable(List<Player> players) {
		this();
		this.playerList = players;
		for (Player player : this.playerList) {
			if (player.isPlaying()) {
				this.currentlyPlaying.add(player);
			}
		}
		this.initializePlayerHands();
		this.initializeShownCards(); // will initialize player's maps

	}

	// Builds the queue used to ask for bets from the currentlyPlaying list
	// Big blind goes first, dealer goes last, like in real poker
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
			player.setHand(new PlayerHand(new ArrayList<>()));
		}
	}

	// Protected for unit testing, else would be private
	protected void initializeSuperpowers() {
		this.superpowerShow = new SuperpowerShow();
		this.superpowerDestroy = new SuperpowerDestroy();
		this.superpowerAdd = new SuperpowerAdd();
		this.superpowerAddHidden = new SuperpowerAddHidden();
	}

	private int howManyAreStillPlaying() {
		return this.currentlyPlaying.size();
	}

	// Protected for unit testing
	protected int getDefaultBlind() {
		return DEFAULT_BLIND;
	}

	// protected for inheritance purposes
	protected void initializeBlinds() {
		int n = this.howManyAreStillPlaying();
		if (n <= 1) {
			return;
		}
		// last player at the table will be first big blind, the one before him
		// will be small blind, and the one before that will be donor.
		// if there's only two players, one player will always be donor and small blind.
		if (this.bigBlind == null) {
			this.bigBlind = new Blind(DEFAULT_BLIND, this.currentlyPlaying.get(n - 1));
			this.smallBlind = new Blind(DEFAULT_BLIND / 2, this.currentlyPlaying.get(n - 2));
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
		// it's okay to use == here, because we're gonna be dealing with references
		// to players that will never change
		// (i.e. we'll never create another object supposed to represent the same
		// player)
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
		return removedNames; // our network table will love this return
	}

	/**
	 * Deals 2 new cards to every player from the current deck
	 */
	protected void giveCards() {
		for (Player player : this.currentlyPlaying) {
			player.setHand(new PlayerHand(this.deck.getRandomCards(2)));
		}
	}

	protected Deck getDeck() {
		return this.deck;
	}

	/**
	 * Checks what <Player> won the current round. Also supports draws
	 * 
	 * @param players
	 * @return
	 */
	protected List<Player> checkWhoWins(List<Player> players) {
		// Work on a copy so as not to sort the original list,
		// Just in case we call the method on this.currentlyPlaying
		// That would create bugs on the order of blinds and the order
		// We ask players for bets
		List<Player> playersCopy = new ArrayList<>(players);
		List<Player> playersThatWon = new ArrayList<>();
		if (playersCopy.isEmpty()) {
			return playersThatWon;
		}
		// Find each WinningCombination for each player and set it
		for (Player player : playersCopy) {
			player.setWinCombination(
					WinConditionLogic.findWinningCombination(this.invertedColor, dealer, player));
		}
		// Players are comparable, p1>p2 if
		// p1.getWinCombination()>p2.getWinCombination()
		// In other words, if p1 has a better hand than p2
		// So the winner will always be the last element of our players list
		Collections.sort(playersCopy);
		int index = playersCopy.size() - 1;
		Player wonForSure = playersCopy.get(index);
		// Look for the strongest player that hasn't folded
		while (index > 0 && !wonForSure.hasNotFolded()) {
			index--;
			wonForSure = playersCopy.get(index);
		}
		playersThatWon.add(wonForSure);
		// check for other players that haven't folded with similar strength hands
		for (Player player : playersCopy) {
			if (player.hasNotFolded() && !playersThatWon.contains(player) && wonForSure.compareTo(player) == 0) {
				playersThatWon.add(player);
			}
		}
		return playersThatWon;
	}

	// Resets super power usage, i.e. clears the list of players that already used
	// it
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

	// Players use the dealer hand for decision making, so we have to update it
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
	 * (i.e. resets bets, raises, folds, hands)
	 */
	private void resetPlayers() {
		for (Player player : this.currentlyPlaying) {
			player.setCurrentlyRaising(false);
			player.setHasNotFolded(true);
			player.resetBet();
			player.setHand(null);
		}
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

	protected int getHighestBet() {
		return this.highestBet;
	}

	// protected for unit testing
	protected void increaseBlinds() {
		this.bigBlind.increase(DEFAULT_BLIND);
		this.smallBlind.increase(DEFAULT_BLIND / 2);
	}

	// pot implementation
	// -------------------------------------------------------------------

	/**
	 * Creates a new pot, used for pot initialization of the base pot
	 */
	private void createPot() {
		this.pots.add(new Pot());
	}

	/**
	 * Updates the base pot's value.
	 */
	private void updatePotValue(Pot pot) {
		// set value to 0 then add every player bet
		pot.setValue(0);
		for (Player player : this.currentlyPlaying) {
			pot.addBet(player.getBet());
		}

	}

	/**
	 * Used to make a pot for an all-in player. This pot will be used to payout
	 * without
	 * bugs. If a player bets 10 and another 100, the player that bets 10 will win
	 * 20,
	 * and not 110. The other player will compete with the rest for the remainder.
	 * 
	 * @param player that needs a new pot because he is all in now
	 */
	private void makePotForAllInPlayer(Player player) {
		int playerBet = player.getBet();
		this.pots.add(new AllInPot(playerBet));
		// work on the pot we just created
		AllInPot pot = (AllInPot) this.pots.get(this.pots.size() - 1);
		for (Player playa : this.currentlyPlaying) {
			// if a player is all in with less chips, we add his bet value to the pot
			// else we add the value of the all in player
			if (playa.getBet() > 0) {
				pot.addBet(Math.min(playa.getBet(), playerBet));
				// players competing for this pot are only players that bet more than the all in
				// player
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
	private void updateAllInPot(AllInPot pot) {
		pot.setValue(0);
		for (Player player : this.currentlyPlaying) {
			pot.addBet(Math.min(pot.getThresholdBet(), player.getBet()));
		}
	}

	protected Blind getBigBlind() {
		return this.bigBlind;
	}

	protected Blind getSmallBlind() {
		return this.smallBlind;
	}

	// Clears pots for next round
	private void clearPots() {
		this.pots.clear();
	}

	// Updates the value contained in all the pots
	private void updateAllPotsValues() {
		for (Pot pot : this.pots) {
			if (pot instanceof AllInPot) {
				this.updateAllInPot((AllInPot) pot);
			} else {
				this.updatePotValue(pot);
			}
		}
	}

	protected abstract int askForBets(int playersInRound);

	private void makeAllInPotsIfNecessary() {
		for (Player player : this.currentlyPlaying) {

			if (player.hasNotFolded() && player.isAllIn()) {
				this.makePotForAllInPlayer(player);

			}
		}
	}

	/**
	 * A player is still competing for the base pot when he managed to pay through
	 * every raise.
	 */
	private void addCompetingPlayersToBasePot() {
		for (Player player : this.currentlyPlaying) {
			if (player.hasNotFolded() && player.getBet() >= this.pots.get(0).getThresholdBet()) {
				this.pots.get(0).addPlayer(player);
			}
		}
	}

	private void setThresholdForBasePot() {
		this.pots.get(0).setThresholdBet(this.highestBet);
	}

	// protected for unit testing
	protected void resetTable() {
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
	private void payoutPot(Pot pot) {
		int value = pot.getValue();
		if (value <= 0) {
			return;
		}
		List<Player> winners = checkWhoWins(pot.getPlayers());
		if (winners.isEmpty()) {
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

	// protected for unit testing
	protected int getSuperpowerUseNumber(String name) {
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
	// This method is protected because it's used in unit testing
	protected void turnPots() {
		createPot();
		makeAllInPotsIfNecessary();
		this.updateAllPotsValues();
		// find highest bet (this call is useful for unit testing and avoiding bugs/edge
		// cases)
		this.findHighestBet();
		// threshold for base pot is the highest bet
		this.setThresholdForBasePot();
		// only players that have paid the threshold bet will be added to base pot
		this.addCompetingPlayersToBasePot();
		// sort the pots by increasing order
		this.pots.sort(null);
		// Payout the pots in increasing order, so that we never payout more than we
		// should; if there's multiple pots of same value, we will only payout one of them
		for (int i = 0; i < this.pots.size(); i++) {
			payoutPot(this.pots.get(i));
		}
	}

	// Starts a turn (a round)
	private void startTurn() {
		this.initializeBlinds();
		this.buildQueue();
		turnCards();
		turnPots();
	}
	/**
	 * Starts a game of poker
	 * @return <Player> that won the game
	 */
	public Player play() {
		this.initializeShownCards();
		while (this.gameContinues()) {
			this.startTurn();
			this.resetTable();
		}
		return this.currentlyPlaying.get(0);
	}
	
	protected Dealer getDealer() {
		return this.dealer;
	}

	// unit testing purposes
	protected List<Player> getPlayers() {
		return this.currentlyPlaying;
	}

	//protected for unit testing
	protected boolean gameContinues() {
		return this.howManyAreStillPlaying() > 1;
	}

	protected abstract SuperpowerChoice askForSuperpowerUse(Player player);

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
		SuperpowerChoice answer = askForSuperpowerUse(player);
		useSuperpower(player, answer);
	}
	//Converts a string to a player
	protected Player strToPlayer(String name) {
		for (Player player : this.currentlyPlaying) {
			if (player.getName().equals(name)) {
				return player;
			}
		}
		return null;
	}
	//Different ways of using a superpower
	protected abstract Player useSuperpower(Player player, SuperpowerChoice answer);
	protected abstract Player useSuperpower(Player player, String name);

	protected void updateShownCards() {
		for (Player player : this.currentlyPlaying) { // update cards that each player knows
			for (Player otherPlayer : this.currentlyPlaying) { // from all the other player hands
				if (!player.equals(otherPlayer)) {
					for (Card card : otherPlayer.getHand()) {
						if (card.isFaceUp()) {
							player.getCardsKnownFromOtherPlayers().get(otherPlayer).add(card);
						}
					}
				}
			}
		}
	}
	//Initializes the maps of cards that each player knows from other players
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
	
	//Could've made the players return a color string instead of an int for their answer
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
			player.setInvertedColor(toInvertTo); //inform the players of the inverted color
		}
	}

	// Used for unit testing
	protected CardColor getInvertedColor() {
		return this.invertedColor;
	}
}
