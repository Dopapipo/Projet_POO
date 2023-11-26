package fr.pantheonsorbonne.miage.game.classes.superpowers;

import fr.pantheonsorbonne.miage.game.classes.pokerTableStuff.Deck;
import fr.pantheonsorbonne.miage.game.classes.cards.Card;
import fr.pantheonsorbonne.miage.game.classes.cards.Exceptions.AlreadyUsedException;
import fr.pantheonsorbonne.miage.game.classes.cards.Exceptions.NotEnoughChipsException;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;


public abstract class SuperpowerSelf extends Superpower {
    protected SuperpowerSelf(int cost, String name, String description) {
        super(cost, name, description);
    }

    public Card useOnSelf(Player player, Deck deck) throws AlreadyUsedException, NotEnoughChipsException {
            this.handleChecksAndUse(player);
            this.numberOfUses++;
            return this.use(player, deck);
    }

    protected abstract Card use(Player player, Deck deck);
}