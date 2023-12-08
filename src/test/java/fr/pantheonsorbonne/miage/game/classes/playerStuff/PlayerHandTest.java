package fr.pantheonsorbonne.miage.game.classes.playerStuff;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.pantheonsorbonne.miage.game.classes.cards.Card;
import fr.pantheonsorbonne.miage.game.classes.cards.CardColor;
import fr.pantheonsorbonne.miage.game.classes.cards.CardValue;

public class PlayerHandTest {
        @Test
    void testPlayerHand() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(CardValue.ACE, CardColor.CLOVER));
        cards.add(new Card(CardValue.ACE, CardColor.DIAMOND));

        PlayerHand hand = new PlayerHand(cards);
        hand.remove(new Card(CardValue.ACE, CardColor.CLOVER));
        assertFalse(hand.getHand().contains(new Card(CardValue.ACE, CardColor.CLOVER)));
        assertTrue(hand.getHand().contains(new Card(CardValue.ACE, CardColor.DIAMOND)));
        hand.removeRandomCard();
        assertTrue(hand.getHand().isEmpty());
        hand.showRandomCard();
        hand.add(new Card(CardValue.ACE, CardColor.CLOVER));
        hand.showRandomCard();
        assertTrue(hand.getHand().contains(new Card(CardValue.ACE, CardColor.CLOVER)));
        assertTrue(hand.getCardAtIndex(0).isFaceUp());
        hand.showRandomCard();
        // call this method a bunch to make sure there's no infinite loop
    }    
}
