package fr.pantheonsorbonne.miage.game.classes.playerStuff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
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
        outerLoop: while (true) {

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
                    System.out.println(command.body() + " has lost the game!");
                    if (command.body().equals(player.getName()))
                        break outerLoop;
                    handleKick(command);
                    break;
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
                case "namesOfPlayers":
                    handleInitializePlayersKnown(command);
                    break;

            }
        }
    }
    // The following functions are used to handle the different types of commands

    //When the table asks us who do we want to target our superpower on
    private static void handleAskSuperpowerTarget(GameCommand command) {
        String target = ((PlayerBotSmarter) player).askForPlayerToUseSuperpowerOn(command.body());
        playerFacade.sendGameCommandToPlayer(poker, poker.getHostName(), new GameCommand("superpowerTarget", target));
    }
    //When the table asks us what superpower we want to use
    private static void handleAskSuperpower() {
        String superpower = String.valueOf(((PlayerBotSmarter) player).getSuperpower());
        playerFacade.sendGameCommandToPlayer(poker, poker.getHostName(), new GameCommand("superpower", superpower));
    }
    //When the table gives us payout at the end of a round (even 0)
    private static void handlePayout(GameCommand command) {
        System.out.println(player.getName() + " has won chips: " + command.body());
        System.out.println("I was betting" + player.getBet() + " chips");
        player.won(Integer.parseInt(command.body()));
        System.out.println("I have " + player.getChipStack() + " chips");
    }
    //When the table asks us to raise, call or fold
    private static void handleRaiseCallOrFold(GameCommand command) {
        int highestestBet = Integer.parseInt(command.body());
        int playerCommand = ((PlayerBotSmarter) player).getCommand(highestestBet - player.getBet());
        int playerBetAmount = ((PlayerBotSmarter) player).getBetAmount(highestestBet - player.getBet());
        switch (playerCommand) {
            case 1:
                player.call(highestestBet - player.getBet());
                break;
            case 2:
                player.fold();
                System.out.println("I have folded");
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
    //When the table asks us to pay the blind, we inform our control player that he has paid a blind
    private static void handlePayBlind(GameCommand command) {
        int toPay = Integer.parseInt(command.body());
        bet(toPay);
    }
    private static void bet(int howMuch) {
        player.bet(howMuch);

    }
    //When the table asks us if we want to invert a color's values
    private static void handleInvertedColor() {
        playerFacade.sendGameCommandToPlayer(poker, poker.getHostName(),
                new GameCommand("invertedColorAnswer",
                        String.valueOf(((PlayerBotSmarter) player).askForInvertedColor())));
    }
    //When the table gives us our cards, we update our player's hand
    private static void handleGiveCards(GameCommand command) {
        List<Card> cards = new ArrayList<>();
        String[] args = command.body().split(",");
        for (int i = 0; i < args.length; i++) {
            cards.add(Card.stringToCard(args[i]));
        }
        player.setHand(new PlayerHand(cards));
        System.out.println("I have those cards: " + cards);
    }
    //When the table gives us an extra card, thanks to the add superpower
    private static void handleCardAdded(GameCommand command) {
        Card card = Card.stringToCard(command.body());
        player.addCard(card);
    }
    //When the table informs us we have seen a new card from a player
    private static void handleCardSeen(GameCommand command) {
        String[] args = command.body().split(",");
        Card card = Card.stringToCard(args[0]);
        Player playerSeen = new Player(args[1], Integer.parseInt(args[2]));
        player.updateCardSeen(playerSeen, card);
    }
    //When the table informs us one of the cards of somebody has been destroyed
    private static void handleCardDestroyed(GameCommand command) {
        String[] args = command.body().split(",");
        Card card = Card.stringToCard(args[0]);
        String playerName = args[1];
        int playerChips = Integer.parseInt(args[2]);
        //if it's ours, we remove it
        if (player.getName().equals(playerName)) {
            player.remove(card);
        } else { //if it's someone else's, we remove it from the known cards, if we know that card
            for (Player player : player.getCardsKnownFromOtherPlayers().keySet()) {
                if (player.getName().equals(playerName)) {
                    player.getCardsKnownFromOtherPlayers().get(new Player(playerName, playerChips)).remove(card);
                }
            }
        }
    }
    //When the table informs us we have lost money, we update our player's chipstack
    //We lose money when spending on superpowers
    private static void handleLostMoney(GameCommand command) {
        int amount = Integer.parseInt(command.body());
        // other functions handle checks so this function should never malfunction
        player.setChipStack(player.getChipStack() - amount);
        System.out.println("I have spent " + amount + " chips on some superpower");
    }
    //When the table informs us of the dealer's hand, we update our player's dealer hand
    private static void handleUpdateDealer(GameCommand command) {
        player.setDealerHand(
                Arrays.stream(command.body().split(",")).map(Card::stringToCard).collect(Collectors.toList()));
    }

    // Our player doesn't need to know how many chips the other players have, he's
    // not smart enough to use it.
    // this function will initialize the players known to our player, so that he can
    // use some superpowers on them
    // But also so that he can associate the cards he sees to the players he sees
    // them from
    private static void handleInitializePlayersKnown(GameCommand command) {
        String[] args = command.body().split(",");
        for (int i = 0; i < args.length; i++) {
            if (!args[i].equals(player.getName()))
                // initialize players with 1 chip, to avoid any weird behaviour (which shouldn't
                // happen right now but could cause issues down the line)
                player.getCardsKnownFromOtherPlayers().put(new Player(args[i], 1), new HashSet<>());
        }
    }
    //a player has been kicked, so we remove him from our player's known players
    private static void handleKick(GameCommand command) {
        player.getCardsKnownFromOtherPlayers().remove(new Player(command.body(), 1));
    }
}