package cz.cvut.fel.Model;

public class Beam extends Interactive {
    private int velocity = validateVelocity(180);
    private int damage = 25;

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = validateVelocity(velocity);
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public Beam(int x, int y) {
        super(x, y);
    }
}
