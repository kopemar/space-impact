package cz.cvut.fel.Model;

import cz.cvut.fel.App;
import cz.cvut.fel.util.GameLogger;
import cz.cvut.fel.util.Images;

// TODO - add JavaDoc :)
/**
 *
 */
public class Player extends Spaceship {
    private int score = 0;
    private Game game;
    private int maxLifePoints;
    private int initX;
    private int initY;
    private int shieldsCount;
    private int healingCount;
    private boolean protection = false;
    private int lastShield;

    private int livePoints;

    public Player(int x, int y) {
        super(x, y);
        this.initX = x;
        this.initY = y;
        this.game = App.getGame();
        setUp();
    }

    public Player(int x, int y, Game game) {
        super(x, y);
        this.game = game;
        setUp();
    }

    public void addScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return score;
    }

    public void setLifePoints(int livePoints) {
        this.livePoints = livePoints;
    }

    public int getLifePoints() {
        return livePoints;
    }

    public void damage(int damage) {
        if (damage >= livePoints){
            livePoints = 0;
        } else if (livePoints - damage > maxLifePoints) {
            livePoints = maxLifePoints;
        }
        else {
            livePoints -= damage;
        }
    }

    public int getMaxLifePoints() {
        return maxLifePoints;
    }

    public int getInitX() {
        return initX;
    }

    public void setInitX(int initX) {
        this.initX = initX;
    }

    public int getInitY() {
        return initY;
    }

    public void setInitY(int initY) {
        this.initY = initY;
    }

    public int getShieldsCount() {
        return shieldsCount;
    }

    public void setShieldsCount(int shieldsCount) {
        this.shieldsCount = shieldsCount;
    }

    public int getHealingCount() {
        return healingCount;
    }

    public void setHealingCount(int healingCount) {
        this.healingCount = healingCount;
    }

    /**
     * @param count - how many shields should be added
     */
    public void addShield(int count){
        if (shieldsCount + count >= 0) {
            this.shieldsCount += count;}
        else {
            shieldsCount = 0;
        }
    }

    /**
     * @param count - how many healing potions should be added
     */
    public void addHealing(int count){
        if (healingCount + count >= 0) {
            this.healingCount += count;}
        else {
            healingCount = 0;
        }
    }

    /**
     * @return Game`s CurrentX when last shield started being active
     * Important because protection should be only temporary
     */
    public int getLastShield() {
        return lastShield;
    }

    public void setLastShield(int lastShield) {
        this.lastShield = lastShield;
    }

    /**
     * Set whether the player has protection or not. Change image if protected and has Sprite.
     * @param protection - whether the player should be protected or not
     */
    public void setProtection(boolean protection) {
        super.setProtection(protection);
        setLastShield(game.getCurrentX());
        GameLogger.getLOGGER().warning("Shield protected since " + game.getCurrentX());
        this.protection = protection;
        if (this.getSprite() != null) {
            if (protection) {
                this.getSprite().changeResource(Images.PRO_PLAYER.getResource());
            }
            else {
                this.getSprite().changeResource(Images.PLAYER.getResource());
            }
        }
    }

    private void setUp() {
        shieldsCount = 0;
        healingCount = 0;
        this.setHeight(100);
        this.setWidth(100);
        maxLifePoints = (game.getLifeCount() + 1) * game.getLifeWorth();
        livePoints = maxLifePoints;
    }
}

