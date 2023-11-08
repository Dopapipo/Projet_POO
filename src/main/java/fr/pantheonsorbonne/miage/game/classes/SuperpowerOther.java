package fr.pantheonsorbonne.miage.game.classes;

public abstract class SuperpowerOther extends Superpower {
    public SuperpowerOther(int cost, String name, String description){
        super(cost, name, description);
    }
    public void useOnOther(Player player, Player other){
        player.setChipStack(player.getChipStack() - this.getCost());
        this.use(player, other);
        this.hasBeenUsed = true;
    }
    
    public abstract void use(Player player, Player other);
}
