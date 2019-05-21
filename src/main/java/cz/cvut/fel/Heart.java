package cz.cvut.fel;

import cz.cvut.fel.util.Images;
import javafx.scene.image.Image;

public class Heart {
    private boolean full;
    private Image image;
    private final Image FULL_IMG = new Image(getClass().getResource(Images.FULL_HT.getResource()).toExternalForm());
    private final Image EMPTY_IMG = new Image(getClass().getResource(Images.EMPTY_HT.getResource()).toExternalForm());

    public Heart(boolean full) {
        this.full = full;
        updateImage();
    }

    private void updateImage() {
        if(full) {
            image = FULL_IMG;
        }
        else {
            image = EMPTY_IMG;
        }
    }

    public Image getImage() {
        return image;
    }

    public void setFull(boolean full) {
        this.full = full;
        updateImage();
    }
}
