package fr.pantheonsorbonne.miage.game.classes.cards;

//this class is used to represent a card that has been inverted by a player and is only used in app. logic
//it brings no extra functionality at the time, except readability for the developers
public class InvertedCard extends Card {

    public InvertedCard(CardValue value, CardColor color) {
        super(value, color);

    }

}
