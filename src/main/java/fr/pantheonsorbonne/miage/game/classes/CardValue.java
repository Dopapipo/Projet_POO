package fr.pantheonsorbonne.miage.game.classes;

public enum CardValue {
	TWO,THREE,FOUR,FIVE,SIX,SEVEN,EIGHT,NINE,TEN,JACK,QUEEN,KING,ACE;
	

	public CardValue getNext() {
		return values()[(this.ordinal()+1)%values().length];
	}
	
	public int compare(CardValue value) {
		if (value==null) {
			return 1;
		}
		return this.ordinal()-value.ordinal();
	}
	public CardValue max(CardValue other) {
		if (this.compare(other)>0) {
			return this;
		}
		return other;
	}
}
