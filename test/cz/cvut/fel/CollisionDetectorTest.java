package cz.cvut.fel;

import cz.cvut.fel.Managers.CollisionDetector;
import cz.cvut.fel.Managers.UserEventHandler;
import cz.cvut.fel.Model.*;

import cz.cvut.fel.util.WrongUserInputException;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CollisionDetectorTest {
    private Game game;
    private CollisionDetector glh;
    private Player player;
    private UserEventHandler userEventHandler;
    @Before
    public void setUp() {
        glh = new CollisionDetector();
        game = new Game();

        try {
            game.setLifeCount(3);
            game.setLifeWorth(100);
        } catch (WrongUserInputException e) {
            e.printStackTrace();
        }

        player = new Player(100, 100, game);

        userEventHandler = new UserEventHandler(player, false, game);
    }

    @Test
    public void beamCollisionDetectionTest() {

        int lifePoints = player.getLifePoints();
        EnemyBeam beam1 = new EnemyBeam(100,  100);
        EnemyBeam beam2 = new EnemyBeam(140,  201);

        glh.checkBeamCollision(player, beam1);

        Assert.assertEquals(lifePoints - beam1.getDamage(), player.getLifePoints());

        int lifePoints2 = player.getLifePoints();
        glh.checkBeamCollision(player, beam2);

        Assert.assertEquals(lifePoints2, player.getLifePoints());
    }

    @Test
    public void lifeCollisionDetectionTest() {
        int lifePoints = player.getLifePoints();
        Life life1 = new Life(100,  100);
        game.getLifePositionableManager().add(life1, false, null);
        glh.checkPLifeCollision(player, life1);

        KeyEvent keyEvent1 = new KeyEvent(KeyEvent.ANY, "", "", KeyCode.Q, false, false, false, false);
        userEventHandler.handle(keyEvent1);
        Assert.assertEquals(lifePoints, player.getLifePoints());

        player.damage(200);

        lifePoints = player.getLifePoints();
        Life life2 = new Life(300, 300);
        life2.setHeight(100);
        life2.setWidth(100);

        player.setX(400);
        player.setY(200);

        glh.checkPLifeCollision(player, life2);

        userEventHandler.handle(keyEvent1);
        Assert.assertEquals(lifePoints - Life.getDamage(), player.getLifePoints());
    }

    @Test
    public void shieldUsageTest() {

        int initialShieldsCount = 0;
        int initialLifePoints = player.getLifePoints();

        Shield shield1 = new Shield(100, 100);
        glh.checkPShieldCollision(player, shield1);

        int shieldCount = player.getShieldsCount();

        Assert.assertEquals(initialShieldsCount + 1, player.getShieldsCount());
        EnemyBeam beam1 = new EnemyBeam(100, 100);

        KeyEvent keyEvent1 = new KeyEvent(KeyEvent.ANY, "", "", KeyCode.E, false, false, false, false);
        userEventHandler.handle(keyEvent1);

        Assert.assertEquals(shieldCount - 1, player.getShieldsCount());

        glh.checkBeamCollision(player, beam1);
        Assert.assertEquals(initialLifePoints - Math.floorDiv(beam1.getDamage(), 2), player.getLifePoints());

        player.setProtection(false);

        initialLifePoints = player.getLifePoints();

        glh.checkBeamCollision(player, beam1);
        Assert.assertEquals(initialLifePoints - beam1.getDamage(), player.getLifePoints());
    }

}
