package cz.cvut.fel.Model;

import cz.cvut.fel.App;
import cz.cvut.fel.Managers.GameLoopHandler;

class Interactive extends Positionable {
    /**
     * @param velocity - velocity intended to be set
     * @return nearest valid value
     */
    static int validateVelocity(int velocity) {
        if (velocity* GameLoopHandler.getSlowFactor() % App.getFPS() != 0) {
            velocity = (int) Math.round(velocity*0.1) * App.getFPS()/GameLoopHandler.getSlowFactor();
        }
        return velocity;
    }

    Interactive(int x, int y) {
        super(x, y);
    }

}
