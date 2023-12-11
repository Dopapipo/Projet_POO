package fr.pantheonsorbonne.miage.game.classes.superpowers;

import java.util.HashSet;
import fr.pantheonsorbonne.miage.game.classes.cards.Card;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;

public class SuperpowerShow extends SuperpowerOther {
    //i'll the name and description here for readability
    private static final int COST = 100;
    private static final String NAME = "See random card";
    private static final String DESCRIPTION = "This superpower allows you to see a random card from the other player's hand.";

    public SuperpowerShow() {
        super(COST);
    }

    /*
     * This method is used to show a random card from the other player's hand.
     */
    @Override
    public Card use(Player player, Player other) {
        player.getCardsKnownFromOtherPlayers().putIfAbsent(other, new HashSet<>());
        int k;
        if (other.allCardsAreShown()
                || player.getCardsKnownFromOtherPlayers().get(other).size() == other.handSize()) {
            return null;
        }
        do {
            k = (random.nextInt(other.handSize()));
        } while (other.getCardAtIndex(k).isFaceUp()
                || player.getCardsKnownFromOtherPlayers().get(other).contains(other.getCardAtIndex(k)));
        Card card = other.getCardAtIndex(k);
        player.getCardsKnownFromOtherPlayers().get(other).add((card));
        return card;
    }

    public static int getCost() {
        return COST;
    }
}
