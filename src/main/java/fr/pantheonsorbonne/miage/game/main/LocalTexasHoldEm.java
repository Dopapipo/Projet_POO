package fr.pantheonsorbonne.miage.game.main;

import java.util.ArrayList;
import java.util.List;

import fr.pantheonsorbonne.miage.game.classes.Player;
import fr.pantheonsorbonne.miage.game.classes.PokerTableAutomatisee;

public class LocalTexasHoldEm {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Player p1 = new Player("Romain", 200);
		Player p2 = new Player("Lucian", 200);
		Player p3 = new Player("Raymond",200);
		Player p4 = new Player("Abel",200);
		List <Player> playerList = new ArrayList<>();
		playerList.add(p1);
		playerList.add(p2);
		playerList.add(p3);
		playerList.add(p4);
		PokerTableAutomatisee table = new PokerTableAutomatisee(playerList);
		while (table.howManyAreStillPlaying() > 1) {
			table.startTurnWithPots();
			System.out.println(p1.getName() + ": " + p1.getChipStack());
			System.out.println(p2.getName() + ": " + p2.getChipStack());
			System.out.println(p3.getName() + ": " + p2.getChipStack());
			System.out.println(p4.getName() + ": " + p2.getChipStack());
			
		}
	}

}
