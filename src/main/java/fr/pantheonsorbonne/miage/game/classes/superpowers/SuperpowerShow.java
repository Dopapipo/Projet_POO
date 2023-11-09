package fr.pantheonsorbonne.miage.game.classes.superpowers;

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
        other.getPlayerHand().getPlayerHand().get((int)Math.random() * other.getPlayerHand().getPlayerHand().size()).show();
    }
    
}
