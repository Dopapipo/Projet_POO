package fr.pantheonsorbonne.miage.game.classes.cards;

import org.junit.jupiter.api.Test;

public class CardsTest {
    @Test
    void testInvertCardValue() {
        //Naive way of doing that : (useful to reassure myself...)
        CardValue ace = CardValue.ACE;
        assert(ace.getInverted().getRank() == 2);
        assert(ace.getInverted()==CardValue.TWO);
        CardValue king = CardValue.KING;
        assert(king.getInverted().getRank() == 3);
        assert(king.getInverted()==CardValue.THREE);
        CardValue queen = CardValue.QUEEN;
        assert(queen.getInverted().getRank() == 4);
        assert(queen.getInverted()==CardValue.FOUR);
        CardValue jack = CardValue.JACK;
        assert(jack.getInverted().getRank() == 5);
        assert(jack.getInverted()==CardValue.FIVE);
        CardValue ten = CardValue.TEN;
        assert(ten.getInverted().getRank() == 6);
        assert(ten.getInverted()==CardValue.SIX);
        CardValue nine = CardValue.NINE;
        assert(nine.getInverted().getRank() == 7);
        assert(nine.getInverted()==CardValue.SEVEN);
        CardValue eight = CardValue.EIGHT;
        assert(eight.getInverted().getRank() == 8);
        assert(eight.getInverted()==CardValue.EIGHT);
        CardValue seven = CardValue.SEVEN;
        assert(seven.getInverted().getRank() == 9);
        assert(seven.getInverted()==CardValue.NINE);
        CardValue six = CardValue.SIX;
        assert(six.getInverted().getRank() == 10);
        assert(six.getInverted()==CardValue.TEN);
        CardValue five = CardValue.FIVE;
        assert(five.getInverted().getRank() == 11);
        assert(five.getInverted()==CardValue.JACK);
        CardValue four = CardValue.FOUR;
        assert(four.getInverted().getRank() == 12);
        assert(four.getInverted()==CardValue.QUEEN);
        CardValue three = CardValue.THREE;
        assert(three.getInverted().getRank() == 13);
        assert(three.getInverted()==CardValue.KING);
        CardValue two = CardValue.TWO;
        assert(two.getInverted().getRank() == 14);
        assert(two.getInverted()==CardValue.ACE);
        // //smarter way of testing that:
        // for (CardValue value : CardValue.values()) {
        //     CardValue inverted = value.getInverted();
        //     assert(inverted.getRank() == 14 - value.getRank()+2); //Ace will be 2, King will be 3, etc...
        // }
    }


}
