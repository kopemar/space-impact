package cz.cvut.fel.Model;

public class EnemyBeam extends Beam {
    private int damage = 50;

    public EnemyBeam(int x, int y) {
        super(x, y);
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
