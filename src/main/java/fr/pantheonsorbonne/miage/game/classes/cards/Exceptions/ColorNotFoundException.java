package fr.pantheonsorbonne.miage.game.classes.cards.Exceptions;

public class ColorNotFoundException extends RuntimeException{
    //Color not found for parsing from string exception
    public ColorNotFoundException(String message) {
        super(message);
    }
}
