package fr.pantheonsorbonne.miage.game.classes.playerStuff;

/*
 * This playerbot always tries to use the add superpower as it's the most broken
 */
public class PlayerBotSmarter extends PlayerBot {

    public PlayerBotSmarter(String name) {
        super(name);
    }
    public PlayerBotSmarter(String name, int chipStack) {
        super(name, chipStack);
    }
    @Override
    public int getCommand() {
        return 3;
    }
    @Override
    public int getBetAmount() {
        return this.getChipStack();
    }
    @Override
    public int getSuperpower() {
        return 3;
    }

}
