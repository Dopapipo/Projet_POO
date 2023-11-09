package fr.pantheonsorbonne.miage.game.classes.superpowers;

import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;

public abstract class SuperpowerOther extends Superpower {
    public SuperpowerOther(int cost, String name, String description){
        super(cost, name, description);
    }
    public void useOnOther(Player player, Player other){
        player.setChipStack(player.getChipStack() - this.getCost());
        this.use(player, other);
        this.hasBeenUsed = true;
    }
    
    protected abstract void use(Player player, Player other);
}
