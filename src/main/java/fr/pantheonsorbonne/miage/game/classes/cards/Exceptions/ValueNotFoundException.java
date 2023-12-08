package fr.pantheonsorbonne.miage.game.classes.cards.Exceptions;

public class ValueNotFoundException extends RuntimeException{
    //Value not found for parsing from string exception
    public ValueNotFoundException(String message) {
        super(message);
    }
    
}
