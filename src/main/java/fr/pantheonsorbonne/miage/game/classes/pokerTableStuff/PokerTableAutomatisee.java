package fr.pantheonsorbonne.miage.game.classes.pokerTableStuff;

import java.util.List;

import fr.pantheonsorbonne.miage.game.classes.cards.Exceptions.AlreadyUsedException;
import fr.pantheonsorbonne.miage.game.classes.cards.Exceptions.NotEnoughChipsException;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.PlayerBot;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.PlayerBotSmarter;
import fr.pantheonsorbonne.miage.game.classes.superpowers.SuperpowerChoice;

public class PokerTableAutomatisee extends PokerTable {
	public PokerTableAutomatisee(List<Player> players) {
		super(players);
	}

	public PokerTableAutomatisee() {
		super();
	}

	@Override
	protected int askForBets(int playersInRound) {
		// loopAgain will be true if someone raises within a round, and it's not the
		// first player
		// (otherwise all the others can just call and it's pointless to loop again)
		boolean loopAgain = true;
		// if only one player can still bet, it's pointless to keep looping
		while (playersInRound > 1 && loopAgain) {
			int playersToAsk = this.playerQueue.size();
			loopAgain = false;
			// ask every player in the queue for bet...
			for (int i = 0; i < playersToAsk; i++) {
				Player player = this.playerQueue.poll();
				// ask for superpower use
				this.askAndUseSuperpower(player);
				// if player hasn't folded, isn't all in
				// ask him for bet
				if (player.hasNotFolded() && !player.isAllIn()) {
					//get their answer (have to check if he's smart or dumb for it to work properly)
					int answer = player instanceof PlayerBotSmarter
							? ((PlayerBotSmarter) player).getCommand(this.highestBet - player.getBet())
							: ((PlayerBot) player).getCommand(this.highestBet - player.getBet());
					switch (answer) {
						case 1:
							this.call(player);
							this.playerQueue.add(player);
							break;
						case 2:
							player.fold();
							playersInRound--;
							break;
						case 3:
							int x = player instanceof PlayerBotSmarter
									? ((PlayerBotSmarter) player).getBetAmount(this.highestBet - player.getBet())
									: ((PlayerBot) player).getBetAmount(this.highestBet - player.getBet());
							;
							this.raise(player, x);
							this.playerQueue.add(player);
							if (x > 0 && i > 0)
								loopAgain = true;
							break;
						default:
							break;
					}
				}
				if (player.isAllIn()) {
					playersInRound--;
				}
				//find the highest bet at the end of a player's answer so that the following
				//players can know what to do
				this.findHighestBet(); 
			}
		}
		// reset player raise state for next dealer card
		this.resetPlayersRaise();
		return playersInRound;
	}
	/*
	 * This method handles everything that happens from when the dealer gives cards to players
	 * to the last bet of the round. 
	 */
	@Override
	protected void turnCards() {
		this.giveCards();
		this.initializeBlinds();
		this.askBlindPayment();
		this.askAndSetInvertedColor();
		int playersInRound = currentlyPlaying.size();
		playersInRound = this.askForBets(playersInRound);
		this.flop();
		playersInRound = this.askForBets(playersInRound);
		this.turn();
		playersInRound = this.askForBets(playersInRound);
		this.river();
		this.askForBets(playersInRound);
	}

	@Override
	protected SuperpowerChoice askForSuperpowerUse(Player player) {
		return (player instanceof PlayerBot?((PlayerBot) player).getSuperpower():((PlayerBotSmarter) player).getSuperpower());
	}

	@Override
	protected Player useSuperpower(Player player, SuperpowerChoice answer) {
		if (answer == SuperpowerChoice.NONE) {
			return null;
		}
		switch (answer) {
			case SHOW:
				// see a random card from a player

				try {
					Player otherPlayer = ((PlayerBot) player).askForPlayerToUseSuperpowerOn(this.currentlyPlaying);
					superpowerShow.useOnOther(player, otherPlayer);
					return otherPlayer;

				} catch (NotEnoughChipsException | AlreadyUsedException e) {
					 System.out.println(e.getMessage());
				}
				break;
			case DESTROY:
				// destroy a random card from a player
				try {
					Player otherPlayer = ((PlayerBot) player).askForPlayerToUseSuperpowerOn(this.currentlyPlaying);
					superpowerDestroy.useOnOther(player, otherPlayer);
					return otherPlayer;
				} catch (NotEnoughChipsException | AlreadyUsedException e) {
					 System.out.println(e.getMessage());
				}
				break;
			case ADD:
				// add a card to your hand (shown)
				try {
					superpowerAdd.useOnSelf(player, this.deck);
					this.updateShownCards(); // update shown cards for everyone
				} catch (NotEnoughChipsException | AlreadyUsedException e) {
					 System.out.println(e.getMessage());
				}
				break;
			case ADD_HIDDEN:
				// add a hidden card to your hand
				try {

					this.superpowerAddHidden.useOnSelf(player, this.deck);
				} catch (NotEnoughChipsException | AlreadyUsedException e) {
					 System.out.println(e.getMessage());
				}
				break;
			default:
				return null;
		}
		return null;
	}

	//PlayerBotSmarter and PlayerBot invert colors in the same way, so no need to check for instanceof
	@Override
	protected void askAndSetInvertedColor() {
		Player playerToAsk = this.bigBlind.getPlayer();
		int answer = ((PlayerBot) playerToAsk).askForInvertedColor();
		this.setInvertedColor(answer);
	}
	/*
	 * Uses a superpower from its name
	 */
	@Override
	protected Player useSuperpower(Player player, String name) {
		switch (name) {
			case "add":
				return this.useSuperpower(player, SuperpowerChoice.ADD);
			case "addHidden":
				return this.useSuperpower(player, SuperpowerChoice.ADD_HIDDEN);
			case "destroy":
				return this.useSuperpower(player, SuperpowerChoice.DESTROY);
			case "show":
				return this.useSuperpower(player, SuperpowerChoice.SHOW);
			default:
				return null;
		}
	}

}
