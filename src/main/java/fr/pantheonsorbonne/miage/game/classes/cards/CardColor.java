package fr.pantheonsorbonne.miage.game.classes.cards;

import fr.pantheonsorbonne.miage.game.classes.cards.Exceptions.ColorNotFoundException;

/**
 * List the possible colors of a card
 */
public enum CardColor {
    SPADE(127137),
    HEART(127137 + 16),
    DIAMOND(127137 + 16 * 2),
    CLOVER(127137 + 16 * 3);

    private final int code;

    CardColor(int code) {
        this.code = code;
    }
    //Useful for network communication
    public static CardColor valueOfStr(String substring) {
        for (CardColor color : CardColor.values()) {
            if (color.name().substring(0, 1).equals(substring)) {
                return color;
            }
        }
        throw new ColorNotFoundException("No Such Color");
    }

    //Useful for network communication
    public String getStringRepresentation() {
        return "" + this.name().charAt(0);
    }
}