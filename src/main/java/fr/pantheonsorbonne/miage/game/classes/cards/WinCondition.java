package fr.pantheonsorbonne.miage.game.classes.cards;

public enum WinCondition {
	HIGH_CARD(1), PAIR(2), TWO_PAIR(3), THREE_OF_A_KIND(6), STRAIGHT(12), FLUSH(24), FULL_HOUSE(31), FOUR_OF_A_KIND(39),
	STRAIGHT_FLUSH(48),
	ROYAL_FLUSH(58);

	private int rank;
	// Royal flush is a straight flush with maxCard=ACE
	// Full house is a ToaK + a pair combined
	// Straight flush is a straight and a flush combined
	// careful because the cards from the flush & the straight must be the same!

	/**
	 * returns resp. positive,0,negative integer if this WinCondition ranks
	 * higher,equal,lower than the WinCondition condition
	 * 
	 * @param condition : the WinCondition we compare this to
	 * @return positive,0,negative integer
	 */
	private WinCondition(int rank) {
		this.rank = rank;
	}

	// This could be useful to write algorithms for the bots to decide what to do,
	public int getRank() {
		return this.rank;
	}

	public int isHigher(WinCondition condition) {
		return this.ordinal() - condition.ordinal();

	}

}
