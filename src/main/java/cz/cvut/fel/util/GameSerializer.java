package cz.cvut.fel.util;

import com.google.gson.*;
import cz.cvut.fel.Model.*;

import java.lang.reflect.Type;

public class GameSerializer implements JsonSerializer<Game> {
    private Game game;
    @Override
    public JsonElement serialize(Game src, Type typeOfSrc, JsonSerializationContext context) {
        game = src;
        JsonObject object = new JsonObject();
        object.addProperty("window_X", src.getWindowX());
        object.addProperty("window_Y", src.getWindowY());
        object.addProperty("lives", src.getLifeCount());
        object.addProperty("life_worth", src.getLifeWorth());
        object.addProperty("current_X", src.getCurrentX());
        object.addProperty("overall_X", src.getOverallX());
        object.addProperty("generation_X", src.getGenerationX());
        object.addProperty("generation_Y", src.getGenerationY());
        object.addProperty("remaining_enemies", src.getEnemiesRemaining());
        object.add("player", getPlayerSerialized(game.getgPlayer()));
        object.add("enemies", getEnemiesSerialized());
        object.add("shields", serializeShields());
        object.add("hearts", serializeLives());
        object.add("player_beams", serializePlayerBeams());
        object.add("enemy_beams", serializeEnemyBeams());
        return object;
    }

    private JsonElement serializeEnemyBeams() {
        JsonArray object = new JsonArray();
        for (EnemyBeam beam : game.getEnemyBeamManager().getAll()) {
            object.add(serializeBeam(beam));
        }
        return object;
    }

    private JsonArray getEnemiesSerialized() {
        JsonArray object = new JsonArray();
        game.getEnemies().forEach((k, v)-> object.add(serializeEnemy(k.getX(), k.getY(), v)));
        return object;
    }

    private JsonElement getPlayerSerialized(Player player) {
        JsonObject object = new JsonObject();
        object.addProperty("life_points", player.getLifePoints());
        object.addProperty("max_lives", player.getMaxLifePoints());
        object.addProperty("current_X", player.getX());
        object.addProperty("current_Y", player.getY());
        object.addProperty("initX", player.getInitX());
        object.addProperty("initY", player.getInitY());
        object.addProperty("score", player.getScore());
        object.addProperty("shields", player.getShieldsCount());
        object.addProperty("protected", player.hasProtection());
        object.addProperty("last_shield", player.getLastShield());
        object.addProperty("healing", player.getHealingCount());
        return object;
    }

    private JsonElement serializeEnemy(int initX, int initY, Enemy enemy){
        JsonObject object = new JsonObject();
        object.addProperty("initX", initX);
        object.addProperty("initY", initY);
        object.addProperty("current_X", enemy.getX());
        object.addProperty("current_Y", enemy.getY());
        object.addProperty("in_game", enemy.isInGame());
        object.addProperty("id", enemy.getId());
        object.addProperty("strength", enemy.getStrength());
        object.addProperty("current_strength", enemy.getCurrentStrength());

        if (enemy.getSprite() != null) {
            String resource = enemy.getSprite().getResource() == null ?  "" : enemy.getSprite().getResource();
            object.addProperty("sprite_resource", resource);
        }

        return object;
    }

    private JsonArray serializeShields(){
        JsonArray object = new JsonArray();
        game.getShieldsGenerated().forEach((k, v)-> object.add(serializePickable(k.getX(), v)));
        return object;
    }

    private JsonArray serializeLives() {
        JsonArray object = new JsonArray();
        game.getLivesGenerated().forEach((k, v)-> object.add(serializePickable(k.getX(), v)));
        return object;
    }

    private JsonArray serializePlayerBeams() {
        JsonArray object = new JsonArray();
        for (PlayerBeam beam : game.getPlayerBeamPositionableManager().getAll()) {
            object.add(serializeBeam(beam));
        }
        return object;
    }

    private JsonElement serializePickable(int initX, Pickable shield) {
        JsonObject object = new JsonObject();
        object.addProperty("initX", initX);
        object.addProperty("initY", shield.getY());
        object.addProperty("playground_X", shield.getX());
        object.addProperty("picked", shield.isPicked());
        return object;
    }

    private JsonElement serializeBeam(Beam beam) {
        JsonObject object = new JsonObject();
        object.addProperty("current_X", beam.getX());
        object.addProperty("current_Y", beam.getY());
        object.addProperty("damage", beam.getDamage());
        object.addProperty("velocity", beam.getVelocity());
        return object;
    }

}
