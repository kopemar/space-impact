package cz.cvut.fel.Model;

public class Life extends Pickable {
    private static final int damage = -100;

    public Life(int x, int y) {
        super(x, y);
    }

    public static int getDamage() {
        return damage;
    }

}
