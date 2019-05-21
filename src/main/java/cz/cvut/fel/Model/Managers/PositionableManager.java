package cz.cvut.fel.Model.Managers;

import cz.cvut.fel.Managers.SpriteManager;
import cz.cvut.fel.Model.Positionable;
import cz.cvut.fel.Sprite;
import cz.cvut.fel.util.Images;
import cz.cvut.fel.util.PositionableNotFoundException;

import java.util.ArrayList;

public class PositionableManager<T extends Positionable> {
    private final int HEIGHT = 70;
    private final int WIDTH = 70;
    private ArrayList<T> positionables = new ArrayList<>();
    private SpriteManager<T> spriteManager = new SpriteManager<>();

    /**
     * @param positionable - which positionable is added to manager
     * @param hasSprite - whether should game add sprite (intended to be set to false if game has no GUI)
     * @param resource - sprite image resource
     */
    public void add(T positionable, boolean hasSprite, Images resource) {
        positionables.add(positionable);
        if (hasSprite) {
            if (positionable.getSprite() == null) {
                Sprite sprite = new Sprite(positionable, resource.getResource());
                positionable.setSprite(sprite);
            }
            spriteManager.addSprite(positionable);
        }
        else {
            positionable.setHeight(HEIGHT);
            positionable.setWidth(WIDTH);
        }
    }

    /**
     * @return ArrayList of all positionables in given manager
     */
    public ArrayList<T> getAll() {
        return positionables;
    }

    /**
     * @param positionable - positionable to be deleted from ArrayList
     * @throws PositionableNotFoundException if positionable was not found in ArrayList of positionables
     */
    public void delete(T positionable) throws PositionableNotFoundException {
        if (isInManager(positionable)) {
            positionables.remove(positionable);
        }
        else {
            throw new PositionableNotFoundException("Positionable " + positionable + " was not found");
        }
        if (positionable != null) {
            spriteManager.deleteSprite(positionable);
        }
    }

    /**
     * delete all positionables in given manager
     */
    public void deleteAll() {
        positionables.clear();
    }

    /**
     * @param positionable - some positionable of given type
     * @return true iff positionable is in given manager, else true
     */
    public boolean isInManager(T positionable) {
        return positionables.indexOf(positionable) != -1;
    }

}

