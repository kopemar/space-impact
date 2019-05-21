package cz.cvut.fel.Model;

public class Spaceship extends Interactive {
    private int maxStrength = 50;
    private int currentStrength = maxStrength;
    private boolean protection;

    public Spaceship(int x, int y) {
        super(x, y);
    }

    public int getStrength() {
        return maxStrength;
    }

    public void setStrength(int strength) {
        this.maxStrength = strength;
    }

    public void damage(int value) {
        if (currentStrength > value) {
            currentStrength -= value;
        } else {
            currentStrength = 0;
        }
    }

    public int getCurrentStrength(){
        return currentStrength;
    }

    public void setCurrentStrength(int strength) {
        this.currentStrength = strength;
    }

    public void istaKill() {
        this.currentStrength = 0;
    }

    public boolean hasProtection() {
        return protection;
    }

    public void setProtection(boolean protection) {
        this.protection = protection;
    }
}
