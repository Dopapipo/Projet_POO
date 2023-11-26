package fr.pantheonsorbonne.miage.game.classes.pokerTableStuff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import fr.pantheonsorbonne.miage.Facade;
import fr.pantheonsorbonne.miage.HostFacade;
import fr.pantheonsorbonne.miage.model.Game;
import fr.pantheonsorbonne.miage.model.GameCommand;
import fr.pantheonsorbonne.miage.game.classes.cards.Card;
import fr.pantheonsorbonne.miage.game.classes.cards.Exceptions.AlreadyUsedException;
import fr.pantheonsorbonne.miage.game.classes.cards.Exceptions.NotEnoughChipsException;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.*;
import fr.pantheonsorbonne.miage.game.classes.superpowers.SuperpowerAdd;
import fr.pantheonsorbonne.miage.game.classes.superpowers.SuperpowerAddHidden;
import fr.pantheonsorbonne.miage.game.classes.superpowers.SuperpowerDestroy;
import fr.pantheonsorbonne.miage.game.classes.superpowers.SuperpowerShow;

public class NetworkPokerTableAutomatisee extends PokerTableAutomatisee {
	private static final int PLAYER_COUNT = 5;
	private static final int DEFAULT_CHIPS = 300;
	private final HostFacade hostFacade;
	private final Game poker;
	private static final String CARD_SEEN = "cardSeen";
	private static final String LOST_MONEY = "lostMoney";


	public NetworkPokerTableAutomatisee(HostFacade hostFacade, Set<String> players,
			fr.pantheonsorbonne.miage.model.Game poker) {
		super();
		this.hostFacade = hostFacade;
		this.poker = poker;
		List<Player> playersList = players.stream().map(p -> new Player(p, DEFAULT_CHIPS)).collect(Collectors.toList());
		this.playerList = playersList;
		for (Player player : this.playerList) {
			if (player.getChipStack() > 0) {
				this.currentlyPlaying.add(player);
			}
		}
	}

	public static void main(String[] args) {
		// create the host facade
		HostFacade hostFacade = Facade.getFacade();
		hostFacade.waitReady();

		// set the name of the player
		hostFacade.createNewPlayer("Host");

		// create a new game of poker
		fr.pantheonsorbonne.miage.model.Game poker = hostFacade.createNewGame("Poker");

		// wait for enough players to join
		hostFacade.waitForExtraPlayerCount(PLAYER_COUNT);

		NetworkPokerTableAutomatisee host = new NetworkPokerTableAutomatisee(hostFacade, poker.getPlayers(), poker);
		System.out.println(host.play().getName() +" won the game!");
		System.exit(0);

	}

	@Override
	protected void askBlindPayment() {
		super.askBlindPayment();
		hostFacade.sendGameCommandToPlayer(poker, this.getBigBlind().getPlayer().getName(),
				new GameCommand("payBlind", String.valueOf(this.getBigBlind().getValue())));
		hostFacade.sendGameCommandToPlayer(poker, this.getSmallBlind().getPlayer().getName(),
				new GameCommand("payBlind", String.valueOf(this.getSmallBlind().getValue())));
	}

	@Override
	protected void askAndSetInvertedColor() {
		hostFacade.sendGameCommandToPlayer(poker, this.getBigBlind().getPlayer().getName(),
				new GameCommand("invertedColor", String.valueOf(this.getInvertedColor())));
		String answer = hostFacade.receiveGameCommand(poker).body();
		this.setInvertedColor(Integer.parseInt(answer));
	}

	@Override
	protected void giveCards() {
		for (Player player : this.getPlayers()) {
			Card c1 = this.getDeck().draw();
			Card c2 = this.getDeck().draw();
			List<Card> cards = new ArrayList<>(); //don't use Arrays.asList() because it's immutable
			cards.add(c1);
			cards.add(c2);
			player.setHand(new PlayerHand(cards));
			String card1 = Card.cardToString(c1);
			String card2 = Card.cardToString(c2);
			hostFacade.sendGameCommandToPlayer(poker, player.getName(),
					new GameCommand("giveCards", card1 + "," + card2));
		}
	}

	@Override
	protected List<String> kickBrokePlayers() {
		List<String> removed = super.kickBrokePlayers();
		for (String player : removed) {
			hostFacade.sendGameCommandToPlayer(poker, player, new GameCommand("kick", ""));
		}
		return removed;
	}

	@Override
	protected int askForBetsWithPots(int playersInRound) {
		boolean loopAgain = true;
		//ask for bets while someone over-raised like in real poker
		while (playersInRound>1&&loopAgain) {
			int playersToAsk = this.playerQueue.size();
			loopAgain=false;
			for (int i = 0;i<playersToAsk;i++) { //loop on the players to ask
				Player player = this.playerQueue.poll();
				this.askAndUseSuperpower(player);
				// if there's more than one player to ask, player hasn't folded, isn't all in
				// and isn't the one currently raising,
				// ask him for bet
				if (!player.isCurrentlyRaising()) {
					GameCommand playerAnswer = this.askRaiseCallOrFold(player);
					switch (Integer.parseInt(playerAnswer.name())) {
						case 1:
							this.call(player);
							this.playerQueue.add(player);
							break;
						case 2:
							this.fold(player);
							playersInRound--;
							break;
						// if a player raises, we set him to currently raising, and all the other
						// players to not currently raising
						case 3:
							int x = Integer.parseInt(playerAnswer.body());
							this.raise(player, x);
							if (!player.isAllIn())this.playerQueue.add(player);
							if (x>0) loopAgain=true;
							break;
						default:
							break;
					}
				}
				if (player.isAllIn()) {
					playersInRound--;
				}
				this.findHighestBet();
			}
		}
		// reset player raise state for next dealer card
		this.resetPlayersRaise();
		return playersInRound;
	}

