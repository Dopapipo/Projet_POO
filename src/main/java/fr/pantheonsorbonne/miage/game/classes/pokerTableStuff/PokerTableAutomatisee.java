package fr.pantheonsorbonne.miage.game.classes.pokerTableStuff;

import java.util.ArrayList;
import java.util.List;

import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.PlayerBot;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.PlayerBotSmarter;

public class PokerTableAutomatisee extends PokerTable {
	public PokerTableAutomatisee(List<Player> players) {
		super(players);
	}

	public PokerTableAutomatisee() {
		super();
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
					int answer = ((PlayerBotSmarter) player).getCommand(this.highestBet-player.getBet());
					switch (answer) {
						case 1:
							this.call(player);
							break;
						case 2:
							player.fold();
							playersInRound--;
							break;
						// if a player raises, we set him to currently raising, and all the other
						// players to not currently raising
						case 3:
							int x = ((PlayerBotSmarter) player).getBetAmount(this.highestBet-player.getBet());
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
	protected void turnCards() {
		this.giveCards();
		this.initializeBlinds();
		this.askBlindPayment();
		this.askAndSetInvertedColor();
		int playersInRound = currentlyPlaying.size();
		playersInRound = this.askForBetsWithPots(playersInRound);
		this.flop();
		playersInRound = this.askForBetsWithPots(playersInRound);
		this.turn();
		playersInRound = this.askForBetsWithPots(playersInRound);
		this.river();
		this.askForBetsWithPots(playersInRound);
		for (Player player : this.currentlyPlaying) {
			System.out.println(player.getName() + " is betting " + player.getBet());
		}
	}

	@Override
	protected int askForSuperpowerUse(Player player) {
		return ((PlayerBot) player).getSuperpower();
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
					Player otherPlayer = ((PlayerBot) player).askForPlayerToUseSuperpowerOn(this.currentlyPlaying);
					superpowerShow.useOnOther(player, otherPlayer);
					return otherPlayer;

				} catch (RuntimeException e) {
					System.out.println(e.getMessage());
				}
				break;
			case 2:
				// destroy a random card from a player
				try {
					Player otherPlayer = ((PlayerBot) player).askForPlayerToUseSuperpowerOn(this.currentlyPlaying);
					superpowerDestroy.useOnOther(player, otherPlayer);
					return otherPlayer;
				} catch (RuntimeException e) {
					System.out.println(e.getMessage());
				}
				break;
			case 3:
				// add a card to your hand (shown)
				try {
					superpowerAdd.useOnSelf(player, this.deck);
				} catch (RuntimeException e) {
					System.out.println(e.getMessage());
				}
				break;
			case 4:
				// add a hidden card to your hand
				try {

					this.superpowerAddHidden.useOnSelf(player, this.deck);
				} catch (RuntimeException e) {
					System.out.println(e.getMessage());
				}
				break;
		}
		return null;
	}

	@Override
	protected void askAndSetInvertedColor() {
		Player playerToAsk = this.bigBlind.getPlayer();
		int answer = ((PlayerBot) playerToAsk).askForInvertedColor();
		this.setInvertedColor(answer);
	}

	@Override
	protected Player useSuperpower(Player player, String name) {
		switch (name) {
			case "add":
				return this.useSuperpower(player, 3);
			case "addHidden":
				return this.useSuperpower(player, 4);
			case "destroy":
				return this.useSuperpower(player, 2);
			case "show":
				return this.useSuperpower(player, 1);
		}
		return null;
	}

}
