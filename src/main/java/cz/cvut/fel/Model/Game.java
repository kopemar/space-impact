package cz.cvut.fel.Model;

import cz.cvut.fel.Managers.CollisionDetector;
import cz.cvut.fel.Model.Managers.PositionableManager;
import cz.cvut.fel.util.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@SuppressWarnings("ALL")
public class Game {
    // Done - let user set game variables
    // Done - load game from file
    // Done - pickables

    /**
     * currentX - variable updated on every frame (1 added by default)
     */
    private int currentX = -1;

    /**
     * overallX - canvas size
     */
    private int overallX;

    /**
     * lifeCount - number of player's lives
     */
    private int lifeCount;

    /**
     * lifeWorth - damage points to lose one life
     */
    private int lifeWorth;

    /**
     * generationX - enemies appear only when currentX is equal to n * generationX (n is a whole number)
     */
    private int generationX;

    /**
     * generationY - enemies' Y is equal to n * generationY (n is a whole number)
     */
    private int generationY;

    /**
     * enemyCount - count of regular enemies that appear during gametime
     */
    private int enemyCount;

    /**
     * enemiesRemaining - number of enemies that were not destroyed... yet
     */
    private int enemiesRemaining = enemyCount;

    /**
     * gPlayer - game player connected with this exact game (important for serialization)
     */
    private Player gPlayer;

    private CollisionDetector collisionDetector;

    private int windowX;

    private int windowY;

    private int windowTop;

    @SuppressWarnings("FieldCanBeLocal")
    private final int MAX_PICKABLE_COUNT = 10;

    private final PositionableManager<EnemyBeam> enemyBeamPositionableManager = new PositionableManager<>();
    private final PositionableManager<Player> playerPositionableManager = new PositionableManager<>();
    private final PositionableManager<Life> lifePositionableManager = new PositionableManager<>();
    private final PositionableManager<Shield> shieldPositionableManager = new PositionableManager<>();
    private final PositionableManager<PlayerBeam> playerBeamPositionableManager = new PositionableManager<>();
    private final PositionableManager<Enemy> enemyPositionableManager = new PositionableManager<>();

    /**
     * HashMap of all enemies that will appear during game
     */
    private Map<Coordinates, Enemy> enemies = new HashMap<>();

    private Map<Coordinates, Life> lives = new HashMap<>();

    private Map<Coordinates, Shield> shields = new HashMap<>();

    /**
     * @param count - number of enemies
     * @return true if fits the generation X, overall X and generation Y given rules, else false
     */
    private int validateEnemyCount(int count) {
        int max = Math.floorDiv(getOverallX(), generationX) *
                Math.floorDiv(windowY - windowTop, generationY);
        if (count > max || count <= 0) return max;
        return count;
    }

    public int getOverallX() {
        return overallX;
    }

    /**
     * @param overallX - whole playground width
     * @throws WrongUserInputException - input is smaller than zero
     */
    public void setOverallX(int overallX) throws WrongUserInputException {
        if (overallX > 0) {
            this.overallX = overallX; }
        else {
            throw new WrongUserInputException("Overall X has to be greater than zero");
        }
    }

    private int getEnemyCount() {
        return enemyCount;
    }

    /**
     * @param enemyCount - number of enemies to be in game
     * @throws WrongUserInputException - throw if given enemy count is higher than game settings allow
     */
    public void setEnemyCount(int enemyCount) throws WrongUserInputException {
        if (enemyCount <= validateEnemyCount(enemyCount)) {
            this.enemyCount = enemyCount;
            setEnemiesRemaining(enemyCount);}
        else {
            throw new WrongUserInputException("Could not set enemy count to " + enemyCount + ", maximal value is " + validateEnemyCount(enemyCount));
        }

    }

    public void setEnemiesRemaining(int count) {
        this.enemiesRemaining = count;
    }
    /**
     * Add enemies to HashMap enemies - render random positions (but check if the position is not taken already)
     */
    public void generateEnemies() {
        int count = validateEnemyCount(getEnemyCount());
        try {
            setEnemyCount(count);
        } catch (WrongUserInputException e) {
            e.printStackTrace();
        }
        GameLogger.getLOGGER().info("Generating enemies - count has been set to " + count);
        while (enemies.size() < count) {
            int x = getRandomX();
            int y = getRandomY();
            Enemy enemy = new Enemy(windowX, y);
            addEnemy(x, y, enemy);
        }
    }

