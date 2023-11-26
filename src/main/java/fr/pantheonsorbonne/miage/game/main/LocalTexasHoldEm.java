package fr.pantheonsorbonne.miage.game.main;

import java.util.ArrayList;
import java.util.List;

import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.PlayerBot;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.PlayerBotSmarter;
import fr.pantheonsorbonne.miage.game.classes.pokerTableStuff.PokerTableAutomatisee;


/*
 * Some general comments:
 * 
 */
public class LocalTexasHoldEm {

	public static void main(String[] args) {
		//uncomment below to run an individual game
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
		//The line below will print the result of 1000 games between 2 smart and 2 dumb bots
		System.out.println(runSimulations(1000));
	}
	static String runSimulations(int howMany) {
		int smartWins=0;
		int otherWins=0;
		for (int i =0;i<howMany;i++) {
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
