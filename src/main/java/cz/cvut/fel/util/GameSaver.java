package cz.cvut.fel.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.cvut.fel.Model.Game;

import java.io.FileWriter;
import java.io.IOException;

public class GameSaver {
    final Game game;

    public GameSaver(Game game) {
        this.game = game;
    }

    public void save() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Game.class, new GameSerializer()).create();
        final String gameData = gson.toJson(game);
        try (FileWriter fw = new FileWriter("last_game.json")) {
            fw.write(gameData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
