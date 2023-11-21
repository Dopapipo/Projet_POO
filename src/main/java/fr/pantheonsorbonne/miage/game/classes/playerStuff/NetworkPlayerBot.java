package fr.pantheonsorbonne.miage.game.classes.playerStuff;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import fr.pantheonsorbonne.miage.Facade;
import fr.pantheonsorbonne.miage.PlayerFacade;
import fr.pantheonsorbonne.miage.game.classes.cards.Card;
import fr.pantheonsorbonne.miage.game.classes.cards.WinningCombination;
import fr.pantheonsorbonne.miage.model.Game;
import fr.pantheonsorbonne.miage.model.GameCommand;

public class NetworkPlayerBot {
    static final PlayerFacade playerFacade = Facade.getFacade();
    static final String playerId = "Player-" + new Random().nextInt();
    static Game poker;
    static Player player = new PlayerBotSmarter(playerId,300);
     public static void main(String[] args) {

        playerFacade.waitReady();
        playerFacade.createNewPlayer(playerId);
        poker = playerFacade.autoJoinGame("Poker");
        while (true) {

            GameCommand command = playerFacade.receiveGameCommand(poker);
            switch (command.name()) {
                case "payBlind":
                    handlePayBlind(command);
                    break;
                case "raiseCallOrFold":
                    handleRaiseCallOrFold(command);
                    break;
                case "showCards":
                    handleShowCards(command);
                case "askSuperpower" :
                    handleAskSuperpower(command);
                    break;
                case "askSuperpowerTarget" :
                    handleAskSuperpowerTarget(command);
                    break;
                case "payout" :
                    handlePayout(command);
                    break;
                case "kick" :
                    handleKick(command);
                    break;
                

            }
        }
    }
    private static void handleAskSuperpowerTarget(GameCommand command) {
        String target = ((PlayerBot)player).askForPlayerToUseSuperpowerOn(command.body());
        playerFacade.sendGameCommandToPlayer(poker,poker.getHostName(), new GameCommand("superpowerTarget",target));
    }
    private static void handleAskSuperpower(GameCommand command) {
        String superpower = String.valueOf(((PlayerBot)player).getSuperpower());
        playerFacade.sendGameCommandToPlayer(poker,poker.getHostName(), new GameCommand("superpower",superpower));
    }
    private static void handleKick(GameCommand command) {
        playerFacade.sendGameCommandToAll(poker, new GameCommand("I have been kicked :( - ", playerId));
    }
    private static void handleShowCards(GameCommand command) {
        player.getPlayerHand().showRandomCard();
    }
    private static void handlePayout(GameCommand command) {
        player.won(Integer.valueOf(command.body()));
    }
    private static void handleRaiseCallOrFold(GameCommand command) {
        int playerCommand = ((PlayerBot)player).getCommand();
        int playerBetAmount = ((PlayerBot)player).getBetAmount();
        int highestBet = Integer.valueOf(command.body());
        switch (playerCommand) {
            case 1:
                player.call(Integer.valueOf(command.body())-player.getBet());
                break;
            case 2:
                player.fold();
                break;
            case 3:
                player.bet(highestBet+playerBetAmount-player.getBet());
        }
        playerFacade.sendGameCommandToPlayer(poker,poker.getHostName(), new GameCommand(((PlayerBot)player).stringFromCommand(playerCommand),String.valueOf(((PlayerBot)player).getBetAmount())));
    }
    private static void handlePayBlind(GameCommand command) {
        int toPay = Integer.valueOf(command.body());
        bet(toPay);
    }

    public static void bet(int howMuch) {
		player.bet(howMuch);

	}

    
}