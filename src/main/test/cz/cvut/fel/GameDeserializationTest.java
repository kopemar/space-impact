package cz.cvut.fel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import cz.cvut.fel.Model.Game;
import cz.cvut.fel.util.GameDeserializer;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GameDeserializationTest {

    @Test (expected = JsonParseException.class)
    public void deserializeTest_non_valid_json() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Game.class, new GameDeserializer()).create();
        try (BufferedReader reader = new BufferedReader(new FileReader("deserialize_wrong_test_data.json"))) {
            Game g = gson.fromJson(reader, Game.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deserializeTest_valid_json() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Game.class, new GameDeserializer()).create();
        try (BufferedReader reader = new BufferedReader(new FileReader("deserialize_ok_test_data.json"))) {
            Game g = gson.fromJson(reader, Game.class);
            Assert.assertEquals(-1, g.getCurrentX());
            Assert.assertEquals(275, g.getgPlayer().getY());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
