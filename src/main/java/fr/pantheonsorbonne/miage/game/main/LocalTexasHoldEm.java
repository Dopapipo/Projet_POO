package fr.pantheonsorbonne.miage.game.main;

import java.util.ArrayList;
import java.util.List;

import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.PlayerBot;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.PlayerBotSmarter;
import fr.pantheonsorbonne.miage.game.classes.pokerTableStuff.PokerTableAutomatisee;

public class LocalTexasHoldEm {

	public static void main(String[] args) {

		// Player p1 = new PlayerBotSmarter("Romain", 300);
		// Player p2 = new PlayerBot("Lucian", 300);
		// Player p3 = new PlayerBot("Raymond", 300);
		// Player p4 = new PlayerBot("Abel", 300);
		// List<Player> playerList = new ArrayList<>();
		// playerList.add(p1);
		// playerList.add(p2);
		// playerList.add(p3);
		// playerList.add(p4);
		// PokerTableAutomatisee table = new PokerTableAutomatisee(playerList);
		// System.out.println(table.play().getName());

		System.out.println(runSimulations());
	}
	static String runSimulations() {
		int smartWins=0;
		int otherWins=0;
		for (int i =0;i<1000;i++) {
			Player p1 = new PlayerBotSmarter("Romain", 300);
			Player p2 = new PlayerBotSmarter("Lucian", 300);
			Player p3 = new PlayerBot("Raymond", 300);
			Player p4 = new PlayerBot("Abel", 300);
			List<Player> playerList = new ArrayList<>();
			playerList.add(p1);
			playerList.add(p2);
			playerList.add(p3);
			playerList.add(p4);
			PokerTableAutomatisee table = new PokerTableAutomatisee(playerList);
			Player winner = table.play();
			if (winner instanceof PlayerBotSmarter) {
				smartWins++;
			}
			else {
				otherWins++;
			}
			System.out.println("smart: " + smartWins + " other: " + otherWins);
		}
		if (smartWins>otherWins) {
			return PlayerBotSmarter.class.getName();
		}
		else {
			return PlayerBot.class.getName();
		}
	}

}
