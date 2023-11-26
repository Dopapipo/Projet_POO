package fr.pantheonsorbonne.miage.game.classes.playerStuff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import fr.pantheonsorbonne.miage.Facade;
import fr.pantheonsorbonne.miage.PlayerFacade;
import fr.pantheonsorbonne.miage.game.classes.cards.Card;
import fr.pantheonsorbonne.miage.model.Game;
import fr.pantheonsorbonne.miage.model.GameCommand;


public class NetworkPlayerBot {
    private static final PlayerFacade playerFacade = Facade.getFacade();
    private static final String playerId = "Player-" + new Random().nextInt();
    private static Game poker;
    private static Player player = new PlayerBotSmarter(playerId, 300);

    public static void main(String[] args) {

        playerFacade.waitReady();
        playerFacade.createNewPlayer(playerId);
        poker = playerFacade.autoJoinGame("Poker");
        outerLoop:
        while (true) {

            GameCommand command = playerFacade.receiveGameCommand(poker);
            switch (command.name()) {
                case "payBlind":
                    handlePayBlind(command);
                    break;
                case "raiseCallOrFold":
                    handleRaiseCallOrFold(command);
                    break;
                case "askSuperpower":
                    handleAskSuperpower();
                    break;
                case "askSuperpowerTarget":
                    handleAskSuperpowerTarget(command);
                    break;
                case "payout":
                    handlePayout(command);
                    break;
                case "kick":
                    System.out.println(player.getName() +" has lost the game!");
                    break outerLoop;
                case "invertedColor":
                    handleInvertedColor();
                    break;
                case "giveCards":
                    handleGiveCards(command);
                    break;
                case "cardAdded":
                    handleCardAdded(command);
                    break;
                case "cardDestroyed":
                    handleCardDestroyed(command);
                    break;
                case "cardSeen":
                    handleCardSeen(command);
                    break;
                case "lostMoney":
                    handleLostMoney(command);
                    break;
                case "updateDealer":
                    handleUpdateDealer(command);
                    break;
                default:
                    break;

            }
        }
    }

    private static void handleAskSuperpowerTarget(GameCommand command) {
        String target = ((PlayerBotSmarter) player).askForPlayerToUseSuperpowerOn(command.body());
        playerFacade.sendGameCommandToPlayer(poker, poker.getHostName(), new GameCommand("superpowerTarget", target));
    }

    private static void handleAskSuperpower() {
        String superpower = String.valueOf(((PlayerBotSmarter) player).getSuperpower());
        playerFacade.sendGameCommandToPlayer(poker, poker.getHostName(), new GameCommand("superpower", superpower));
    }

    private static void handlePayout(GameCommand command) {
        System.out.println(player.getName() +" has won chips: " + command.body());
        player.won(Integer.parseInt(command.body()));
    }

    private static void handleRaiseCallOrFold(GameCommand command) {
        int highestestBet = Integer.parseInt(command.body());
        int playerCommand = ((PlayerBotSmarter) player).getCommand(highestestBet-player.getBet());
        int playerBetAmount = ((PlayerBotSmarter) player).getBetAmount(highestestBet-player.getBet());
        switch (playerCommand) {
            case 1:
                player.call(highestestBet - player.getBet());
                break;
            case 2:
                player.fold();
                break;
            case 3:
                player.bet(highestestBet + playerBetAmount - player.getBet());
                break;
            default:
                break;
        }
        playerFacade.sendGameCommandToPlayer(poker, poker.getHostName(),
                new GameCommand(String.valueOf(playerCommand), String.valueOf(playerBetAmount)));
    }

    private static void handlePayBlind(GameCommand command) {
        int toPay = Integer.parseInt(command.body());
        bet(toPay);
    }

    private static void bet(int howMuch) {
        player.bet(howMuch);

    }

    private static void handleInvertedColor() {
        playerFacade.sendGameCommandToPlayer(poker, poker.getHostName(),
                new GameCommand("invertedColorAnswer", String.valueOf(((PlayerBotSmarter) player).askForInvertedColor())));
    }

    private static void handleGiveCards(GameCommand command) {
        List <Card> cards = new ArrayList<>(); 
        String[] args = command.body().split(",");
        for (int i = 0; i < args.length; i++) {
            cards.add(Card.stringToCard(args[i]));
        }
        player.setHand(new PlayerHand(cards));
    }

    private static void handleCardAdded(GameCommand command) {
        Card card = Card.stringToCard(command.body());
        player.addCard(card);
    }

    private static void handleCardSeen(GameCommand command) {
        String[] args = command.body().split(",");
        Card card = Card.stringToCard(args[0]);
        Player playerSeen = new Player(args[1], Integer.parseInt(args[2]));
        player.updateCardSeen(playerSeen, card);
    }

    private static void handleCardDestroyed(GameCommand command) {
        String[] args = command.body().split(",");
        Card card = Card.stringToCard(args[0]);
        String playerName = args[1];
        int playerChips = Integer.parseInt(args[2]);
        if (player.getName().equals(playerName)) {
            player.remove(card);
        } else {
            for (Player player : player.getCardsKnownFromOtherPlayers().keySet()) {
                if (player.getName().equals(playerName)) {
                    player.getCardsKnownFromOtherPlayers().get(new Player(playerName, playerChips)).remove(card);
                }
            }
        }
    }

    private static void handleLostMoney(GameCommand command) {
        int amount = Integer.parseInt(command.body());
        // other functions handle checks so this function should never malfunction
        player.setChipStack(player.getChipStack() - amount);
    }
    private static void handleUpdateDealer(GameCommand command) {
        player.setDealerHand(Arrays.stream(command.body().split(",")).map(Card::stringToCard).collect(Collectors.toList()));
    }
}