package fr.pantheonsorbonne.miage.game.classes.superpowers;

import fr.pantheonsorbonne.miage.game.classes.cards.Card;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;

public class SuperpowerDestroy extends SuperpowerOther {
    //i'll the name and description here for readability
    private static final int COST = 100;
    private static final String NAME = "Destroy";
    private static final String DESCRIPTION = "Destroy a random card from the other player's hand.";

    public SuperpowerDestroy() {
        super(COST);
    }

    @Override
    public Card use(Player player, Player other) {
        return other.removeRandomCard();
    }

    public static int getCost() {
        return COST;
    }
}
