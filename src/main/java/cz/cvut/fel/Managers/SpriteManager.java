package cz.cvut.fel.Managers;

import cz.cvut.fel.App;
import cz.cvut.fel.Model.Positionable;

import java.util.ArrayList;

public class SpriteManager<T extends Positionable> {

    public SpriteManager() { }

    private ArrayList<T> sprites = new ArrayList<>();

    /**
     * @param sprite - sprite to be added to GUI
     */
    public void addSprite(T sprite) {
        sprites.add(sprite);
        App.getGroup().getChildren().add(sprite.getSprite().getImageView());
    }

    /**
     * @param sprite - sprite to be deleted from GUI
     */
    public void deleteSprite(T sprite) {
        int index = sprites.indexOf(sprite);
        if (index != -1) {
            sprites.remove(index);
            App.getGroup().getChildren().remove(sprite.getSprite().getImageView());
        }
    }
}
