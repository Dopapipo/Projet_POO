package fr.pantheonsorbonne.miage.game.classes.pokerTableStuff;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.pantheonsorbonne.miage.game.classes.cards.Card;
import fr.pantheonsorbonne.miage.game.classes.cards.CardColor;
import fr.pantheonsorbonne.miage.game.classes.cards.CardValue;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.Player;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.PlayerBot;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.PlayerBotSmarter;
import fr.pantheonsorbonne.miage.game.classes.playerStuff.PlayerHand;

class PlayerTests {
    @Test
    void testPlayer() {
        Player player = new PlayerBot("Player");
        assertEquals(0, player.getChipStack());
        assertEquals("Player", player.getName());
        assertEquals(3, ((PlayerBot) player).getCommand(0));
        assertEquals(0, ((PlayerBot) player).getBetAmount(0));
        Player player2 = new PlayerBotSmarter("Player2");
        assertEquals (0, player2.getChipStack());
        assertTrue (((PlayerBot) player).getSuperpower() < 5 && ((PlayerBot) player).getSuperpower() > -1);
        assertTrue (((PlayerBot) player).askForPlayerToUseSuperpowerOn("Player1,Player2").equals("Player1")
                || ((PlayerBot) player).askForPlayerToUseSuperpowerOn("Player1,Player2").equals("Player2"));

    }

    @Test
    void testPlayerBotSmarter() {
        Player player = new PlayerBotSmarter("Player", 300);
        PokerTable table = new PokerTableAutomatisee(Arrays.asList(new Player[] { player }));
        table.giveCards();
        table.flop();
        assert (player.getChipStack() == 300);
        assert (player.getName().equals("Player"));
        // player is alone so of course he has 100% winrate and raises he's SMART
        assert (((PlayerBotSmarter) player).getCommand(0) == 3);
        assert (((PlayerBotSmarter) player).getSuperpower() < 5 && ((PlayerBot) player).getSuperpower() > -1);
        assert (((PlayerBotSmarter) player).askForPlayerToUseSuperpowerOn("Player1,Player2").equals("Player1")
                || ((PlayerBotSmarter) player).askForPlayerToUseSuperpowerOn("Player1,Player2").equals("Player2"));
        player.setChipStack(250);
        assertEquals(0, ((PlayerBotSmarter) player).getSuperpower());
        player.won(500); // reset superpower and give him money
        assertEquals(3, ((PlayerBotSmarter) player).getSuperpower());
        assertEquals(2, ((PlayerBotSmarter) player).getSuperpower());
        ((PlayerBotSmarter) player).setWinrate(1);
        assertEquals(((PlayerBotSmarter) player).getBetAmount(0), player.getChipStack());
        ((PlayerBotSmarter) player).setWinrate(0.8);
        assertEquals(((PlayerBotSmarter) player).getBetAmount(0), player.getChipStack() * 0.3);
        ((PlayerBotSmarter) player).setWinrate(0.5);
        assertEquals(((PlayerBotSmarter) player).getBetAmount(0), player.getChipStack() * 0.1);
        Player player2 = new PlayerBot("Dumdum", 300);
        Card p1Card = new Card(CardValue.ACE, CardColor.DIAMOND);
        Card p1Cardd = new Card(CardValue.ACE, CardColor.CLOVER);
        Card p2Card = new Card(CardValue.TWO, CardColor.DIAMOND);
        Card p2Cardd = new Card(CardValue.FOUR, CardColor.SPADE);
        List<Card> p1cards = new ArrayList<>();
        p1cards.add(p1Card);
        p1cards.add(p1Cardd);
        List<Card> p2cards = new ArrayList<>();
        p2cards.add(p2Card);
        p2cards.add(p2Cardd);
        player.setHand(new PlayerHand(p1cards));
        player2.setHand(new PlayerHand(p2cards));
        table = new PokerTableAutomatisee(Arrays.asList(new Player[] { player, player2 }));
        table.getDealer()
                .setHand(Arrays.asList(new Card[] { new Card(CardValue.ACE, CardColor.HEART),
                        new Card(CardValue.ACE, CardColor.SPADE), new Card(CardValue.KING, CardColor.HEART),
                        new Card(CardValue.JACK, CardColor.CLOVER), new Card(CardValue.EIGHT, CardColor.HEART) }));
        player2.showRandomCard();
        player2.showRandomCard();
        table.updateShownCards();
        table.updateDealerHandForPlayers();
        // all of the above is to make sure our smart player has <0.4 calculated winrate
        assertEquals(1, ((PlayerBotSmarter) player).getCommand(0));
        // And if he has to pay anything with that abysmal winrate, he folds...
        assertEquals(2, ((PlayerBotSmarter) player).getCommand(10));
    }

    @Test
    void testPlayerHand() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(CardValue.ACE, CardColor.CLOVER));
        cards.add(new Card(CardValue.ACE, CardColor.DIAMOND));

        PlayerHand hand = new PlayerHand(cards);
        hand.remove(new Card(CardValue.ACE, CardColor.CLOVER));
        assertFalse(hand.getHand().contains(new Card(CardValue.ACE, CardColor.CLOVER)));
        assertTrue(hand.getHand().contains(new Card(CardValue.ACE, CardColor.DIAMOND)));
        hand.removeRandomCard();
        assertTrue(hand.getHand().isEmpty());
        hand.showRandomCard();
        hand.add(new Card(CardValue.ACE, CardColor.CLOVER));
        hand.showRandomCard();
        assertTrue(hand.getHand().contains(new Card(CardValue.ACE, CardColor.CLOVER)));
        assertTrue(hand.getCardAtIndex(0).isFaceUp());
        hand.showRandomCard();
        // call this method a bunch to make sure there's no infinite loop
        // (and also to make jacoco happy :D)
    }

}
