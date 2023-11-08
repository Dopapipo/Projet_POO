package fr.pantheonsorbonne.miage.game.main;

import java.util.ArrayList;
import java.util.List;

import fr.pantheonsorbonne.miage.game.classes.Player;
import fr.pantheonsorbonne.miage.game.classes.PlayerBot;
import fr.pantheonsorbonne.miage.game.classes.PokerTableAutomatisee;

public class LocalTexasHoldEm {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		PlayerBot p1 = new PlayerBot("Romain", 200);
		PlayerBot p2 = new PlayerBot("Lucian", 200);
		PlayerBot p3 = new PlayerBot("Raymond",200);
		PlayerBot p4 = new PlayerBot("Abel",200);
		List <PlayerBot> playerList = new ArrayList<>();
		playerList.add(p1);
		playerList.add(p2);
		playerList.add(p3);
		playerList.add(p4);
		PokerTableAutomatisee table = new PokerTableAutomatisee(playerList);
		while (table.howManyAreStillPlaying() > 1) {
			table.startTurnWithPots();
			System.out.println(p1.getName() + ": " + p1.getChipStack());
			System.out.println(p2.getName() + ": " + p2.getChipStack());
			System.out.println(p3.getName() + ": " + p3.getChipStack());
			System.out.println(p4.getName() + ": " + p4.getChipStack());
			
		}
	}

}
