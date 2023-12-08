package fr.pantheonsorbonne.miage.game.classes.superpowers;

public enum SuperpowerChoice {
    NONE, ADD, DESTROY, SHOW, ADD_HIDDEN;
    public static SuperpowerChoice fromString(String s) {
        switch (s) {
            case "ADD":
                return ADD;
            case "DESTROY":
                return DESTROY;
            case "SHOW":
                return SHOW;
            case "ADD_HIDDEN":
                return ADD_HIDDEN;
            default:
                return NONE;
        }
    }
}
