package fr.pantheonsorbonne.miage.game.classes.cards;


public enum WinCondition {
	HIGH_CARD, PAIR, TWO_PAIR, THREE_OF_A_KIND, STRAIGHT, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH,
	ROYAL_FLUSH;
	//Royal flush is a straight flush with maxCard=ACE
	//Full house is a ToaK + a pair combined
	//Straight flush is a straight and a flush combined

	/**
	 * returns resp. positive,0,negative integer if this WinCondition ranks
	 * higher,equal,lower than the WinCondition condition
	 * 
	 * @param condition : the WinCondition we compare this to
	 * @return positive,0,negative integer
	 */

	public int isHigher(WinCondition condition) {
			return this.ordinal() - condition.ordinal();

	}

}
