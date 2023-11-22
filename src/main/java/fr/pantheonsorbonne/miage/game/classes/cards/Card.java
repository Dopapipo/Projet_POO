package fr.pantheonsorbonne.miage.game.classes.cards;

public class Card {
	// les attributs
	private CardValue cardValue;
	private CardColor cardColor;
	// will be used in PokerTable to add face up cards to every player's map of
	// known cards
	private boolean faceUp;

	// le constructeur
	public Card(CardValue value, CardColor color) {
		this.cardValue = value;
		this.cardColor = color;
		this.faceUp = false;
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
		return this.cardValue.getStringRepresentation() + this.cardColor.getStringRepresentation();
	}

	@Override
	public boolean equals(Object card) {
		if (card == null) {
			return false;
		}
		if (card == this) {
			return true;
		}
		if (card instanceof Card) {
			return this.getCardValue() == ((Card) card).getCardValue();

		}
		return false;
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

	public static Card stringToCard(String str) {
		String[] values = str.split(";");
		return new Card(CardValue.valueOfStr(values[0]), CardColor.valueOfStr(values[1]));
	}

	public static String cardToString(Card card) {
		return card.getCardValue().getStringRepresentation() + ";" + card.getCardColor().getStringRepresentation();
	}
	// public String toFancyString() {
	// int rank = this.getCardValue().ordinal();
	// if (rank > 10) {
	// rank++;
	// }
	// return new String(Character.toChars(this.cardColor.getCode() + rank));
	// }
}