package fr.pantheonsorbonne.miage.game.classes.superpowers;

import fr.pantheonsorbonne.miage.game.classes.pokerTableStuff.Deck;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;

public abstract class SuperpowerSelf extends Superpower {
    public SuperpowerSelf(int cost, String name, String description){
        super(cost, name, description);
    }
    public void useOnSelf(Player player,Deck deck) throws RuntimeException{
        this.handleChecksAndUse(player);
        this.use(player,deck);
    }
    protected abstract void use(Player player, Deck deck);
}