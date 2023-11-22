package fr.pantheonsorbonne.miage.game.classes.playerStuff;

import org.junit.jupiter.api.Test;

public class PlayerTests {
    @Test
    void testPlayer() {
        Player player = new PlayerBot("Player");
        assert (player.getChipStack() == 0);
        assert (player.getName().equals("Player"));
        assert (((PlayerBot) player).getCommand(0) == 3);
        assert (((PlayerBot) player).getBetAmount(0) == 0);
        Player player2 = new PlayerBotSmarter("Player2");
        assert (player2.getChipStack() == 0);
        assert (((PlayerBot) player).getSuperpower() < 5 && ((PlayerBot) player).getSuperpower() > -1);
        assert (((PlayerBot) player).askForPlayerToUseSuperpowerOn("Player1,Player2").equals("Player1")
                || ((PlayerBot) player).askForPlayerToUseSuperpowerOn("Player1,Player2").equals("Player2"));

    }
}
