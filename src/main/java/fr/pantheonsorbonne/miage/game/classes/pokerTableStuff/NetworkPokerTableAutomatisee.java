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
		host.play();
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
		this.setInvertedColor(Integer.valueOf(answer));
	}

	@Override
	protected void giveCards() {
		for (Player player : this.getPlayers()) {
			Card c1 = this.getDeck().draw();
			Card c2 = this.getDeck().draw();
			player.setHand(new PlayerHand(Arrays.asList(c1, c2)));
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
		boolean everyoneCalled = false;
		// Implementation of support for constant raising : while
		// not everyone has called/folded (i.e. there's still a player raising)
		// We ask every other player for a call/fold/raise
		List<Boolean> playersCalled = new ArrayList<>();
		while (!everyoneCalled) {
			playersCalled.clear();
			for (Player player : this.currentlyPlaying) {
				// ask for superpower use
				this.askAndUseSuperpower(player);
				// if there's more than one player to ask, player hasn't folded, isn't all in
				// and isn't the one currently raising,
				// ask him for bet
				if (player.hasNotFolded() && !player.isAllIn() && !player.isCurrentlyRaising()) {
					GameCommand playerAnswer = this.askRaiseCallOrFold(player);
					switch (Integer.valueOf(playerAnswer.name())) {
						case 1:
							this.call(player);
							break;
						case 2:
							this.fold(player);
							playersInRound--;
							break;
						// if a player raises, we set him to currently raising, and all the other
						// players to not currently raising
						case 3:
							int x = Integer.valueOf(playerAnswer.body());
							this.raise(player, x);
							break;
					}
				}
				if (player.isAllIn()) {
					playersInRound--;
				}
				this.findHighestBet();
			}
			/**
			 * If everyone has called, we can stop asking for bets
			 * If someone has raised, we need to ask everyone again
			 * If everyone called, playersCalled is empty.
			 */
			everyoneCalled = true;
			for (Boolean playerCalled : playersCalled) {
				if (!playerCalled) {
					everyoneCalled = false;
					break;
				}
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
		return Integer.valueOf(received.body());
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
							new GameCommand("lostMoney", String.valueOf(SuperpowerShow.getCost())));
					hostFacade.sendGameCommandToPlayer(poker, player.getName(),
							new GameCommand("cardSeen", Card.cardToString(card) + "," + otherPlayer.getName()));
					return otherPlayer;

				} catch (RuntimeException e) {
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
							new GameCommand("lostMoney", String.valueOf(SuperpowerDestroy.getCost())));
					hostFacade.sendGameCommandToAll(poker, new GameCommand("cardDestroyed",
							Card.cardToString(card) + "," + otherPlayer.getName() + "," + otherPlayer.getChipStack()));
					return otherPlayer;
				} catch (RuntimeException e) {
					System.out.println(e.getMessage());
				}
				break;
			case 3:
				// add a card to your hand (shown)
				try {
					Card card = superpowerAdd.useOnSelf(player, this.deck);
					hostFacade.sendGameCommandToPlayer(poker, player.getName(),
							new GameCommand("lostMoney", String.valueOf(SuperpowerAdd.getCost())));
					hostFacade.sendGameCommandToPlayer(poker, player.getName(),
							new GameCommand("cardAdded", Card.cardToString(card)));
					hostFacade.sendGameCommandToAll(poker, new GameCommand("cardSeen",
							Card.cardToString(card) + "," + player.getName() + "," + player.getChipStack()));
				} catch (RuntimeException e) {
					System.out.println(e.getMessage());
				}
				break;
			case 4:
				// add a hidden card to your hand
				try {

					Card card = this.superpowerAddHidden.useOnSelf(player, this.deck);
					hostFacade.sendGameCommandToPlayer(poker, player.getName(),
							new GameCommand("lostMoney", String.valueOf(SuperpowerAddHidden.getCost())));
					hostFacade.sendGameCommandToPlayer(poker, player.getName(),
							new GameCommand("cardAdded", Card.cardToString(card)));
				} catch (RuntimeException e) {
					System.out.println(e.getMessage());
				}
				break;
		}
		return null;
	}

	private GameCommand askRaiseCallOrFold(Player player) {
		hostFacade.sendGameCommandToPlayer(poker, player.getName(),
				new GameCommand("raiseCallOrFold", String.valueOf(this.getHighestBet())));
		GameCommand received = hostFacade.receiveGameCommand(poker);
		return received;
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
