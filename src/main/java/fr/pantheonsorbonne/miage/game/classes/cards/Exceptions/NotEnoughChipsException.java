package fr.pantheonsorbonne.miage.game.classes.cards.Exceptions;

public class NotEnoughChipsException extends Exception {
    //Not enough chips for super power exception
    public NotEnoughChipsException(String message) {
        super(message);
    }
}
