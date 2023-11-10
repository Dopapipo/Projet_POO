package fr.pantheonsorbonne.miage.game.classes.cards;

public class WinningCombination implements Comparable<WinningCombination> {
	private WinCondition winCondition;
	private CardValue cardValue;
	
	
	public WinningCombination(WinCondition wc,CardValue cv) {
		this.winCondition=wc;
		this.cardValue=cv;
	}
	@Override
	public int compareTo(WinningCombination wc) {
		// TODO Auto-generated method stub
		if (this.winCondition.compareTo(wc.winCondition)>0) {
			return 1;
		}
		if (this.winCondition.compareTo(wc.winCondition)==0) {
			return this.cardValue.compareTo(wc.cardValue);
		}
		return -1;
	}
	public WinCondition getWinCondition() {
		return winCondition;
	}
	public void setWinCondition(WinCondition winCondition) {
		this.winCondition = winCondition;
	}
	public CardValue getCardValue() {
		return cardValue;
	}
	public void setCardValue(CardValue cardValue) {
		this.cardValue = cardValue;
	}
	
	@Override
	public String toString() {
		return this.cardValue +" " + this.winCondition;
	}
	@Override
	public boolean equals(Object other) {
		if (other instanceof WinningCombination) {
			return this.compareTo((WinningCombination)other)==0;
		}
		return false;
	}
	
}
