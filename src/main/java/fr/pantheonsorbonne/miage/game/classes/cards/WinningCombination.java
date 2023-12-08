package fr.pantheonsorbonne.miage.game.classes.cards;

/*
 * A WinningCombination is used to represent a poker hand strength, for instance
 * Pair of Kings, or Jack high straight
 * We can compare them, and the higher the WinningCombination, the better the hand
 * Highest WinningCombination will win the pot.
 */
public class WinningCombination implements Comparable<WinningCombination> {
	private WinCondition winCondition;
	private CardValue cardValue;

	public WinningCombination(WinCondition wc, CardValue cv) {
		this.winCondition = wc;
		this.cardValue = cv;
	}
	//A WinningCombination is greater than another if its WinCondition is greater
	//Or if its WinCondition is equal and its CardValue is greater
	@Override
	public int compareTo(WinningCombination wc) {
		if (this.winCondition.compareTo(wc.winCondition) > 0) {
			return 1;
		}
		if (this.winCondition.compareTo(wc.winCondition) == 0) {
			return this.cardValue.compareTo(wc.cardValue);
		}
		return -1;
	}

	public WinCondition getWinCondition() {
		return winCondition;
	}

	public CardValue getCardValue() {
		return cardValue;
	}

	@Override
	public String toString() {
		return this.cardValue + " " + this.winCondition;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null)
			return false;
		if (other instanceof WinningCombination) {
			return this.compareTo((WinningCombination) other) == 0;
		}
		return false;
	}
	//Sonarlint says we should override hashCode when we override equals so let's do it
	@Override
	public int hashCode() {
		return this.cardValue.hashCode() + this.winCondition.hashCode();
	}

}
