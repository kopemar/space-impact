package cz.cvut.fel.Managers;

import cz.cvut.fel.Model.*;
import cz.cvut.fel.util.GameLogger;

import java.util.logging.Level;

public class CollisionDetector {
    /**
     * Check if Positionable (intended to be a spaceship) collided with another Positionable object (any Positionable)
     * @param ship - ship we check collision for
     * @param object - object that might have collided with the ship
     * @return true iff collision happened, else false
     */
    boolean didCollide(Positionable ship, Positionable object) {
        int shipX = ship.getX();
        int shipY = ship.getY();
        double shipHeight = ship.getHeight();
        double shipWidth = ship.getWidth();

        int objectX = object.getX();
        int objectY = object.getY();
        double objectHeight = object.getHeight();
        double objectWidth = object.getWidth();

        return objectX  <= shipX + shipWidth&&
                objectX + objectWidth >= shipX  &&
                objectY  <= shipY  + shipHeight &&
                objectY + objectHeight >= shipY;
    }

    /**
     * @param spaceship - any spaceship (Player or derived from Enemy)
     * @param beam - any beam (be it PlayerBeam or EnemyBeam)
     * Damage spaceship if it collided with beam
     */
    public void checkBeamCollision(Spaceship spaceship, Beam beam) {
        int damage = beam.getDamage();
        int PROTECTION_FACTOR = 2;
        if (didCollide(spaceship, beam)) {
            GameLogger.getLOGGER().log(Level.INFO, "Beam" + beam + " has has collided with " + spaceship);
            if (spaceship.hasProtection()) {
                damage = Math.floorDiv(damage, PROTECTION_FACTOR);
            }
            spaceship.damage(damage);
            GameLogger.getLOGGER().info("Spaceship has been damaged: " + damage + " points subtracted");
        }
    }

    /**
     * @param player - game Player
     * @param life - pickable life
     * Damage player if it collided with life (player`s healing count += 1)
     */
    public void checkPLifeCollision(Player player, Life life) {
        if (didCollide(player, life)) {
            GameLogger.getLOGGER().log(Level.INFO, "Player has picked life " + life);
            player.addHealing(1);
            life.setPicked(true);
        }
    }

    /**
     * @param player - game Player
     * @param shield - pickable shield
     * Add shield to player if it collided with shield (player's shield count += 1)
     */
    public void checkPShieldCollision(Player player, Shield shield) {
        if (didCollide(player, shield)) {
            GameLogger.getLOGGER().log(Level.INFO, "Player has picked shield " + shield);
            player.addShield(1);
            shield.setPicked(true);
        }
    }
}
