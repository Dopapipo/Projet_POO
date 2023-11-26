package fr.pantheonsorbonne.miage.game.classes.pokerTableStuff;
/*
 * Tests blinds functionnalities by themselves & within a poker table
 */

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;

 class BlindsTest {
    @Test
    void testBlindIncrease() {
        Blind blind = new Blind(10, null);
        blind.increase(10);
        assertEquals (20,blind.getValue());
    }

    @Test
    void testBlindToString() {
        Blind blind = new Blind(10, new Player("Speedy Gonzales"));
        assertEquals ("Speedy Gonzales has to pay a blind of 10",blind.toString());
    }

    @Test
    void testBlindIncreaseInPokerTable() {
        Player speedyGonzales = new Player("Speedy Gonzales", 300);
        Player luckyLuke = new Player("Lucky Luke", 300);
        Player pabloEscobar = new Player("Pablo Escobar", 300);
        PokerTable table = new PokerTableAutomatisee(
                Arrays.asList(new Player[] { speedyGonzales, luckyLuke, pabloEscobar }));
        table.initializeBlinds();
        assertEquals (table.getDefaultBlind() / 2,table.getSmallBlind().getValue());
        assertEquals ( table.getDefaultBlind(),table.getBigBlind().getValue() );
        table.increaseBlinds();
        assertEquals ( table.getDefaultBlind(),table.getSmallBlind().getValue());
        assertEquals (table.getDefaultBlind() * 2,table.getBigBlind().getValue());
        assertEquals(pabloEscobar,table.getBigBlind().getPlayer()); // last player in list should get BB
        assertEquals(luckyLuke,table.getSmallBlind().getPlayer()); // the one before him should get SB
        table.switchBlinds();
        assertEquals(speedyGonzales,table.getBigBlind().getPlayer()); // blinds place increments mod size(PlayerList)
        assertEquals(pabloEscobar,table.getSmallBlind().getPlayer());
    }
}
