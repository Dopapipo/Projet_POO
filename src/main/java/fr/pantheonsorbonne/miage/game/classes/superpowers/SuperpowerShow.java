package fr.pantheonsorbonne.miage.game.classes.superpowers;

import java.util.HashSet;
import java.util.Set;

import fr.pantheonsorbonne.miage.game.classes.cards.Card;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;

public class SuperpowerShow extends SuperpowerOther{
    private static final int COST = 50;
    private static final String NAME = "See random card";
    private static final String DESCRIPTION = "This superpower allows you to see a random card from the other player's hand.";
    public SuperpowerShow() {
        super(COST,NAME,DESCRIPTION);
    }
    /*
     * This method is used to show a random card from the other player's hand.
     */
    @Override
    public void use(Player player, Player other) {
        player.getCardsKnownFromOtherPlayers().putIfAbsent(other,(Set<Card>)new HashSet<Card>());
        int k;
        do {
            k=((int) Math.random() * other.getPlayerHand().getHand().size());
        } while(other.getPlayerHand().getHand().get(k).isFaceUp());
        Card card = other.getPlayerHand().getHand().get(k);
        player.getCardsKnownFromOtherPlayers().get(other).add((card));
        card.show();
    }
    public static int getCost() {
        return COST;
    }
}
