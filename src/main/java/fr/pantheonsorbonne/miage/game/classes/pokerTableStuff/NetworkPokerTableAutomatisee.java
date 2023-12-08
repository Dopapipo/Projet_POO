package fr.pantheonsorbonne.miage.game.classes.pokerTableStuff;

import java.util.ArrayList;
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
import fr.pantheonsorbonne.miage.game.classes.superpowers.SuperpowerChoice;
import fr.pantheonsorbonne.miage.game.classes.superpowers.SuperpowerDestroy;
import fr.pantheonsorbonne.miage.game.classes.superpowers.SuperpowerShow;
/*
 * This class is a poker table that can be played on a network.
 * For logic, this table is the same as PokerTableAutomatisee. It runs on local players.
 * However, it lets Network Players decide what to do. The difficulty was to make sure
 * the information that our table has on the players and the information the network
 * players have is synchronised. We have to let the players know when information updates,
 * by sending them commands. 
 */
public class NetworkPokerTableAutomatisee extends PokerTableAutomatisee {
	private static final int PLAYER_COUNT = 5; //apparently the host counts as a player
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
		//start the game and print the winner (play() returns the winner)
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
			hostFacade.sendGameCommandToAll(poker, new GameCommand("kick", player));
		}
		return removed;
	}

	@Override
	protected int askForBets(int playersInRound) {
		boolean loopAgain = true;
		//ask for bets while someone over-raised like in real poker
		while (playersInRound>1&&loopAgain) {
			int playersToAsk = this.playerQueue.size();
			loopAgain=false; //we loop again if someone raises
			for (int i = 0;i<playersToAsk;i++) { //loop on the players to ask
				Player player = this.playerQueue.poll();
				this.askAndUseSuperpower(player);
				// if there's more than one player to ask, player hasn't folded, isn't all in
				// and isn't the one currently raising,
				// ask him for bet
				if (!player.isCurrentlyRaising()) {
					GameCommand playerAnswer = this.askRaiseCallOrFold(player);
					switch (Integer.parseInt(playerAnswer.name())) {
						//1 is call, 2 is fold, 3 is raise
						case 1:
							this.call(player);
							this.playerQueue.add(player);
							break;
						case 2:
							this.fold(player);
							playersInRound--;
							break;
						case 3:
							int x = Integer.parseInt(playerAnswer.body());
							this.raise(player, x);
							if (!player.isAllIn()) this.playerQueue.add(player);
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
	protected SuperpowerChoice askForSuperpowerUse(Player player) {
		hostFacade.sendGameCommandToPlayer(poker, player.getName(), new GameCommand("askSuperpower", ""));
		GameCommand received = hostFacade.receiveGameCommand(poker);
		return SuperpowerChoice.fromString(received.body());
	}

	@Override
	protected Player useSuperpower(Player player, SuperpowerChoice answer) {

		switch (answer) {
			case NONE:	
				return null;
			case SHOW:
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
			case DESTROY:
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
			case ADD:
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
			case ADD_HIDDEN:
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
					new GameCommand("payout", String.valueOf(value/winners.size())));
		}
		for (Player player : this.getPlayers()) {
			if (!winners.contains(player)) {
				this.won(player,0);
				hostFacade.sendGameCommandToPlayer(poker, player.getName(),
						new GameCommand("payout", String.valueOf(0)));
			}
		}
	}
	@Override
	protected void updateDealerHandForPlayers() {
		super.updateDealerHandForPlayers();
		hostFacade.sendGameCommandToAll(poker, new GameCommand("updateDealer", this.getDealer().getDealerHand().stream().map(Card::cardToString).collect(Collectors.joining(","))));

	}
	@Override
	protected void initializeShownCards() {
		super.initializeShownCards(); //initialize the player/cards known structures of our players from our table
		//then let the network players initialize theirs
		hostFacade.sendGameCommandToAll(poker, new GameCommand("namesOfPlayers", this.getPlayers().stream().map(Player::getName).collect(Collectors.joining(","))));
	}
}
