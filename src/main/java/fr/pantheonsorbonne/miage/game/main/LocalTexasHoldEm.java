package fr.pantheonsorbonne.miage.game.main;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.PlayerBot;
import fr.pantheonsorbonne.miage.game.classes.pokerTableStuff.PokerTableAutomatisee;

public class LocalTexasHoldEm {

	public static void main(String[] args) {

		PlayerBot p1 = new PlayerBot("Romain", 300);
		PlayerBot p2 = new PlayerBot("Lucian", 300);
		PlayerBot p3 = new PlayerBot("Raymond",300);
		PlayerBot p4 = new PlayerBot("Abel",300);
		List <Player> playerList = new ArrayList<>();
		playerList.add(p1);
		playerList.add(p2);
		playerList.add(p3);
		playerList.add(p4);
		PokerTableAutomatisee table = new PokerTableAutomatisee(playerList);
		while (table.gameContinues()) {
			table.startTurnWithPots();
			System.out.println(p1.getName() + ": " + p1.getChipStack());
			System.out.println(p2.getName() + ": " + p2.getChipStack());
			System.out.println(p3.getName() + ": " + p3.getChipStack());
			System.out.println(p4.getName() + ": " + p4.getChipStack());
			
		}
	}
	
}
