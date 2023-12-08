package fr.pantheonsorbonne.miage.game.classes.cards;

import fr.pantheonsorbonne.miage.game.classes.cards.Exceptions.ValueNotFoundException;

/**
 * An enum that represend the possible cards value from a deck
 */
public enum CardValue {
    ACE("1", 14),
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    NINE("9", 9),
    TEN("10", 10),
    JACK("J", 11),
    QUEEN("Q", 12),
    KING("K", 13);

    private final  String stringRepresentation;
    private final  int rank;

    CardValue(String stringRepresentation, int value) {
        this.stringRepresentation = stringRepresentation;
        this.rank = value;
    }

    // The stuff below will be useful for network
    /**
     * From a string representation, return the card
     *
     * @param str
     * @return the corresponding card
     * @throws ValueNotFoundException if the representation is invalid
     */

    public static CardValue valueOfStr(String str) {
        for (CardValue value : CardValue.values()) {
            if (str.equals(value.getStringRepresentation())) {
                return value;
            }
        }

        throw new ValueNotFoundException("failed to parse value");

    }
    //Useful for network communication
    public String getStringRepresentation() {
        return stringRepresentation;
    }

    /**
     * the rank of the card for comparison purpose. The higher the rank, the better
     * the card
     *
     * @return
     */
    public int getRank() {
        return rank;
    }
    //Invert the card value (ACE -> TWO, KING -> THREE...)
    public CardValue getInverted() {
        return getValueFromRank(14 - this.getRank() + 2); 
    }
    //From a card rank, returns the corresponding CardValue
    private CardValue getValueFromRank(int rank) {
        for (CardValue value : CardValue.values()) {
            if (value.getRank() == rank) {
                return value;
            }
        }
        return null;
    }
    //Returns the next card value (ACE -> TWO, KING -> ACE...)
    //Useful to look for straights
    public CardValue getNext() {
        return CardValue.values()[(this.ordinal() + 1) % CardValue.values().length];
    }
    //Can't override CompareTo so we have to do it like that
    public int compare(CardValue other) {
        if (other == null) {
            return 1;
        }
        return this.getRank() - other.getRank();

    }
    //Returns the max between this and another CardValue
    public CardValue max(CardValue other) {
        if (this.compare(other) > 0) {
            return this;
        }
        return other;
    }
}