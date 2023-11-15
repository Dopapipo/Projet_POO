package fr.pantheonsorbonne.miage.game.classes.superpowers;

import java.util.ArrayList;
import java.util.List;

import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;
public abstract class Superpower {
    protected int cost;
    protected String name;
    protected String description;
    protected List<Player> playersThatUsedIt;
    protected int numberOfUses;
    public Superpower(int cost, String name, String description){
        this.cost = cost;
        this.name = name;
        this.description = description;
        this.playersThatUsedIt=new ArrayList<>();
    }
    protected boolean canUse(Player player){
        return player.getChipStack() >= this.cost;
    }
    protected boolean hasBeenUsedBy(Player player){
        return this.playersThatUsedIt.contains(player);
    }

    public void resetUsage() {
        this.playersThatUsedIt.clear();
    }
    protected void handleChecksAndUse(Player player) throws RuntimeException {
        if (!this.canUse(player)) throw new RuntimeException("Not enough chips");
        if (this.hasBeenUsedBy(player)) throw new RuntimeException("Already used");
        player.setChipStack(player.getChipStack() - this.cost);
        this.playersThatUsedIt.add(player);
    }
    public int getNumberOfUses() {
        return numberOfUses;
    }



}
