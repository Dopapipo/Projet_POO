package fr.pantheonsorbonne.miage.game.classes;

public class SuperpowerShow extends SuperpowerOther{

    public SuperpowerShow(int cost, String name, String description) {
        super(50, "Show random card", "This superpower allows you to see a random card from the other player's hand.");
    }
    /*
     * This method is used to show a random card from the other player's hand.
     */
    @Override
    public void use(Player player, Player other) {
        other.getPlayerHand().getPlayerHand().get((int)Math.random() * other.getPlayerHand().getPlayerHand().size()).show();
    }
    
}
