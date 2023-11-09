package fr.pantheonsorbonne.miage.game.classes.superpowers;

import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;

public abstract class Superpower {
    protected int cost;
    protected String name;
    protected String description;
    protected boolean hasBeenUsed;
    public void setDescription(String description) {
        this.description = description;
    }
    public Superpower(int cost, String name, String description){
        this.cost = cost;
        this.name = name;
        this.description = description;
        this.hasBeenUsed=false;
    }
    public int getCost(){
        return this.cost;
    }
    public String getName() {
        return this.name;
    }
    public boolean canUse(Player player){
        return player.getChipStack() >= this.cost;
    }
    public String getDescription() {
        return description;
    }
    public boolean hasBeenUsed() {
        return hasBeenUsed;
    }
    public void setHasBeenUsed(boolean hasBeenUsed) {
        this.hasBeenUsed = hasBeenUsed;
    }



}
