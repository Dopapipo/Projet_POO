package fr.pantheonsorbonne.miage.game.classes.superpowers;

import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;

public class SuperpowerDestroy extends SuperpowerOther{
    private static final int COST = 100;
    private static final String NAME = "Destroy";
    private static final String DESCRIPTION = "Destroy a random card from the other player's hand.";
    public SuperpowerDestroy(){
        super(COST, NAME, DESCRIPTION);
    }
    @Override
    public void use(Player player, Player other){
        other.getPlayerHand().getHand().remove((int)Math.random() * other.getPlayerHand().getHand().size());
    }
    
}
