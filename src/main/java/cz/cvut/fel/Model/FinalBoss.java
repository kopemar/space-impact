package cz.cvut.fel.Model;

public class FinalBoss extends Enemy {
    private int velocity = Interactive.validateVelocity(30);
    private int shootingPeriod = 100;

    private int currentStrength = 500;

    public FinalBoss(int x, int y) {
        super(x, y);
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public int getShootingPeriod() {
        return shootingPeriod;
    }

    public void setShootingPeriod(int shootingPeriod) {
        this.shootingPeriod = shootingPeriod;
    }

    @Override
    public int getCurrentStrength() {
        return this.currentStrength;
    }

    @Override
    public void damage(int value) {
        this.currentStrength -=value;
    }
}
