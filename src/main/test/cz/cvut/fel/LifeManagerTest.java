package cz.cvut.fel;


import cz.cvut.fel.Model.Game;
import cz.cvut.fel.Model.Managers.PlayerLivesManager;
import cz.cvut.fel.Model.Player;
import cz.cvut.fel.util.WrongUserInputException;
import org.junit.Assert;
import org.junit.Test;

public class LifeManagerTest {
    @Test
    public void lifeCountTest() {
        int initLifeCount = 3;
        int lifeWorth = 100;
        Game game = new Game();
        try {
            game.setLifeCount(initLifeCount);
            game.setLifeWorth(lifeWorth);
        } catch (WrongUserInputException e) {
            e.printStackTrace();
        }

        Player player = new Player(100, 100, game);

        PlayerLivesManager plm = new PlayerLivesManager(player, game);
        int realHeartsCount = plm.getHeartsCount();

        Assert.assertEquals(initLifeCount, realHeartsCount);

        player.damage(lifeWorth - 1);
        int realHeartsCount2 = plm.getHeartsCount();
        Assert.assertEquals(initLifeCount, realHeartsCount2);

        player.damage(1);
        int realHeartsCount3 = plm.getHeartsCount();
        Assert.assertEquals(initLifeCount, realHeartsCount3);

        player.damage(1);
        int realHeartsCount4 = plm.getHeartsCount();
        Assert.assertEquals(initLifeCount - 1, realHeartsCount4);
    }
}
