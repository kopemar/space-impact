package cz.cvut.fel;

import cz.cvut.fel.Managers.UserEventHandler;
import cz.cvut.fel.Model.Game;
import cz.cvut.fel.Model.Player;

import cz.cvut.fel.util.WrongUserInputException;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.Assert;

import org.junit.Test;

public class UserEventHandlerTest {

    @Test
    public void keyReactionTest() {
        int initialX = 100;
        int initialY = 100;
        Game game = new Game();
        try {
            game.setLifeCount(3);
            game.setLifeWorth(100);
        } catch (WrongUserInputException e) {
            e.printStackTrace();
        }

        Player player = new Player(initialX, initialY, game);
        UserEventHandler userEventHandler = new UserEventHandler(player, false, game);

        int movement = userEventHandler.getMOVEMENT_VAL();
        KeyEvent keyEvent1 = new KeyEvent(KeyEvent.ANY, "", "", KeyCode.S, false, false, false, false);

        userEventHandler.handle(keyEvent1);
        Assert.assertEquals(initialY  + movement, player.getY());

        KeyEvent keyEvent2 = new KeyEvent(KeyEvent.ANY, "", "", KeyCode.W, false, false, false, false);
        userEventHandler.handle(keyEvent2);
        Assert.assertEquals(initialY, player.getY());
    }
}