    // TODO - fix overlapping of Shields / Lives
    /**
     * randomly generate Pickables (based on game settings)
     */
    public void generatePickables() {
        int count = new Random().nextInt(validateEnemyCount(MAX_PICKABLE_COUNT));
        GameLogger.getLOGGER().info("Generating pickables - count has been set to " + count);
        while (lives.size() + shields.size() < count) {
            int x = getRandomX();
            int y = getRandomY();
            int factor = (new Random().nextInt(2)+ 1);
            if (factor == 1) {
                Life life = new Life(rndX(), y);
                lives.put(new Coordinates(x, 100), life);}
            else{
                Shield shield = new Shield(rndX(), y);
                shields.put(new Coordinates(x, 100), shield);
            }
        }
    }

    public Map<Coordinates, Enemy> getEnemies() {
        return enemies;
    }

    public Map<Coordinates, Life> getLivesGenerated() {
        return lives;
    }

    public Map<Coordinates, Shield> getShieldsGenerated() {
        return shields;
    }

    public void setCurrentX(int currentX) {
        this.currentX = currentX;
    }

    public int getCurrentX() {
        return currentX;
    }

    public void addEnemy(int x, int y, Enemy enemy) {
        enemies.put(new Coordinates(x, y), enemy);
    }

    public void addShield(int x, int y, Shield shield) {
        shields.put(new Coordinates(x, y), shield);
    }

    public void addLife(int x, int y, Life life) {
        lives.put(new Coordinates(x, y), life);
    }

    public void addX(int value) {
        this.currentX += value;
    }

    public int getLifeCount() {
        return lifeCount;
    }

    public void setLifeCount(int lifeCount) throws WrongUserInputException {
        if (lifeCount > 0 && lifeCount < 19) {
            this.lifeCount = lifeCount;
        }
        else {
            throw new WrongUserInputException("Life count has to be between 1 and 18");
        }
    }

    public int getLifeWorth() {
        return lifeWorth;
    }

    public void setLifeWorth(int lifeWorth) throws WrongUserInputException {
        if (lifeWorth > 0) {
            this.lifeWorth = lifeWorth;
        }
        else {
            throw new WrongUserInputException("Life has to be worth more than zero");
        }
    }

    public int getGenerationX() {
        return generationX;
    }

    public void setGenerationX(int generationX) throws WrongUserInputException {
        if (generationX < getOverallX() && generationX > 0) {
            this.generationX = generationX;
        }
        else {
            throw new WrongUserInputException("Could not set generation X - has to be smaller than overall X and greater than zero");
        }
    }

    public int getGenerationY() {
        return generationY;
    }

    public void setGenerationY(int generationY) throws WrongUserInputException {
        if (generationY * 1.5 < (windowY - windowTop)) {
        this.generationY = generationY; }
        else {
            throw new WrongUserInputException("Could not set generation Y - has to be greater than zero and slightly smaller than window height");
        }
    }

    public int getEnemiesRemaining() {
        return enemiesRemaining;
    }

    public Player getgPlayer() {
        return gPlayer;
    }

    public void setgPlayer(Player gPlayer) {
        this.gPlayer = gPlayer;
    }

    public void subtractEnemy(int count) {
        enemiesRemaining -= count;
    }

    private int getRandomX() {
        Random random = new Random();
        return random.nextInt(Math.floorDiv(overallX, generationX)) * generationX;
    }

    public int getRandomY() {
        Random random = new Random();
        return random.nextInt(Math.floorDiv(windowY - windowTop - 20, generationY)) * generationY + 20 + windowTop;
    }

    public int getWindowX() {
        return windowX;
    }

    public void setWindowX(int windowX) {
        this.windowX = windowX;
    }

    public int getWindowY() {
        return windowY;
    }

    public void setWindowY(int windowY) {
        this.windowY = windowY;
    }

    public int getWindowTop() {
        return windowTop;
    }

    public void setWindowTop(int windowTop) {
        this.windowTop = windowTop;
    }

    private int rndX() {
        return (new Random().nextInt(Math.floorDiv(getWindowX(), 100)) + 1) * 100 - 100;
    }

    public PositionableManager<EnemyBeam> getEnemyBeamManager() {
        return enemyBeamPositionableManager;
    }

    public PositionableManager<Player> getPlayerPositionableManager() {
        return playerPositionableManager;
    }

    public PositionableManager<Life> getLifePositionableManager() {
        return lifePositionableManager;
    }

    public PositionableManager<Shield> getShieldPositionableManager() {
        return shieldPositionableManager;
    }

    public PositionableManager<PlayerBeam> getPlayerBeamPositionableManager() {
        return playerBeamPositionableManager;
    }

    public PositionableManager<Enemy> getEnemyPositionableManager() {
        return enemyPositionableManager;
    }
}
