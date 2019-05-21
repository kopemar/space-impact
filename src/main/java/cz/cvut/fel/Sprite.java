package cz.cvut.fel;

import cz.cvut.fel.Model.Positionable;
import cz.cvut.fel.util.GameLogger;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprite {
    private String resource;
    private final Positionable OBJECT;
    private ImageView imageView;

    /**
     * @param OBJECT - to which Positionable does this Sprite belong
     * @param resource - resource that should correspond with filename of image in resources folder
     */
    public Sprite(Positionable OBJECT, String resource) {
        this.resource = resource;
        this.OBJECT = OBJECT;
        setImageView(resource, OBJECT.getX(), OBJECT.getY());
        OBJECT.setSprite(this);
        GameLogger.getLOGGER().info("Creating new sprite " + OBJECT.getSprite());
    }

    /**
     * method for initial setting of image
     * @param resource - resource of image (image file found in resources folder)
     * @param X - initial x position for image view
     * @param Y - initial y position for image view
     *
     */
    private void setImageView(String resource, int X, int Y){
        Image img = new Image(getClass().getResource(resource).toExternalForm());
        this.imageView = new ImageView(img);
        imageView.setX(X);
        imageView.setY(Y);
        OBJECT.setWidth((int) imageView.getImage().getWidth());
        OBJECT.setHeight((int) imageView.getImage().getHeight());
    }

    /**
     * update position (according to object's current position)
     */
    public void updatePosition() {
        imageView.setX(OBJECT.getX());
        imageView.setY(OBJECT.getY());
    }

    /**
     * @return ImageView of this sprite
     */
    public ImageView getImageView() {
        return this.imageView;
    }

    /**
     * @return Get String resource that should correspond with filename of image in resources folder.
     */
    public String getResource() {
        return resource;
    }

    /**
     * change resource of this Sprite's image
     * @param resource - new image resource
     *
     */
    public void changeResource(String resource) {
        this.resource = resource;
        Image img = new Image(getClass().getResource(this.resource).toExternalForm());
        this.imageView.setImage(img);
    }

    /**
     * @param invisible - whether the object should be invisible or not
     * Hide the Sprite object from playground.
     * (necessary for hiding beams and unpicked shields & lives when game is won - easier than deleting all sprites from playground)
     */
    public void makeInvisible(boolean invisible) {
        this.imageView.setVisible(!invisible);
    }

}