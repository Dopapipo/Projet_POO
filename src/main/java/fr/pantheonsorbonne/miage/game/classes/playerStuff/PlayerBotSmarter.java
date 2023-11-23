package fr.pantheonsorbonne.miage.game.classes.playerStuff;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fr.pantheonsorbonne.miage.game.classes.cards.Card;
import fr.pantheonsorbonne.miage.game.classes.pokerTableStuff.PokerTableSimulations;

/*
 * This playerbot always tries to use the add superpower as it's the most overpowered, so he's 
 * smarter than the rest: he figured out the META!
 * If he's giga rich, he will first use add superpower, then remove superpower!
 * He runs simulations in order to figure out his winrate and will take decisions based on that.
 * If he's got a really strong hand, he will all in !
 * If he's got a strong hand, he will raise !
 * He will call if it's not too expensive ! 
 * He will check if he's got a weak hand & no one raised, otherwise he will fold !
 */
public class PlayerBotSmarter extends PlayerBot {
    private int superpower = 3;
    private boolean alreadyRaised = false;
    private double winrate;
    public PlayerBotSmarter(String name) {
        super(name);
    }

    public PlayerBotSmarter(String name, int chipStack) {
        super(name, chipStack);
    }
    @Override
    public int getCommand(int amountToCall) {
        this.winrate = this.findWinrate();
        if (winrate>0.9||(!alreadyRaised && winrate>0.4 && amountToCall<this.getChipStack()*0.2)){
            alreadyRaised = true;
            return 3;
        }
        if (amountToCall==0) {
            return 1;
        }
        if ((this.getChipStack()*0.4<amountToCall&&winrate>0.4) || (this.getChipStack()*0.1<amountToCall&&winrate>0.1) || (this.getChipStack()*0.9<amountToCall&&winrate>0.8)) {
            return 1;
        }
        return 2;
    }

    @Override
    public int getBetAmount( int amountToCall) {
        if (this.winrate > 0.9) {
            return this.getChipStack();
        }
        if (winrate>0.7) {
            return (int) ((this.getChipStack()-amountToCall)*0.3);
        }
        return (int) ((this.getChipStack()-amountToCall)*0.1);
    }

    @Override
    public int getSuperpower() {
        if (this.getChipStack()<300) {
            return 0;
        }
        if (this.getChipStack()>600 && this.superpower!=3) {
            return 2;
        }
        int toReturn = this.superpower;
        this.superpower = 1000; // so that we don't use it again for this round
        return toReturn;
    }

    @Override
    public void won(int winnings) {
        super.won(winnings);
        this.superpower = 3;// reset superpower bc round ended
    }
    public void setWinrate(double winrate) {
        this.winrate = winrate;
    }
    private double findWinrate() {
        List<Card> thisHand = this.getPlayerHand().getHand().stream().collect(Collectors.toList());
        List<Card> thisDealer = this.getDealerHand().stream().collect(Collectors.toList());
        PokerTableSimulations simulation = new PokerTableSimulations(thisHand, thisDealer,this.copy(), this.getPlayers(), this.getCardsKnownFromOtherPlayers(),this.getInvertedColor(),100);
        return simulation.getWinRate();
    }
    private List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        for (Player player : this.getCardsKnownFromOtherPlayers().keySet()) {
            players.add(player.copy());
        }
        players.add(this.copy());
        return players;
    }


    
}
