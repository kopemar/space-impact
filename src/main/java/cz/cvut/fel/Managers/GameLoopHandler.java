package cz.cvut.fel.Managers;

import cz.cvut.fel.*;
import cz.cvut.fel.Model.*;

import cz.cvut.fel.util.GameLogger;
import cz.cvut.fel.util.Images;
import cz.cvut.fel.util.PositionableNotFoundException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.logging.Level;

/**
 * GameLoopHandler - EventHandler to redraw the canvas periodically
 */
public class GameLoopHandler extends CollisionDetector implements EventHandler<ActionEvent> {

    // TODO - What happens when ships meet?

    // Review - Fix boss rendering bug (sometimes shows earlier when last regular enemies are active)

    private static int counter = 0;

    private static Game game = App.getGame();
    private static FinalBoss boss;
    private static boolean active = true;
    private static Player gPlayer;

    private static boolean won = false;
    private static boolean bossInitiated = false;
    private static boolean worthChecking = true;
    private static final int HIT_POINTS = 10;
    private static final int KILL_POINTS = 50;
    private static final int WINNING_MOVEMENT_SPEED = 7;
    private static final int SHIELD_DURATION = 240;

    /**
     * slowFactor - slows animations (FPS reduction)
     */
    private static final int slowFactor = 2;
    private static final int BOSS_DISPERSION = 35;

    public GameLoopHandler() {
    }

    /**
     * check if it is time to show another enemy
     */
    private void checkForEnemies() {
        game.getEnemies().forEach((k, v)-> {
            if (k.getX() == game.getCurrentX()) {
                GameLogger.getLOGGER().info("New enemy has been added: " + v);
                game.getEnemyPositionableManager().add(v, true, Images.ENEMY);
                v.setInGame(true);
            }
            else if (v.isInGame() && !game.getEnemyPositionableManager().isInManager(v)) {
                GameLogger.getLOGGER().info("New enemy has been added: " + v);
                game.getEnemyPositionableManager().add(v, true, null);
            }
        });
    }

    /**
     * move all player beams, check for collision with enemy ships
     */
    private void updateBeams(){
        for (PlayerBeam pbs: game.getPlayerBeamPositionableManager().getAll()) {
            pbs.move(pbs.getVelocity()/App.getFPS(), 0);
            for (Enemy enemy : game.getEnemyPositionableManager().getAll()) {
                checkPlayerBeamCollision(enemy, pbs);
                if (didCollide(enemy, pbs)) {
                    GameLogger.getLOGGER().info(enemy + " has collided with player beam");
                    break;
                }
            }
        }
    }

    /**
     * move all enemy beams, call checkEnemyBeams()
     */
    private void updateEnemyBeams() {
        for (EnemyBeam pbs : game.getEnemyBeamManager().getAll()) {
            pbs.move(-pbs.getVelocity()/App.getFPS(), 0);
            checkEnemyBeams(gPlayer, pbs);
        }
    }

    /**
     * move all enemies and shoot (if it's time to do so)
     */
    private void updateEnemies(){
        for (Enemy enemy: game.getEnemyPositionableManager().getAll()) {
            if (outOfScreenCheck(enemy)) { deleteEnemy(enemy); }
            else {
            if (counter % slowFactor == 0) {
                enemy.move(- enemy.getVelocity() * GameLoopHandler.getSlowFactor()/App.getFPS(), (int) Math.round(2*Math.sin(enemy.getX()*0.1)));
            }
            if (counter % enemy.getShootingPeriod() == 0) { shoot(enemy, 0); }
            }
        }
    }

    /**
     * update Final Boss - end game if FB killed, call again if dismissed and shoot (if it's time to do so)
     */
    private void updateBoss() {
        if (boss.getCurrentStrength() <= 0) {
            deleteEnemy(boss);
            hideBeams();
            hidePositionables();
            won = true;
            active = false;
            return;
        }
        if (outOfScreenCheck(boss)) {
            callBoss();
        }

        if (counter % boss.getShootingPeriod() == 0) {
            shoot(boss, BOSS_DISPERSION);
            shoot(boss, -BOSS_DISPERSION);
        }
    }

