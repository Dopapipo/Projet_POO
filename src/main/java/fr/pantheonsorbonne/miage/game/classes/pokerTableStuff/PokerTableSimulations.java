package fr.pantheonsorbonne.miage.game.classes.pokerTableStuff;

import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.pantheonsorbonne.miage.game.classes.cards.Card;
import fr.pantheonsorbonne.miage.game.classes.cards.CardColor;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;
/*
 * This class is used by bots to run simulations. They give it initial conditions,
 * such as the list of playing players, the cards they know from them,
 * the dealer's cards, their own cards, the inverted color. They choose the number
 * of simulations to run, and then the table runs them from the initial conditions, while
 * giving random cards to complete a round. It then returns the winrate of the player,
 * i.e. the number of wins divided by the number of simulations, with a small tweak if
 * there's draws.
 */
public class PokerTableSimulations extends PokerTableAutomatisee {
    private Player simulatedPlayer;
    private int nbSimulations;
    private List<Card> initialPlayerHand;
    private List<Card> initialDealerHand;
    private Map<Player,Set<Card>> initialCardsKnownFromOtherPlayers;
    private List<Player> players; //players that are playing the game

    public PokerTableSimulations(List<Card> playerHand, List<Card> dealerHand, Player simulatedPlayer,List<Player> players, Map<Player,Set<Card>> cardsKnownFromOtherPlayers,CardColor invertedColor,int nbSimulations) {
        super(players);
        this.nbSimulations = nbSimulations;
        this.players=players;
        this.simulatedPlayer=simulatedPlayer;
        this.initialCardsKnownFromOtherPlayers = cardsKnownFromOtherPlayers;
        this.initialDealerHand = dealerHand;
        this.initialPlayerHand = playerHand;
        this.invertedColor=invertedColor;
    }
    /*
     * Initializes the table to the initial conditions
     */
    private void initialize() {
        for (Card card:this.initialPlayerHand) {
            this.deck.remove(card); //remove the cards that the player knows from the deck
        }
        for (Card card:this.initialDealerHand) {
            this.deck.remove(card); //remove the cards that the player knows from the deck
        }
        for(Map.Entry<Player,Set<Card>> entry: this.initialCardsKnownFromOtherPlayers.entrySet()) {
            //initialize the hand because it will be null (our player doesn't see PlayerHand objects)
            entry.getKey().initializePlayerHand(); 
            for(Card card : entry.getValue()) {
                this.deck.remove(card);
                entry.getKey().addCard(card);
            }
        }
        this.currentlyPlaying=this.players;
    }
    @Override
    protected void giveCards() {
        for (Player player : this.currentlyPlaying) {
            if (!player.equals(this.simulatedPlayer)) {
                while (player.handSize()<2) {
                    player.addCard(this.deck.draw());
                }
            }
            else {
                for (Card card : this.initialPlayerHand) {
                    player.addCard(card);
                }
            }
        }
        
    }
    private void reset() {
        this.deck.resetDeck();
        for (Player player : this.players) {
            player.resetHand();
        }
    }
    private double runSimulations() {
        double sum = 0;
        for (int i = 0;i<this.nbSimulations;i++) {
            initialize();
            this.giveCards();
            while(this.dealer.getDealerHand().size()<5) {
                this.dealer.getDealerHand().add(this.deck.draw());
            }
            List<Player> winners = this.checkWhoWins(this.currentlyPlaying);
            if (winners.contains(this.simulatedPlayer)) {
                sum+=((double)1)/winners.size();
            }
            this.reset();
            
        }
        giveCards();
        return sum/this.nbSimulations;
    }
    public double getWinRate() {
        return this.runSimulations();
    }
}
