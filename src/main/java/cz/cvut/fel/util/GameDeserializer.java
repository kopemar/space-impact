package cz.cvut.fel.util;

import com.google.gson.*;
import cz.cvut.fel.Model.*;
import cz.cvut.fel.Sprite;

import java.lang.reflect.Type;

public class GameDeserializer implements JsonDeserializer<Game> {
    // TODO - fix enemy count
    @Override
    public Game deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Game game = new Game();

        try {
            game.setCurrentX(jsonObject.get("current_X").getAsInt());
            game.setWindowX(jsonObject.get("window_X").getAsInt());
            game.setWindowY(jsonObject.get("window_Y").getAsInt());
            game.setOverallX(jsonObject.get("overall_X").getAsInt());
            game.setGenerationX(jsonObject.get("generation_X").getAsInt());
            game.setGenerationY(jsonObject.get("generation_Y").getAsInt());
            game.setEnemyCount(jsonObject.getAsJsonArray("enemies").size());
            game.setEnemiesRemaining(jsonObject.get("remaining_enemies").getAsInt());
            game.setLifeCount(jsonObject.get("lives").getAsInt());
            game.setLifeWorth(jsonObject.get("life_worth").getAsInt());
            deserializeEnemies(jsonObject, game);
            deserializeShields(jsonObject, game);
            deserializeLives(jsonObject, game);
            deserializeBeams(jsonObject, game);

            JsonObject playerObject = jsonObject.getAsJsonObject("player");
            Player player = new Player(playerObject.get("current_X").getAsInt(), playerObject.get("current_Y").getAsInt(), game);
            game.setgPlayer(player);
            player.setLifePoints(playerObject.get("life_points").getAsInt());
            player.setShieldsCount(playerObject.get("shields").getAsInt());
            player.setProtection(playerObject.get("protected").getAsBoolean());
            player.setLastShield(playerObject.get("last_shield").getAsInt());
            player.setHealingCount(playerObject.get("healing").getAsInt());
        }
        catch (WrongUserInputException | NullPointerException e) {
            e.printStackTrace();
            throw new JsonParseException("The user input is not valid serialized Space Impact game");
        }
        return game;
    }

    private void deserializeEnemies(JsonObject jsonObject, Game game) {
        for (JsonElement o : jsonObject.getAsJsonArray("enemies")) {
            JsonObject asJsonObject = o.getAsJsonObject();
            Enemy enemy = new Enemy(asJsonObject.get("current_X").getAsInt(), asJsonObject.get("current_Y").getAsInt());
            enemy.setCurrentStrength(asJsonObject.get("current_strength").getAsInt());
            enemy.setInGame(asJsonObject.get("in_game").getAsBoolean());
            if (asJsonObject.get("sprite_resource") != null) {
                enemy.setSprite(new Sprite(enemy, asJsonObject.get("sprite_resource").getAsString()));
                System.out.println("resource " + enemy.getSprite().getResource());
            }

            game.addEnemy(asJsonObject.get("initX").getAsInt(), asJsonObject.get("initY").getAsInt(), enemy);
        }
    }

    private void deserializeShields(JsonObject jsonObject, Game game) {
        for (JsonElement o : jsonObject.getAsJsonArray("shields")) {
            JsonObject asJsonObject = o.getAsJsonObject();
            Shield shield = new Shield(asJsonObject.get("playground_X").getAsInt(), asJsonObject.get("initY").getAsInt());
            shield.setPicked(asJsonObject.get("picked").getAsBoolean());
            game.addShield(asJsonObject.get("initX").getAsInt(), asJsonObject.get("initY").getAsInt(), shield);
        }
    }
    private void deserializeLives(JsonObject jsonObject, Game game) {
        for (JsonElement o : jsonObject.getAsJsonArray("hearts")) {
            JsonObject asJsonObject = o.getAsJsonObject();
            Life life = new Life(asJsonObject.get("playground_X").getAsInt(), asJsonObject.get("initY").getAsInt());
            life.setPicked(asJsonObject.get("picked").getAsBoolean());
            game.addLife(asJsonObject.get("initX").getAsInt(), asJsonObject.get("initY").getAsInt(), life);
        }
    }

    private void deserializeBeams(JsonObject jsonObject, Game game) {
        for (JsonElement o : jsonObject.getAsJsonArray("player_beams")) {
            JsonObject PBjsonObject = o.getAsJsonObject();
            PlayerBeam beam = new PlayerBeam(PBjsonObject.get("current_X").getAsInt(), PBjsonObject.get("current_Y").getAsInt());
            try {
                game.getPlayerBeamPositionableManager().add(beam, true, Images.BEAM);
            } catch (Exception e) {
                game.getPlayerBeamPositionableManager().add(beam, false, null);
            }
        }

        for (JsonElement o : jsonObject.getAsJsonArray("enemy_beams")) {
            JsonObject EBJsonObject = o.getAsJsonObject();
            EnemyBeam pbeam = new EnemyBeam(EBJsonObject.get("current_X").getAsInt(), EBJsonObject.get("current_Y").getAsInt());
            try {
                game.getEnemyBeamManager().add(pbeam, true, Images.BEAM);
            } catch (Exception e) {
                game.getEnemyBeamManager().add(pbeam, false, null);
            }
        }
        }
    }
