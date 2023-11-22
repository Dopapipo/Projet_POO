package fr.pantheonsorbonne.miage.game.classes.pokerTableStuff;

import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.pantheonsorbonne.miage.game.classes.cards.Card;
import fr.pantheonsorbonne.miage.game.classes.cards.CardColor;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.PlayerHand;
import fr.pantheonsorbonne.miage.game.logic.WinConditionLogic;

public class PokerTableSimulations extends PokerTableAutomatisee {
    private Player simulatedPlayer;
    private int nbSimulations;
    private List<Card> initialPlayerHand;
    private List<Card> initialDealerHand;
    private Map<Player,Set<Card>> initialCardsKnownFromOtherPlayers;
    private List<Player> players; //to avoid spaghetti code, we won't use this.currentlyPlaying 
    //basically, some players might be set as not playing if we use it, because they might be all in
    //at the time we pass them to the simulation, so their chipstack will be 0, and they'll be initialized
    //as not playing.

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
    private void initialize(List<Card> playerHand, List<Card> dealerHand, Map<Player,Set<Card>> cardsKnownFromOtherPlayers,CardColor invertedColor) {
        this.simulatedPlayer.setHand(new PlayerHand(playerHand));
        for (Card card:playerHand) {
            this.deck.remove(card);
        }
        for (Card card:dealerHand) {
            this.deck.remove(card);
        }
        for(Player player : cardsKnownFromOtherPlayers.keySet()) {
            for(Card card : cardsKnownFromOtherPlayers.get(player)) {
                this.deck.remove(card);
                player.getPlayerHand().add(card);
            }
        }
    }
    @Override
    protected void giveCards() {
        for (Player player : this.playerList) {
            if (!player.equals(this.simulatedPlayer)) {
                while (player.getPlayerHand().size()<2) {
                    player.getPlayerHand().add(this.deck.draw());
                }
            }
        }
    }
    
    private double runSimulations() {
        double sum = 0;
        for (int i = 0;i<this.nbSimulations;i++) {
            initialize(this.initialPlayerHand, this.initialDealerHand, this.initialCardsKnownFromOtherPlayers, this.invertedColor);
            this.giveCards();
            while(this.dealer.getDealerHand().size()<5) {
                this.dealer.getDealerHand().add(this.deck.draw());
            }
            List<Player> winners = this.checkWhoWins(players);
            if (winners.contains(this.simulatedPlayer)) {
                sum+=1/winners.size();
            }
            
        }
        return sum/this.nbSimulations;
    }
    public double getWinRate() {
        return this.runSimulations();
    }
}
