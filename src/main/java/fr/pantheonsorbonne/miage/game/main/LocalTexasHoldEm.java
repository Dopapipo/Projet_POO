package fr.pantheonsorbonne.miage.game.main;

import java.util.ArrayList;
import java.util.List;

import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.PlayerBot;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.PlayerBotSmarter;
import fr.pantheonsorbonne.miage.game.classes.pokerTableStuff.PokerTableAutomatisee;

public class LocalTexasHoldEm {

	public static void main(String[] args) {

		Player p1 = new PlayerBotSmarter("Romain", 300);
		Player p2 = new PlayerBotSmarter("Lucian", 300);
		Player p3 = new PlayerBotSmarter("Raymond", 300);
		Player p4 = new PlayerBotSmarter("Abel", 300);
		List<Player> playerList = new ArrayList<>();
		playerList.add(p1);
		playerList.add(p2);
		playerList.add(p3);
		playerList.add(p4);
		PokerTableAutomatisee table = new PokerTableAutomatisee(playerList);
		table.play();
	}

}
