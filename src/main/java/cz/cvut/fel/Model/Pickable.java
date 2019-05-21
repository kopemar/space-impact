package cz.cvut.fel.Model;

public class Pickable extends Interactive {
    private boolean picked = false;

    public Pickable(int x, int y) {
        super(x, y);
    }

    public boolean isPicked() {
        return picked;
    }

    public void setPicked(boolean picked) {
        this.picked = picked;
    }
}
