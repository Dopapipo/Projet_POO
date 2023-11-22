package fr.pantheonsorbonne.miage.game.classes.superpowers;

import fr.pantheonsorbonne.miage.game.classes.cards.Card;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;
import fr.pantheonsorbonne.miage.game.classes.pokerTableStuff.Deck;

public class SuperpowerAddHidden extends SuperpowerSelf{
    private static final int COST = 125;
    private static final String NAME = "Add hidden";
    private static final String DESCRIPTION = "Add a random card to your hand without showing it";
    public SuperpowerAddHidden() {
        super(COST, NAME, DESCRIPTION);}

    @Override
    protected Card use(Player player,Deck deck) {
        player.addCard(deck.draw());
        return null;
    }
    public static int getCost() {
        return COST;
    }
}
