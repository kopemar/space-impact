package cz.cvut.fel.Model.Managers;

import cz.cvut.fel.Model.Game;
import cz.cvut.fel.Model.Player;

public class PlayerLivesManager {
    private Game game;
    private Player player;
    private final int MAX_LIFE_COUNT;
    private final int LIFE_WORTH;
    private int heartsCount;

    public PlayerLivesManager (Player player, Game game) {
        this.game = game;
        this.player = player;
        MAX_LIFE_COUNT = game.getLifeCount();
        LIFE_WORTH = game.getLifeWorth();
        heartsCount = MAX_LIFE_COUNT;
    }

    /**
     * update heartsCount accordingly to current situation
     */
    private void update() {
        int playerLP = player.getLifePoints();
        heartsCount = Math.floorDiv(playerLP, LIFE_WORTH);
    }

    /**
     * @return return count of full hearts (to be shown in GUI)
     */
    public int getHeartsCount() {
        update();
        if (heartsCount > MAX_LIFE_COUNT) {
            return MAX_LIFE_COUNT;
        }
        return heartsCount;
    }

}
