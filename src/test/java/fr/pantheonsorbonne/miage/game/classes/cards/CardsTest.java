package fr.pantheonsorbonne.miage.game.classes.cards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CardsTest {
    @Test
    void testInvertCardValue() {
        CardValue ace = CardValue.ACE;
        assertEquals(2, ace.getInverted().getRank());
        assertEquals(CardValue.TWO, ace.getInverted());
        CardValue king = CardValue.KING;
        assertEquals(3, king.getInverted().getRank());
        assertEquals(CardValue.THREE, king.getInverted());
        CardValue queen = CardValue.QUEEN;
        assertEquals(4, queen.getInverted().getRank());
        assertEquals(CardValue.FOUR, queen.getInverted());
        CardValue jack = CardValue.JACK;
        assertEquals(5, jack.getInverted().getRank());
        assertEquals(CardValue.FIVE, jack.getInverted());
        CardValue ten = CardValue.TEN;
        assertEquals(6, ten.getInverted().getRank());
        assertEquals(CardValue.SIX, ten.getInverted());
        CardValue nine = CardValue.NINE;
        assertEquals(7, nine.getInverted().getRank());
        assertEquals(CardValue.SEVEN, nine.getInverted());
        CardValue eight = CardValue.EIGHT;
        assertEquals(8, eight.getInverted().getRank());
        assertEquals(CardValue.EIGHT, eight.getInverted());
        CardValue seven = CardValue.SEVEN;
        assertEquals(9, seven.getInverted().getRank());
        assertEquals(CardValue.NINE, seven.getInverted());
        CardValue six = CardValue.SIX;
        assertEquals(10, six.getInverted().getRank());
        assertEquals(CardValue.TEN, six.getInverted());
        CardValue five = CardValue.FIVE;
        assertEquals(11, five.getInverted().getRank());
        assertEquals(CardValue.JACK, five.getInverted());
        CardValue four = CardValue.FOUR;
        assertEquals(12, four.getInverted().getRank());
        assertEquals(CardValue.QUEEN, four.getInverted());
        CardValue three = CardValue.THREE;
        assertEquals(13, three.getInverted().getRank());
        assertEquals(CardValue.KING, three.getInverted());
        CardValue two = CardValue.TWO;
        assertEquals(14, two.getInverted().getRank());
        assertEquals(CardValue.ACE, two.getInverted());
    }

    @Test
    void testSmallStuff() {
        Object obj = new Object();
        Card card = new Card(CardValue.ACE, CardColor.SPADE);
        assertNotEquals(obj, card);
        assertNotEquals(null, card);
        Card card2 = new Card(CardValue.KING, CardColor.HEART);
        assertNotEquals(card, card2);
        Card card3 = new Card(CardValue.ACE, CardColor.HEART);
        assertNotEquals(card, card3);
        assertEquals(false, card.compareTo(card3));
        assertEquals(false, card.compareTo(card3));
        assertTrue(card.getCardValue().compare(card2.getCardValue()) > 0);
        assertEquals("1;S", Card.cardToString(card));
        assertEquals(card, Card.stringToCard("1;S"));
        assertThrows(RuntimeException.class, () -> CardColor.valueOfStr("K"));
        assertEquals("S", CardColor.SPADE.getStringRepresentation());
        assertEquals("H", CardColor.HEART.getStringRepresentation());
        assertEquals("D", CardColor.DIAMOND.getStringRepresentation());
        assertEquals("C", CardColor.CLOVER.getStringRepresentation());
        assertThrows(RuntimeException.class, () -> CardValue.valueOfStr("rlbnlengv"));
        assertEquals(CardValue.ACE, card.getCardValue().max(card2.getCardValue()));

    }

}