	@Override
	protected int askForSuperpowerUse(Player player) {
		hostFacade.sendGameCommandToPlayer(poker, player.getName(), new GameCommand("askSuperpower", ""));
		GameCommand received = hostFacade.receiveGameCommand(poker);
		return Integer.parseInt(received.body());
	}

	@Override
	protected Player useSuperpower(Player player, int answer) {
		if (answer == 0) {
			return null;
		}
		switch (answer) {
			case 1:
				// see a random card from a player

				try {
					hostFacade.sendGameCommandToPlayer(poker, player.getName(),
							new GameCommand("askSuperpowerTarget", ""));
					GameCommand received = hostFacade.receiveGameCommand(poker);
					Player otherPlayer = this.strToPlayer(received.body());
					Card card = this.superpowerShow.useOnOther(player, otherPlayer);
					hostFacade.sendGameCommandToPlayer(poker, player.getName(),
							new GameCommand(LOST_MONEY, String.valueOf(SuperpowerShow.getCost())));
					hostFacade.sendGameCommandToPlayer(poker, player.getName(),
							new GameCommand(CARD_SEEN, Card.cardToString(card) + "," + otherPlayer.getName()));
					hostFacade.sendGameCommandToPlayer(poker, otherPlayer.getName(), new GameCommand(CARD_SEEN,
							Card.cardToString(card)));
					return otherPlayer;

				} catch (NotEnoughChipsException |AlreadyUsedException e) {
					System.out.println(e.getMessage());
				}
				break;
			case 2:
				// destroy a random card from a player
				try {
					hostFacade.sendGameCommandToPlayer(poker, player.getName(),
							new GameCommand("askSuperpowerTarget", ""));
					GameCommand received = hostFacade.receiveGameCommand(poker);
					Player otherPlayer = this.strToPlayer(received.body());
					Card card = this.superpowerDestroy.useOnOther(player, otherPlayer); // will throw an exception if
																						// can't use
					hostFacade.sendGameCommandToPlayer(poker, player.getName(),
							new GameCommand(LOST_MONEY, String.valueOf(SuperpowerDestroy.getCost())));
					hostFacade.sendGameCommandToAll(poker, new GameCommand("cardDestroyed",
							Card.cardToString(card) + "," + otherPlayer.getName() ));
					return otherPlayer;
				} catch (NotEnoughChipsException |AlreadyUsedException e) {
					System.out.println(e.getMessage());
				}
				break;
			case 3:
				// add a card to your hand (shown)
				try {
					Card card = superpowerAdd.useOnSelf(player, this.deck);
					hostFacade.sendGameCommandToPlayer(poker, player.getName(),
							new GameCommand(LOST_MONEY, String.valueOf(SuperpowerAdd.getCost())));
					hostFacade.sendGameCommandToPlayer(poker, player.getName(),
							new GameCommand("cardAdded", Card.cardToString(card)));
					hostFacade.sendGameCommandToAll(poker, new GameCommand(CARD_SEEN,
							Card.cardToString(card) + "," + player.getName() + "," + player.getChipStack()));
				} catch (NotEnoughChipsException |AlreadyUsedException e) {
					System.out.println(e.getMessage());
				}
				break;
			case 4:
				// add a hidden card to your hand
				try {

					Card card = this.superpowerAddHidden.useOnSelf(player, this.deck);
					hostFacade.sendGameCommandToPlayer(poker, player.getName(),
							new GameCommand(LOST_MONEY, String.valueOf(SuperpowerAddHidden.getCost())));
					hostFacade.sendGameCommandToPlayer(poker, player.getName(),
							new GameCommand("cardAdded", Card.cardToString(card)));
				} catch (NotEnoughChipsException |AlreadyUsedException e) {
					System.out.println(e.getMessage());
				}
				break;
			default :
				break;
		}
		return null;
	}

	private GameCommand askRaiseCallOrFold(Player player) {
		hostFacade.sendGameCommandToPlayer(poker, player.getName(),
				new GameCommand("raiseCallOrFold", String.valueOf(this.getHighestBet())));
		return hostFacade.receiveGameCommand(poker);
	}

	@Override
	protected void distributeGains(List<Player> winners, int value) {
		for (Player player : winners) {
			this.won(player, value / winners.size());
			hostFacade.sendGameCommandToPlayer(poker, player.getName(),
					new GameCommand("payout", String.valueOf(value)));
		}
	}
	@Override
	protected void flop() {
		super.flop();
		hostFacade.sendGameCommandToAll(poker, new GameCommand("updateDealer", this.getDealer().getDealerHand().stream().map(Card::cardToString).collect(Collectors.joining(","))));
	}
}
