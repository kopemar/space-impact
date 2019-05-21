package cz.cvut.fel;

import cz.cvut.fel.Managers.GameLoopHandler;
import cz.cvut.fel.Model.Game;
import cz.cvut.fel.util.GameLogger;
import cz.cvut.fel.util.GameSaver;

import javafx.scene.control.Button;

public class PauseScreen extends StateScreen {

    public PauseScreen(Game game) {
        super(game, false);

        Button save = addNewButton("Save game", 3);

        Button resume = addNewButton("Resume game", 4);

        save.setOnAction(actionEvent -> {
            new GameSaver(game).save();
            GameLogger.getLOGGER().info("Attempt to save game");
        });

        resume.setOnAction(actionEvent -> {
            changeVisibilities();
            GameLoopHandler.setActive(true);
        });
    }
}
