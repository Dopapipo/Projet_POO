package fr.pantheonsorbonne.miage.game.classes.pokerTableStuff;

import java.util.ArrayList;
import java.util.List;

import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.PlayerBot;

public class PokerTableAutomatisee extends PokerTable {
	public PokerTableAutomatisee(List<Player> players) {
		super(players);
	}
	public PokerTableAutomatisee() {
		super();
	}
	public boolean gameContinues() {
		return this.howManyAreStillPlaying() > 1;
	}
	@Override
	public int askForBetsWithPots(int playersInRound) {
		boolean everyoneCalled = false;
		// Implementation of support for constant raising : while
		// not everyone has called/folded (i.e. there's still a player raising)
		// We ask every other player for a call/fold/raise
		List<Boolean> playersCalled = new ArrayList<>();
		while (!everyoneCalled) {
			playersCalled.clear();
			for (Player player : this.currentlyPlaying) {
				// if there's more than one player to ask, player hasn't folded, isn't all in
				// and isn't the one currently raising,
				// ask him for bet
				if (player.hasNotFolded() && !player.isAllIn() && !player.isCurrentlyRaising()) {
					int answer = ((PlayerBot) player).getCommand();
					switch (answer) {
						case 1:
							player.call(this.highestBet - player.getBet());
							player.setCurrentlyRaising(false);
							break;
						case 2:
							player.fold();
							player.setCurrentlyRaising(false);
							playersInRound--;
							break;
						// if a player raises, we set him to currently raising, and all the other
						// players to not currently raising
						case 3:
							int x = ((PlayerBot)player).getBetAmount();
							if (x > 0) {
								player.bet(highestBet - player.getBet() + x);
								if (player.isAllIn()) {
									this.makePotForAllInPlayer(player);
								}
								for (Player aPlayer : this.currentlyPlaying) {
									aPlayer.setCurrentlyRaising(false);
								}
								player.setCurrentlyRaising((true));
								playersCalled.add(false);
							} else {
								player.call(this.highestBet - player.getBet());
								player.setCurrentlyRaising(false);
							}
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
	public void turnCards() {
		this.giveCards();
		this.initializeBlinds();
		this.askBlindPayment();
		int playersInRound = currentlyPlaying.size();
		playersInRound = this.askForBetsWithPots(playersInRound);
		dealer.flop();
		playersInRound = this.askForBetsWithPots(playersInRound);
		dealer.turn();
		playersInRound = this.askForBetsWithPots(playersInRound);
		dealer.river();
		this.askForBetsWithPots(playersInRound);
		System.out.println("highest bet : " + this.highestBet);
		for (Player player : this.currentlyPlaying) {
			System.out.println(player.getName() + " is betting " + player.getBet());
		}
	}

}
