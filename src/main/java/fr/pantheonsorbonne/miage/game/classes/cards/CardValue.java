package fr.pantheonsorbonne.miage.game.classes.cards;
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

    final private String stringRepresentation;
    final private int rank;

    CardValue(String stringRepresentation, int value) {
        this.stringRepresentation = stringRepresentation;
        this.rank = value;
    }
    //The stuff below will be useful for network most likely
    /**
     * From a string representation, return the cad
     *
     * @param str
     * @return the corresponding card
     * @throws RuntimeException if the representation is invalid
     */
    
     public static CardValue valueOfStr(String str) {
        for (CardValue value : CardValue.values()) {
            if (str.equals(value.getStringRepresentation())) {
                return value;
            }
        }

        throw new RuntimeException("failed to parse value");

    }

    public String getStringRepresentation() {
        return stringRepresentation;
    }

    /**
     * the rank of the card for comparison purpose. The higher the rank, the better the card
     *
     * @return
     */
    public int getRank() {
        return rank;
    }
    public CardValue getInverted() {
        // int number = this.rank;
        // int targetRank = this.rank<=8?(8-number)*2+number:((number-(number-8)*2));
        return getValueFromRank(14 - this.getRank()+2); //found this formula while writing the unit test
    }
    private CardValue getValueFromRank(int rank) {
        for (CardValue value : CardValue.values()) {
            if (value.getRank()==rank) {
                return value;
            }
        }
        return null;
    }
	public CardValue getNext() {
		return CardValue.values()[(this.ordinal() + 1)%CardValue.values().length];
	}

    public int compare(CardValue other) {
        if (other==null) {
            return 1;
        }
        return this.getRank()-other.getRank();
    
    }
	public CardValue max(CardValue other) {
		if (this.compare(other)>0) {
			return this;
		}
		return other;
	}
}