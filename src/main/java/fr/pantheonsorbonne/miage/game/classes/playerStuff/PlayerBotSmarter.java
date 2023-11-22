package fr.pantheonsorbonne.miage.game.classes.playerStuff;

/*
 * This playerbot always tries to use the add superpower as it's the most overpowered, so he's 
 * smarter than the rest: he figured out the META!
 */
public class PlayerBotSmarter extends PlayerBot {
    private int superpower = 3;

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
        int toReturn = this.superpower;
        this.superpower = 1000; // so that we don't use it again for this round
        return toReturn;
    }

    @Override
    public void won(int winnings) {
        super.won(winnings);
        this.superpower = 3;// reset superpower bc round ended
    }

}
