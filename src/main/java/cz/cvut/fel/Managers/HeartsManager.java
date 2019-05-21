package cz.cvut.fel.Managers;

import cz.cvut.fel.App;
import cz.cvut.fel.Heart;
import cz.cvut.fel.Model.Game;
import cz.cvut.fel.Model.Managers.PlayerLivesManager;
import cz.cvut.fel.Model.Player;
import javafx.scene.image.ImageView;

public class HeartsManager {
    private static Game game = App.getGame();

    private boolean initiated = false;
    private static Heart[] hearts = new Heart[game.getLifeCount()];
    private static ImageView[] imageViews = new ImageView[game.getLifeCount()];

    public HeartsManager() {
        showLives();
    }

    private void showLives() {
        if (!initiated) {
            initiate();
        }
        int i = 0;
        for(Heart heart : hearts) {
            imageViews[i] = new ImageView();
            imageViews[i].setImage(heart.getImage());
            imageViews[i].setX(i*heart.getImage().getWidth() + (i+1)*3);
            imageViews[i].setY(3);
            i++;
        }
        App.getHeartsSubgroup().getChildren().clear();
        for (ImageView imageView : imageViews) {
            App.getHeartsSubgroup().getChildren().add(imageView);
        }
    }

    private void initiate() {
        for (int index = 0; index < hearts.length; index++) {
            hearts[index] = new Heart(true);
        }
        initiated = true;

        App.getGroup().getChildren().add(App.getHeartsSubgroup());
    }

    void updateLives(Player player) {
        PlayerLivesManager playerLivesManager = new PlayerLivesManager(player, game);
        int lives = playerLivesManager.getHeartsCount();
        for (int i = hearts.length - 1; i >= lives; i--){
            hearts[i].setFull(false);
        }
        for (int j = lives -1; j >= 0; j--) {
            hearts[j].setFull(true);
        }
        showLives();
    }

}
