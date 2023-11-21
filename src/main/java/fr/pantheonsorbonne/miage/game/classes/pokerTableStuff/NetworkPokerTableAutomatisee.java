package fr.pantheonsorbonne.miage.game.classes.pokerTableStuff;

import java.util.Set;
import java.util.stream.Collectors;

import fr.pantheonsorbonne.miage.Facade;
import fr.pantheonsorbonne.miage.HostFacade;
import fr.pantheonsorbonne.miage.model.Game;
import fr.pantheonsorbonne.miage.model.GameCommand;

public class NetworkPokerTableAutomatisee extends PokerTableAutomatisee{
    private static final int PLAYER_COUNT = 4;
    private final HostFacade hostFacade;
    private final Game poker;
    private final Set<String> players;
    public NetworkPokerTableAutomatisee(HostFacade hostFacade, Set<String> players, fr.pantheonsorbonne.miage.model.Game poker) {
        super();
        this.hostFacade = hostFacade;
        this.players = players;
        this.poker = poker;
    }
    public static void main(String[] args) {
        //create the host facade
        HostFacade hostFacade = Facade.getFacade();
        hostFacade.waitReady();

        //set the name of the player
        hostFacade.createNewPlayer("Host");

        //create a new game of war
        fr.pantheonsorbonne.miage.model.Game war = hostFacade.createNewGame("POKER");

        //wait for enough players to join
        hostFacade.waitForExtraPlayerCount(PLAYER_COUNT);

        PokerTableAutomatisee host = new NetworkPokerTableAutomatisee(hostFacade, war.getPlayers(), war);
        host.play();
        System.exit(0);


    }
}

