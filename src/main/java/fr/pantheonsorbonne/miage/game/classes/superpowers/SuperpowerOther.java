package fr.pantheonsorbonne.miage.game.classes.superpowers;

import fr.pantheonsorbonne.miage.game.classes.cards.Card;
import fr.pantheonsorbonne.miage.game.classes.cards.Exceptions.AlreadyUsedException;
import fr.pantheonsorbonne.miage.game.classes.cards.Exceptions.NotEnoughChipsException;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;

public abstract class SuperpowerOther extends Superpower {

    protected SuperpowerOther(int cost) {
        super(cost);
    }

    public Card useOnOther(Player player, Player other) throws AlreadyUsedException, NotEnoughChipsException {
            this.handleChecksAndUse(player);
            this.incrementUse();
            return this.use(player, other);
    }

    protected abstract Card use(Player player, Player other);
}
