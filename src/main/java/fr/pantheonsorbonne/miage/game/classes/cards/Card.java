package fr.pantheonsorbonne.miage.game.classes.cards;

public class Card  {
	// les attributs
	private CardValue cardValue;
	private CardColor cardColor;
	//will be used in PokerTable to add face up cards to every player's map of known cards
	private boolean faceUp;

	// le constructeur
	public Card(CardValue value, CardColor color) {
		this.cardValue = value;
		this.cardColor = color;
		this.faceUp=false;
	}
	public CardValue getCardValue() {
		return cardValue;
	}



	public CardColor getCardColor() {
		return cardColor;
	}



	// les méthodes
	@Override
	public String toString() {
		String color = "";
		switch (this.cardColor) {
			case SPADE:
				color = "SPADE";
				break;
			case DIAMOND:
				color = "DIAMOND";
				break;
			case HEART:
				color = "HEART";
				break;
			case CLOVER:
				color = "CLOVER";
				break;
		}
		return String.valueOf(this.cardValue) + " of " + color;
	}

	public boolean equals(Card card) {
		return this.getCardValue() == card.getCardValue();
	}
	
	public boolean compareTo(Card card) {
		return this.getCardValue().compare(card.getCardValue()) > 0;
	}

	public boolean isFaceUp() {
		return faceUp;
	}

	public void show() {
		this.faceUp = true;
	}
	public String toFancyString() {
        int rank = this.getCardValue().ordinal();
        if (rank > 10) {
            rank++;
        }
        return new String(Character.toChars(this.cardColor.getCode() + rank));
    }
}