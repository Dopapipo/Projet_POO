package fr.pantheonsorbonne.miage.game.classes.pokerTableStuff;

import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.pantheonsorbonne.miage.game.classes.cards.Card;
import fr.pantheonsorbonne.miage.game.classes.cards.CardColor;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;

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
    private void initialize() {
        for (Card card:this.initialPlayerHand) {
            this.deck.remove(card);
        }
        for (Card card:this.initialDealerHand) {
            this.deck.remove(card);
        }
        for(Map.Entry<Player,Set<Card>> entry: this.initialCardsKnownFromOtherPlayers.entrySet()) {
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
                while (player.getPlayerHand().size()<2) {
                    player.addCard(this.deck.draw());
                }
            }
            else {
                for (Card card : this.initialPlayerHand) {
                    player.getPlayerHand().add(card);
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
