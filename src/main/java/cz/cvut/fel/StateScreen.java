package cz.cvut.fel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.cvut.fel.Model.Game;
import cz.cvut.fel.util.GameDeserializer;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;


class StateScreen {
    protected Game game;
    Group menu;

    final int BUTTON_HEIGHT = 50;
    final int STARTING_X = 300;

    protected StateScreen() {
    }

    StateScreen(Game game, boolean score) {
        this.game = game;
        showOptions();
        if (score) {
            showScore();
        }
    }

    StateScreen(Game game, boolean score, String message) {
        this.game = game;

        showOptions();
        showMessage(message);
        if (score) {
            showScore();
        }
    }

    private void showOptions() {
        App.getGroup().setVisible(false);

        menu =  App.getMenuGroup();

        Button defaultGame = addNewButton("New game with default settings", 5);

        Button customGame = addNewButton("New Game with custom settings", 6);

        Button load = addNewButton("Load preset game", 7);

        Button loadLastSaved = addNewButton("Load saved game", 8);

        defaultGame.setOnAction(actionEvent -> {
            changeVisibilities();
            App.defaultGame();
            App.newGame();
        });

        customGame.setOnAction(actionEvent -> {
            App.getMenuGroup().getChildren().clear();
            App.getCustomFieldsGroup().setVisible(true);
            App.customizeGame();
        });
        load.setOnAction(actionEvent-> {
            try {
                App.startPresetGame(readGame("saved_game.json"));
                changeVisibilities();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        );

        loadLastSaved.setOnAction(actionEvent -> {
            try {
                App.resetGame();
                App.continueGame(readGame("last_game.json"));
                changeVisibilities();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    protected Button addNewButton(String label, int Yfactor) {
        Button btn = new Button(label);
        btn.setLayoutX(STARTING_X);
        btn.setLayoutY(Yfactor * BUTTON_HEIGHT);
        menu.getChildren().add(btn);

        return btn;
    }

    private Game readGame(String file) throws IOException {
        BufferedReader fr = new BufferedReader (new FileReader(file));
        Gson gson = new GsonBuilder().registerTypeAdapter(Game.class, new GameDeserializer()).create();
        String mJSON = fr.readLine();
        Game g = gson.fromJson(mJSON, Game.class);
        fr.close();
        return g;
    }

    private void showScore() {
        Text score = new Text(30, 100, "Your score is " + game.getgPlayer().getScore());
        menu.getChildren().add(score);
    }

    void changeVisibilities() {
        App.getGroup().setVisible(true);
        App.getCustomFieldsGroup().getChildren().clear();
        App.getMenuGroup().getChildren().clear();
    }

    private void showMessage(String message) {
        Text msgText = new Text(30, 60, message);
        msgText.setStyle("-fx-font-size: 24pt");
        menu.getChildren().add(msgText);
    }
}
