package cz.cvut.fel.Managers;

import cz.cvut.fel.App;
import cz.cvut.fel.Model.Game;
import cz.cvut.fel.Model.Life;
import cz.cvut.fel.util.GameLogger;
import cz.cvut.fel.Model.Player;
import cz.cvut.fel.Model.PlayerBeam;
import cz.cvut.fel.PauseScreen;
import cz.cvut.fel.util.Images;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * UserEventHandler is an EventHandler for KeyEvents done by User during the game - eg. moving, shooting.
 */
public class UserEventHandler implements javafx.event.EventHandler<KeyEvent> {
    private Player positionable;
    private final int MOVEMENT_VAL = 15;
    private double maxWidth, maxHeight, minHeight;
    private Game game;

    public UserEventHandler(Player positionable, Game game) {
        this.positionable = positionable;
        this.game = game;
        maxWidth = App.getSceneWidth();
        maxHeight = App.getSceneHeight();
        minHeight = App.getScreenTop();
    }

    public UserEventHandler(Player positionable, boolean hasApp, Game game) {
        this.game = game;
        if (hasApp) {
            new UserEventHandler(positionable, game);
        }
        else {
            this.positionable = positionable;
            maxWidth = 800;
            maxHeight = 600;
            minHeight = 40;
        }
    }

    private boolean outOfScreenCheckX(Player positionable, int newVal) {
        return ((double) positionable.getX() + newVal + positionable.getWidth() > maxWidth || positionable.getX() + newVal < 0);
    }

    private boolean outOfScreenCheckY(Player positionable, int newVal) {
        return ((double) positionable.getY() + newVal + positionable.getWidth() > maxHeight || positionable.getY() + newVal < minHeight);
    }

    private void shoot(Player sprite) {
        int beamX = sprite.getX() + sprite.getWidth();
        int beamY = sprite.getY() + (int) Math.round(sprite.getHeight()*0.4);
        PlayerBeam beam = new PlayerBeam(beamX, beamY);
        game.getPlayerBeamPositionableManager().add(beam, true, Images.BEAM);
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        if (GameLoopHandler.isActive()) {

            if (keyEvent.getCode() == KeyCode.W) {
                if (!outOfScreenCheckY(positionable, -MOVEMENT_VAL)) {
                    this.positionable.move(0, -MOVEMENT_VAL);
                }
            } else if (keyEvent.getCode() == KeyCode.S) {
                if (!outOfScreenCheckY(positionable, MOVEMENT_VAL)) {
                    this.positionable.move(0, MOVEMENT_VAL);
                }
            } else if (keyEvent.getCode() == KeyCode.A) {
                if (!outOfScreenCheckX(positionable, -MOVEMENT_VAL)) {
                    this.positionable.move(-MOVEMENT_VAL, 0);
                }
            } else if (keyEvent.getCode() == KeyCode.D) {
                if (!outOfScreenCheckX(positionable, MOVEMENT_VAL)) {
                    this.positionable.move(MOVEMENT_VAL, 0);
                }
            } else if (keyEvent.getCode() == KeyCode.SPACE) {
                shoot(positionable);
            } else if (keyEvent.getCode() == KeyCode.ESCAPE) {
                new PauseScreen(App.getGame());
                GameLoopHandler.setActive(false);
                // Done - pause menu (options - new game, save)
            }
            else if (keyEvent.getCode() == KeyCode.E) {
                if (positionable.getShieldsCount() > 0) {
                    GameLogger.getLOGGER().info("Player has activated shield");
                    positionable.addShield(-1);
                    positionable.setProtection(true);
                }
            }
            else if (keyEvent.getCode() == KeyCode.Q) {
                if (positionable.getHealingCount() > 0) {
                    GameLogger.getLOGGER().info("Player has healed himself");
                    positionable.addHealing(-1);
                    positionable.damage(Life.getDamage());
                }
            }
        }
    }

    public int getMOVEMENT_VAL() {
        return MOVEMENT_VAL;
    }
}
