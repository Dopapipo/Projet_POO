package fr.pantheonsorbonne.miage.game.classes;

public class SuperpowerDestroy extends SuperpowerOther{
    public SuperpowerDestroy(String name, String description){
        super(100, "Destroy", "Destroy a random card from the other player's hand.");
    }
    @Override
    public void use(Player player, Player other){
        other.getPlayerHand().getPlayerHand().remove((int)Math.random() * other.getPlayerHand().getPlayerHand().size());
    }
    
}