    /**
     * check if it is time to show another pickable
     */
    private void checkForPickables() {
        game.getLivesGenerated().forEach((k, v)-> {
            if (k.getX() <= game.getCurrentX() && !v.isPicked() && v.getSprite() == null) {
                GameLogger.getLOGGER().info("New life has been added: " + v);
                game.getLifePositionableManager().add(v, true, Images.PICK_HT);
            }
        });

        game.getShieldsGenerated().forEach((k, v)-> {
            if (k.getX() <= game.getCurrentX() && !v.isPicked() && v.getSprite() == null) {
                GameLogger.getLOGGER().info("New shield has been added: " + v);
                game.getShieldPositionableManager().add(v, true, Images.PICK_SHIELD);
            }
        });
        // Done - add method to generate Pickables (random currentX, random position?)
    }

    /**
     * @param player - player of game
     * iterate through all Lives, then through all Shields and check if they collided
     */
    private void checkPickableCollision(Player player) {
        // Done - check for Pickalbe collision
        for (Life life : game.getLifePositionableManager().getAll() ) {
            checkPLifeCollision(player, life);
            if (didCollide(player, life)) {
                deletePickableLife(life);
            }
        }
        for (Shield shield : game.getShieldPositionableManager().getAll()) {
            checkPShieldCollision(player, shield);
            if (didCollide(player, shield)) {
                deletePickableShield(shield);
            }
        }
    }

    /**
     * shoot - create new beam
     * @param ship - enemy which shoots
     * @param dispersion - relative to center of image
     */
    private void shoot(Enemy ship, int dispersion) {
        int beamX = ship.getX();
        int beamY = ship.getY() + (int) Math.round(ship.getHeight()*0.4) + dispersion;
        EnemyBeam enemyBeam = new EnemyBeam(beamX, beamY);

        game.getEnemyBeamManager().add(enemyBeam, true, Images.BEAM);
    }

    /**
     * @param sprite - any bossSprite given
     * @return true iff object is out of screen, else false
     */
    private boolean outOfScreenCheck(Positionable sprite) {
        return ((double) sprite.getX() > App.getSceneWidth() || sprite.getX() < 0);
    }

    // DONE: make independent on sprites, take classes from model only
    /**
     * @param s - PlayerBeam
     * Checks if the beam has met any of the enemy ships.
     */
    private void checkPlayerBeamCollision(Enemy enemy, PlayerBeam s) {
        super.checkBeamCollision(enemy, s);
        if (didCollide(enemy, s)) {
            GameLogger.getLOGGER().info(enemy + " has collided with player beam");
            gPlayer.addScore(HIT_POINTS);
            deletePlayerBeam(s);
            if (enemy.getCurrentStrength() <= 0) {
                GameLogger.getLOGGER().info(enemy + " has been killed");
                deleteEnemy(enemy);
                gPlayer.addScore(KILL_POINTS);
            }
        }
    }

    /**
     * @param player - player of game
     * @param s - enemy beam
     * check if enemy beam collided with player
     */
    private void checkEnemyBeams(Player player, EnemyBeam s) {
        if (outOfScreenCheck(s)) {
            deleteEnemyBeam(s);
        }
        else {
            super.checkBeamCollision(player, s);
            if (didCollide(player, s)) {
                deleteEnemyBeam(s);
            }
        }
    }

    private void deleteEnemyBeam(EnemyBeam enemyBeam) {
        Runnable deletionTask = () -> {
            try {
                game.getEnemyBeamManager().delete(enemyBeam);
            }
            catch (PositionableNotFoundException e) {
                GameLogger.getLOGGER().warning(enemyBeam + " that game attempted to delete was not found.");
            }
            };
        Platform.runLater(deletionTask);
    }

    private void deletePlayerBeam(PlayerBeam playerBeam) {
        Runnable deletionTask = () -> {
            try {game.getPlayerBeamPositionableManager().delete(playerBeam);}
            catch (PositionableNotFoundException e) {
                GameLogger.getLOGGER().warning(playerBeam + " that game attempted to delete was not found.");
            }
        };
        Platform.runLater(deletionTask);
    }

    private void deletePickableShield(Shield shield) {
        Runnable deletionTask = () -> {
            try {
                game.getShieldPositionableManager().delete(shield);
            }
            catch(PositionableNotFoundException e){
                GameLogger.getLOGGER().warning(shield + " that game attempted to delete was not found.");
            }
        };
        Platform.runLater(deletionTask);
    }

