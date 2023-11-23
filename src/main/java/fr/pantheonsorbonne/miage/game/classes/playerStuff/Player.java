package fr.pantheonsorbonne.miage.game.classes.playerStuff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.pantheonsorbonne.miage.game.classes.cards.Card;
import fr.pantheonsorbonne.miage.game.classes.cards.CardColor;
import fr.pantheonsorbonne.miage.game.classes.cards.WinningCombination;

public class Player implements Comparable<Player> {
	private String name;
	private int chipStack;
	private int bet;
	private WinningCombination combination;
	private PlayerHand playerHand;
	private boolean playing = true;
	private boolean hasNotFolded;
	private boolean currentlyRaising;
	private List<Card> dealerHand;
	private Map<Player, Set<Card>> cardsKnownFromOtherPlayers;
	private CardColor invertedColor;
	public Player(String name) {
		this(name, 0);
		this.dealerHand=new ArrayList<>();
	}

	public Player(String name, int chips) {
		this.name = name;
		this.chipStack = chips;
		this.playing = (chips > 0);
		this.hasNotFolded = true;
		this.cardsKnownFromOtherPlayers = new HashMap<>();
		this.dealerHand=new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public int getChipStack() {
		return chipStack;
	}
	public List<Card> getDealerHand() {
		return dealerHand;
	}
	public void setDealerHand(List<Card> dealerHand) {
		this.dealerHand = dealerHand;
	}
	public void setChipStack(int chipStack) {
		this.chipStack = chipStack;
	}

	public int getBet() {
		return bet;
	}
	public void resetHand() {
		this.playerHand.getHand().clear();
	}
	public void setBet(int bet) {
		this.bet = bet;
	}

	public PlayerHand getPlayerHand() {
		return playerHand;
	}
	public void setInvertedColor(CardColor inverted) {
		this.invertedColor=inverted;
	}
	public CardColor getInvertedColor() {
		return this.invertedColor;
	}
	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	// les méthodes
	public void setHand(PlayerHand cards) {
		this.playerHand = cards;
	}

	public void setWinCombination(WinningCombination winCondition) {
		this.combination = winCondition;
	}

	public void printHand() {
		System.out.println(this.name + " has the following hand :");
		for (Card card : this.playerHand.getHand()) {
			System.out.println(card);
		}
	}
	protected Player copy() {
		Player copy =  new Player(this.getName(),this.getChipStack());
		 copy.setCurrentlyPlaying(true);
		 copy.setHand(this.playerHand);
		 return copy;
	 }
	public void setCurrentlyPlaying(boolean g) {
		this.playing = true;
	}
	private int allIn() {
		if (!this.playing) {
			return 0;
		}
		this.bet += this.chipStack;
		this.chipStack = 0;
		return this.bet;
	}

	/**
	 * Allows a player to bet. If the players tries to bet more than they own, they
	 * automatically all-in. The player can't bet a negative value.
	 * 
	 * @param howMuch
	 * @return how much player is betting now
	 */
	public int bet(int howMuch) {
		if (!this.playing) {
			return 0;
		}
		if (howMuch < 0) {
			return 0;
		}
		if (this.chipStack - howMuch < 0) {
			return allIn();
		}
		this.chipStack -= howMuch;
		this.bet += howMuch;
		return this.bet;

	}

	public void call(int howMuch) {
		this.bet(howMuch);
		this.currentlyRaising = false;
	}

	public void fold() {
		this.hasNotFolded = false;
		this.currentlyRaising = false;
	}

	public void lost() {
		this.won(0);
	}

	public void won(int winnings) {
		this.chipStack += winnings;
		this.bet = 0;
	}

	public WinningCombination getWinningCombination() {
		return this.combination;
	}

	public boolean hasNotFolded() {
		return hasNotFolded;
	}

	public void setHasNotFolded(boolean hasNotFolded) {
		this.hasNotFolded = hasNotFolded;
	}

	@Override
	public int compareTo(Player player) {
		return (this.getWinningCombination().compareTo(player.getWinningCombination()));
	}

	public boolean isCurrentlyRaising() {
		return currentlyRaising;
	}

	public void setCurrentlyRaising(boolean currentlyRaising) {
		this.currentlyRaising = currentlyRaising;
	}

	public boolean isAllIn() {
		return this.chipStack == 0;
	}

	public String toString() {
		return this.name + " current chips: " + this.chipStack;
	}

	public void addCard(Card card) {
		this.playerHand.add(card);
	}

	public Card removeRandomCard() {
		return this.playerHand.removeRandomCard();
	}

	public Map<Player, Set<Card>> getCardsKnownFromOtherPlayers() {
		return cardsKnownFromOtherPlayers;
	}

	public void showRandomCard() {
		this.playerHand.showRandomCard();
	}

	public boolean allCardsAreShown() {
		return this.playerHand.allCardsAreShown();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj instanceof Player) {
			return this.name.equals(((Player) obj).getName()) && this.chipStack == ((Player) obj).getChipStack();
		}
		return false;
	}

}
