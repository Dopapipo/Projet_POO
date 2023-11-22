package fr.pantheonsorbonne.miage.game.classes.playerStuff;

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
    static final PlayerFacade playerFacade = Facade.getFacade();
    static final String playerId = "Player-" + new Random().nextInt();
    static Game poker;
    static Player player = new PlayerBotSmarter(playerId, 300);

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
                case "askSuperpower":
                    handleAskSuperpower(command);
                    break;
                case "askSuperpowerTarget":
                    handleAskSuperpowerTarget(command);
                    break;
                case "payout":
                    handlePayout(command);
                    break;
                case "kick":
                    handleKick(command);
                    break;
                case "invertedColor":
                    handleInvertedColor(command);
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

            }
        }
    }

    private static void handleAskSuperpowerTarget(GameCommand command) {
        String target = ((PlayerBot) player).askForPlayerToUseSuperpowerOn(command.body());
        playerFacade.sendGameCommandToPlayer(poker, poker.getHostName(), new GameCommand("superpowerTarget", target));
    }

    private static void handleAskSuperpower(GameCommand command) {
        String superpower = String.valueOf(((PlayerBot) player).getSuperpower());
        playerFacade.sendGameCommandToPlayer(poker, poker.getHostName(), new GameCommand("superpower", superpower));
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
        int playerCommand = ((PlayerBot) player).getCommand();
        int playerBetAmount = ((PlayerBot) player).getBetAmount();
        int highestestBet = Integer.valueOf(command.body());
        switch (playerCommand) {
            case 1:
                player.call(highestestBet - player.getBet());
                break;
            case 2:
                player.fold();
                break;
            case 3:
                player.bet(highestestBet + playerBetAmount - player.getBet());
        }
        playerFacade.sendGameCommandToPlayer(poker, poker.getHostName(),
                new GameCommand(String.valueOf(playerCommand), String.valueOf(playerBetAmount)));
    }

    private static void handlePayBlind(GameCommand command) {
        int toPay = Integer.valueOf(command.body());
        bet(toPay);
    }

    private static void bet(int howMuch) {
        player.bet(howMuch);

    }

    private static void handleInvertedColor(GameCommand command) {
        playerFacade.sendGameCommandToPlayer(poker, poker.getHostName(),
                new GameCommand("invertedColorAnswer", String.valueOf(((PlayerBot) player).askForInvertedColor())));
    }

    private static void handleGiveCards(GameCommand command) {
        List<Card> cards = Arrays.stream(command.body().split(",")).map(Card::stringToCard)
                .collect(Collectors.toList());
        player.setHand(new PlayerHand(cards));
    }

    private static void handleCardAdded(GameCommand command) {
        Card card = Card.stringToCard(command.body());
        player.getPlayerHand().add(card);
    }

    private static void handleCardSeen(GameCommand command) {
        String[] args = command.body().split(",");
        Card card = Card.stringToCard(args[0]);
        Player playerSeen = new Player(args[1], Integer.valueOf(args[2]));
        player.getCardsKnownFromOtherPlayers().get(playerSeen).add(card);
    }

    private static void handleCardDestroyed(GameCommand command) {
        String[] args = command.body().split(",");
        Card card = Card.stringToCard(args[0]);
        String playerName = args[1];
        int playerChips = Integer.valueOf(args[2]);
        if (player.getName().equals(playerName)) {
            player.getPlayerHand().remove(card);
        } else {
            for (Player player : player.getCardsKnownFromOtherPlayers().keySet()) {
                if (player.getName().equals(playerName)) {
                    player.getCardsKnownFromOtherPlayers().get(new Player(playerName, playerChips)).remove(card);
                }
            }
        }
    }

    private static void handleLostMoney(GameCommand command) {
        int amount = Integer.valueOf(command.body());
        // other functions handle checks so this function should never malfunction
        player.setChipStack(player.getChipStack() - amount);
    }

}