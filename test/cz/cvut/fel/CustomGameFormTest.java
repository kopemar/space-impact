package cz.cvut.fel;

import cz.cvut.fel.Model.Game;
import cz.cvut.fel.util.WrongUserInputException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CustomGameFormTest {
    private Game game;
    @Before
    public void setUp() {
        game = new Game();
        try {
            game.setWindowY(600);
            game.setWindowTop(30);

            game.setOverallX(2500);
            game.setGenerationX(450);
            game.setGenerationY(100);
            game.setLifeCount(3);
            game.setLifeWorth(75);
            game.setEnemyCount(10);
        } catch (WrongUserInputException e) {
            e.printStackTrace();
        }
    }

    // PASSED but this leads to infinite loop - is something wrong with Game->generateEnemies()
    @Test
    public void infLoopCombination() {
        try {
            game.setOverallX(2);
            game.setGenerationX(1);
            game.setGenerationY(3);
            game.setLifeCount(1);
            game.setLifeWorth(1);
            game.setEnemyCount(1);
        } catch (WrongUserInputException e) {
            e.printStackTrace();
            Assert.fail();
        }
        Assert.assertEquals(1, game.getEnemiesRemaining());
        Assert.assertEquals(2, game.getOverallX());
    }

    @Test(expected = WrongUserInputException.class)
    public void setOverallX_less_than_zero() throws WrongUserInputException {
        game.setOverallX(-1);
    }

    @Test(expected = WrongUserInputException.class)
    public void setOverallX_equal_to_zero() throws WrongUserInputException {
        game.setOverallX(0);
    }

    @Test
    public void setOverallX_greater_than_zero() {
        int value = 1;
        try {
            game.setOverallX(value);
            Assert.assertEquals(value, game.getOverallX());
        } catch (WrongUserInputException e) {
            Assert.fail();
        }
    }

    @Test(expected = WrongUserInputException.class)
    public void setGenerationX_less_than_zero() throws WrongUserInputException {
        game.setGenerationX(-1);
    }

    @Test(expected = WrongUserInputException.class)
    public void setGenerationX_equal_to_overallX() throws WrongUserInputException {
        game.setGenerationX(2500);
    }

    @Test(expected = WrongUserInputException.class)
    public void setGenerationX_greater_than_overallX() throws WrongUserInputException {
        game.setGenerationX(2501);
    }

    @Test
    public void setGenerationX_ok() throws WrongUserInputException {
        int value = 1;
        game.setGenerationX(value);
        Assert.assertEquals(value, game.getGenerationX());
    }

    @Test(expected = WrongUserInputException.class)
    public void setGenerationY_max() throws WrongUserInputException {
        int value = 2*(game.getWindowY() - game.getWindowTop())/3;
        game.setGenerationY(value);
    }

    @Test(expected = WrongUserInputException.class)
    public void setGenerationY_more_than_max() throws WrongUserInputException {
        int value = 2*(game.getWindowY() - game.getWindowTop())/3 + 1;
        game.setGenerationY(value);
        Assert.assertEquals(value, game.getGenerationY());
    }

    @Test
    public void setGenerationY_less_than_max() throws WrongUserInputException {
        int value = 2*(game.getWindowY() - game.getWindowTop())/3 - 1;
        game.setGenerationY(value);
        Assert.assertEquals(value, game.getGenerationY());
    }

    @Test(expected = WrongUserInputException.class)
    public void setLifeCount_gt_max() throws WrongUserInputException {
        int value = 19;
        game.setLifeCount(value);
    }


    @Test(expected = WrongUserInputException.class)
    public void setEnemyCount_gt_max() throws WrongUserInputException {
        int value = Math.floorDiv(game.getWindowY() - game.getWindowTop(), game.getGenerationY()) * Math.floorDiv(game.getOverallX(), game.getGenerationX()) + 1;
        game.setEnemyCount(value);
    }

    @Test
    public void setEnemyCount_max() throws WrongUserInputException {
        int value = Math.floorDiv(game.getWindowY() - game.getWindowTop(), game.getGenerationY()) * Math.floorDiv(game.getOverallX(), game.getGenerationX());
        game.setEnemyCount(value);
        Assert.assertEquals(value, game.getEnemiesRemaining());
    }
}
