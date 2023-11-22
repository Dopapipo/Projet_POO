package fr.pantheonsorbonne.miage.game.classes.pokerTableStuff;
/*
 * Tests blinds functionnalities by themselves & within a poker table
 */

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;

public class BlindsTest {
    @Test
    void testBlindIncrease() {
        Blind blind = new Blind(10, null);
        blind.increase(10);
        assert blind.getValue() == 20;
    }

    @Test
    void testBlindToString() {
        Blind blind = new Blind(10, new Player("Speedy Gonzales"));
        assert blind.toString().equals("Speedy Gonzales has to pay a blind of 10");
    }

    @Test
    void testBlindIncreaseInPokerTable() {
        Player speedyGonzales = new Player("Speedy Gonzales", 300);
        Player luckyLuke = new Player("Lucky Luke", 300);
        Player pabloEscobar = new Player("Pablo Escobar", 300);
        PokerTable table = new PokerTableAutomatisee(
                Arrays.asList(new Player[] { speedyGonzales, luckyLuke, pabloEscobar }));
        table.initializeBlinds();
        assert (table.getSmallBlind().getValue() == table.getDefaultBlind() / 2);
        assert (table.getBigBlind().getValue() == table.getDefaultBlind());
        table.increaseBlinds();
        assert (table.getSmallBlind().getValue() == table.getDefaultBlind());
        assert (table.getBigBlind().getValue() == table.getDefaultBlind() * 2);
        assertEquals(table.getBigBlind().getPlayer(), pabloEscobar); // last player in list should get BB
        assertEquals(table.getSmallBlind().getPlayer(), luckyLuke); // the one before him should get SB
        table.switchBlinds();
        assertEquals(table.getBigBlind().getPlayer(), speedyGonzales); // blinds place increments mod size(PlayerList)
        assertEquals(table.getSmallBlind().getPlayer(), pabloEscobar);
    }
}