    private void deletePickableLife(Life life) {
        Runnable deletionTask = () -> {
            try {
                game.getLifePositionableManager().delete(life);
            }
            catch(PositionableNotFoundException e){
                GameLogger.getLOGGER().warning(life + " that game attempted to delete was not found.");
            }
        };

        Platform.runLater(deletionTask);
    }

    private void deleteEnemy(Enemy enemy) {
        Runnable deletionTask = () -> {
        try {
            game.getEnemyPositionableManager().delete(enemy);
            game.subtractEnemy(1);
            enemy.setInGame(false);
            GameLogger.getLOGGER().info("Enemy is being deleted " + enemy + " - remains: " + game.getEnemiesRemaining());
        } catch (PositionableNotFoundException e) {
            GameLogger.getLOGGER().warning("Could not delete " + enemy);
        }
        };
        Platform.runLater(deletionTask);
    }

    private void hideBeams() {
        for (EnemyBeam e: game.getEnemyBeamManager().getAll()) {
            e.getSprite().makeInvisible(true);
        }
        for (PlayerBeam e: game.getPlayerBeamPositionableManager().getAll()) {
            e.getSprite().makeInvisible(true);
        }
    }

    private void hidePositionables() {
        for (Shield e: game.getShieldPositionableManager().getAll()) {
            e.getSprite().makeInvisible(true);
        }
        for (Life e: game.getLifePositionableManager().getAll()) {
            e.getSprite().makeInvisible(true);
        }
    }

    private void callBoss() {
        GameLogger.getLOGGER().log(Level.INFO, "The boss has been called");
        boss = new FinalBoss(game.getWindowX(), game.getRandomY());
        game.getEnemyPositionableManager().add(boss, true, Images.BOSS);
    }

    /**
     * @return slowFactor
     * Reduces framerate, allows for slower movement (objects could not move by less than 1px per frame [which is kinda fast] otherwise)
     */
    public static int getSlowFactor() {
        return slowFactor;
    }

    private void doUpdates() {
        App.getHeartsManager().updateLives(gPlayer);

        if (!bossInitiated) { checkForEnemies(); }
        if (bossInitiated) { updateBoss(); }

        if (game.getEnemiesRemaining() <= 0 && !bossInitiated) {
            bossInitiated = true;
            callBoss();
        }

        updateEnemyBeams();
        updateEnemies();
        updateBeams();
        checkPickableCollision(gPlayer);
        checkForPickables();

        counter++;
        game.addX(1);
    }

    private void activeShieldCheck() {
        if (gPlayer.hasProtection() && gPlayer.getLastShield() + SHIELD_DURATION <= game.getCurrentX()) {
            gPlayer.setProtection(false);
            GameLogger.getLOGGER().info("Shield is not active anymore");
        }
    }

    private void doWinningMovement() {
        if (!outOfScreenCheck(gPlayer)) {
            gPlayer.move(WINNING_MOVEMENT_SPEED, 0);
        }
        else {
            worthChecking = false;
            GameLogger.getLOGGER().info("Player has won with score of " + gPlayer.getScore());
            App.winningScreen();
        }
    }
    private void gameOverCheck() {
        if (gPlayer.getLifePoints() <= 0 && active) {
            GameLogger.getLOGGER().info("Game over - player is dead");
            App.gameOver();
            active = false;
        }
    }
    /**
     * @param actionEvent Event this Handler reacts to
     * Calls other methods that update game and playground...
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        gameOverCheck();
        if (gPlayer.getLifePoints() > 0 && active) {
            activeShieldCheck();
            doUpdates();
        }
        if (won && worthChecking) {
            doWinningMovement();
        }
    }

    /**
     * @param active - whether the GLH is active and should update the game
     */
    public static void setActive(boolean active) {
        GameLoopHandler.active = active;
    }

    /**
     * @return true if the GLH is active, false otherwise
     */
    static boolean isActive() {
        return active;
    }

    /**
     * @param game - game this GLH belongs to
     */
    public static void setGame(Game game) {
        GameLoopHandler.game = game;
    }

    // TODO - check if necessary :)
    /**
     * reset parameters of GLH
     */
    public static void reset() {
        won = false;
        boss = null;
        bossInitiated = false;
        active = true;
        counter = 0;
        worthChecking = true;
    }

    /**
     * @param gPlayer - game Player
     */
    public static void setgPlayer(Player gPlayer) {
        game.setgPlayer(gPlayer);
        GameLoopHandler.gPlayer = gPlayer;
    }
}
