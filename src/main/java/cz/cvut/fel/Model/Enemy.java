package cz.cvut.fel.Model;

public class Enemy extends Spaceship {
    private int velocity = Interactive.validateVelocity(30);
    private int shootingPeriod = 100;
    private boolean inGame = false;

    public Enemy(int x, int y) {
        super(x, y);
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = validateVelocity(velocity);
    }

    public int getShootingPeriod() {
        return shootingPeriod;
    }

    public void setShootingPeriod(int shootingPeriod) {
        this.shootingPeriod = shootingPeriod;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }
}
