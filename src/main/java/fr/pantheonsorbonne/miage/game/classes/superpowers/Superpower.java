package fr.pantheonsorbonne.miage.game.classes.superpowers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.pantheonsorbonne.miage.game.classes.cards.Exceptions.AlreadyUsedException;
import fr.pantheonsorbonne.miage.game.classes.cards.Exceptions.NotEnoughChipsException;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;

/*
 * There was a choice to make : have useOnSelf() and useOnOther() method here, or in the subclasses.
 * We chose to have them in SuperpowerSelf and SuperpowerOther respectively.
 * There might be a cleaner way to do it though that makes better use of Polymorphism.
 */
public abstract class Superpower {
    private int cost;
    private List<Player> playersThatUsedIt;
    private int numberOfUses;
    protected Random random = new Random();

    protected Superpower(int cost) {
        this.cost = cost;
        this.playersThatUsedIt = new ArrayList<>();
    }

    protected boolean canUse(Player player) {
        return player.getChipStack() >= this.cost;
    }

    protected boolean hasBeenUsedBy(Player player) {
        return this.playersThatUsedIt.contains(player);
    }

    public void resetUsage() {
        this.playersThatUsedIt.clear();
    }

    protected void handleChecksAndUse(Player player) throws NotEnoughChipsException, AlreadyUsedException {
        if (!this.canUse(player))
            throw new NotEnoughChipsException("Not enough chips");
        if (this.hasBeenUsedBy(player))
            throw new AlreadyUsedException("Already used");
        player.setChipStack(player.getChipStack() - this.cost);
        this.playersThatUsedIt.add(player);
    }

    public int getNumberOfUses() {
        return numberOfUses;
    }
    protected void incrementUse() {
        this.numberOfUses++;
    }

}
