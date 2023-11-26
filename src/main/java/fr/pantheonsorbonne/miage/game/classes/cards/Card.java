package fr.pantheonsorbonne.miage.game.classes.cards;

public class Card {
	private CardValue cardValue;
	private CardColor cardColor;
	// will be used in PokerTable to add face up cards to every player's map of
	// known cards; to use when player adds a shown card to his hand
	private boolean faceUp;

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
			return this.getCardValue() == ((Card) card).getCardValue() && this.getCardColor().equals(((Card)card).getCardColor());

		}
		return false;
	}
	//Sonarlint says we should override hashCode when we override equals so let's do it
	@Override
	public int hashCode() {
		return this.getCardValue().hashCode() + this.getCardColor().hashCode();
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
	/*
	 * Useful for network communication
	 * Regex not "," because that's the network regex, so we would have problems
	 */
	public static Card stringToCard(String str) {
		String[] values = str.split(";");
		return new Card(CardValue.valueOfStr(values[0]), CardColor.valueOfStr(values[1]));
	}

	public static String cardToString(Card card) {
		return card.getCardValue().getStringRepresentation() + ";" + card.getCardColor().getStringRepresentation();
	}
}