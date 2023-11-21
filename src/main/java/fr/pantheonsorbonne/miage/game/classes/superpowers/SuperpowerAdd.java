package fr.pantheonsorbonne.miage.game.classes.superpowers;

import fr.pantheonsorbonne.miage.game.classes.pokerTableStuff.Deck;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;
import fr.pantheonsorbonne.miage.game.classes.cards.Card;

public class SuperpowerAdd extends SuperpowerSelf{
    private static final int COST = 75;
    private static final String NAME = "Add shown";
    private static final String DESCRIPTION = "Add a random card to your hand and show it to everyone";
    public SuperpowerAdd() {
        super(COST, NAME, DESCRIPTION);}

    @Override
    protected void use(Player player,Deck deck) {
        Card card = deck.draw();
        player.addCard(card);
        card.show();
    }
    public static int getCost() {
        return COST;
    }
    
}