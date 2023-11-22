package fr.pantheonsorbonne.miage.game.classes.pokerTableStuff;

/**
 * An all-in pot's purpose is to payout all-in players accordingly in case they
 * win.
 * In a poker table, there's only one pot and as many all in pots as there are
 * all-in players.
 * An all-in pot is a pot that has a thresholdBet equal to the all-in player's
 * bet.
 * This class exists for readability purposes, as functionally it is identical
 * to a pot, in the way
 * that it is used in the PokerTable class.
 */
public class AllInPot extends Pot {

	public AllInPot(int allInBet) {
		super(allInBet);
	}

}
