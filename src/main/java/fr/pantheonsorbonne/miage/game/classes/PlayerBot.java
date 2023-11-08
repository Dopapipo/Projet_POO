package fr.pantheonsorbonne.miage.game.classes;

public class PlayerBot extends Player {

    public PlayerBot(String name) {
        super(name);
    }
    public PlayerBot(String name, int chipStack) {
        super(name, chipStack);    }
    
    	/**
	 * Call,fold,raise
	 * For now, we always all-in. 
	 * @return 1,2,3 (call,fold,raise)
	 */
	public int getCommand() {
		return 3;
	}
	/**
	 * We always all-in
	 * @return how much we bet
	 */
	public int getBetAmount() {
		return this.getChipStack();
	}
}
