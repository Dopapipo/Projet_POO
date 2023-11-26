package fr.pantheonsorbonne.miage.game.classes.superpowers;

import fr.pantheonsorbonne.miage.game.classes.cards.Card;
import fr.pantheonsorbonne.miage.game.classes.cards.Exceptions.AlreadyUsedException;
import fr.pantheonsorbonne.miage.game.classes.cards.Exceptions.NotEnoughChipsException;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;

public abstract class SuperpowerOther extends Superpower {

    protected SuperpowerOther(int cost, String name, String description) {
        super(cost, name, description);
    }

    public Card useOnOther(Player player, Player other) throws AlreadyUsedException, NotEnoughChipsException {
            this.handleChecksAndUse(player);
            this.numberOfUses++;
            return this.use(player, other);
    }

    protected abstract Card use(Player player, Player other);
}
