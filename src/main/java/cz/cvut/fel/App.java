package cz.cvut.fel;

import cz.cvut.fel.Managers.*;
import cz.cvut.fel.Model.Game;
import cz.cvut.fel.util.GameLogger;
import cz.cvut.fel.Model.Player;
import cz.cvut.fel.util.Images;
import cz.cvut.fel.util.WrongUserInputException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

/**
 * JavaFX App
 */
public class App extends Application {
    private static final double W = 800, H = 600;
    private static Scene scene1;
    private static Stage stage;

    private static Group group;

    private static Group heartsSubgroup;
    private static Group errors;

    private static Group menuGroup;
    private static Group customFieldsGroup;

    private static Timeline timeline;
    private static Game game;
    private static HeartsManager heartsManager;

    private static final int DEFAULT_OVERALL_X = 2500;
    private static final int DEFAULT_GENERATION_X = 450;
    private static final int DEFAULT_GENERATION_Y = 150;
    private static final int DEFAULT_LIFE_COUNT = 3;
    private static final int DEFAULT_LIFE_WORTH = 75;
    private static final int DEFAULT_ENEMY_COUNT = 10;

    private static final int LABEL_X = 30;
    private static final int LABEL_WIDTH = 110;
    private static final int TF_CENTER = 45;
    private static int lblCounter = 0;
    private static final Color BG_COLOR = Color.rgb(148, 170, 100);
    private static final int PLAYER_STARTING_POSITION = 30;
    private static final int PLAY_BTN_X = 600;

    private static final int FPS = 60;
    private static final int SCREEN_TOP = 50;

    @Override
    public void start(Stage stage) {
        App.stage = stage;
        resetGame();
        firstMenu();
    }

    public static void resetGame() {
        group = new Group();
        menuGroup = new Group();
        customFieldsGroup = new Group();
        customFieldsGroup.setVisible(false);
        Group supergroup = new Group();
        supergroup.getChildren().addAll(group, menuGroup, customFieldsGroup);
        scene1 = new Scene(supergroup, W, H, BG_COLOR);

        heartsSubgroup = new Group();

        errors = new Group();

        GameLoopHandler.reset();

        customFieldsGroup.getChildren().add(errors);
    }

    private static void firstMenu() {
        new StateScreen(game, false);

        stage.setScene(scene1);
        stage.setTitle("Space Impact");
        stage.setResizable(false);
        stage.show();
    }

    public static void defaultGame() {
        setGame();
        try {
            game.setOverallX(DEFAULT_OVERALL_X);
            game.setGenerationX(DEFAULT_GENERATION_X);
            game.setGenerationY(DEFAULT_GENERATION_Y);
            game.setLifeCount(DEFAULT_LIFE_COUNT);
            game.setLifeWorth(DEFAULT_LIFE_WORTH);
            game.setEnemyCount(DEFAULT_ENEMY_COUNT);
        } catch (WrongUserInputException e) {
            e.printStackTrace();
        }
    }

    static void customizeGame(){
        Group menu = new Group();

        setGame();
        defaultGame();

        customFieldsGroup.getChildren().add(menu);

        lblCounter = 0;
        TextField enemyCount = addTF(LABEL_X, (lblCounter+1) * TF_CENTER, "Enemy count: ",
                "Number of enemies that will appear during game \n" +
                        "Recommended values: 10 - 20",
                menu);
        TextField overallX = addTF(LABEL_X, (lblCounter+1) * TF_CENTER, "Overall X: ",
                "Higher number means longer playground \n" +
                        "Recommended values: 2500 - 5000",
                menu);
        TextField generationX = addTF(LABEL_X, (lblCounter+1) * TF_CENTER, "Generation X: ",
                "How far away are enemies from each other (on x axis) \n" +
                        "Recommended values: 300 - 800",
                menu);
        TextField generationY = addTF(LABEL_X, (lblCounter+1) * TF_CENTER, "Generation Y: ",
                "How far away are enemies from each other (on y axis) \n" +
                        "Recommended values: 125 - 250",
                menu);
        TextField lifeCount = addTF(LABEL_X, (lblCounter+1) * TF_CENTER, "Life count: ",
                "Number of lives player has \n" +
                        "Recommended values: 3 - 6"
                ,menu);
        TextField lifeWorth = addTF(LABEL_X, (lblCounter+1) * TF_CENTER, "Life worth: ",
                "How much damage can one life take \n" +
                        "Recommended values: 75 - 150",
                menu);

        Button play = new Button("Play");
        play.setLayoutX(PLAY_BTN_X);
        play.setLayoutY((lblCounter+1) * TF_CENTER);
        menu.getChildren().add(play);
        AtomicReference<String> msg = new AtomicReference<>();
        // Done - validate user input (numbers only, overallX > generationX, screenHeight >= 2 * generationY)
        // Done - show errors in GUI
        // TODO - add Back button
        play.setOnAction(actionEvent -> {
            AtomicBoolean allOk = new AtomicBoolean(true);
            if (!overallX.getText().equals("") && allOk.get()) {
                try {
                    game.setOverallX(Integer.parseInt(overallX.getText()));}
                catch (NumberFormatException | WrongUserInputException e) {
                    allOk.set(false);
                    msg.set(e.getMessage());
                    e.printStackTrace();
                }
            }

            if (!generationX.getText().equals("") && allOk.get()) {
                try {
                    game.setGenerationX(Integer.parseInt(generationX.getText()));
                } catch (WrongUserInputException | NumberFormatException e) {
                    allOk.set(false);
                    msg.set(e.getMessage());
                    e.printStackTrace();
                }
            }

            if (!generationY.getText().equals("") && allOk.get()) {
                try {
                    game.setGenerationY(Integer.parseInt(generationY.getText()));
                } catch (WrongUserInputException | NumberFormatException e) {
                    allOk.set(false);
                    msg.set(e.getMessage());
                    e.printStackTrace();
                }
                }

            if (!lifeCount.getText().equals("") && allOk.get()) {
                try {
                    game.setLifeCount(Integer.parseInt(lifeCount.getText()));
                } catch (NumberFormatException | WrongUserInputException e) {
                    allOk.set(false);
                    msg.set(e.getMessage());
                    e.printStackTrace();
                }
            }

            if (!lifeWorth.getText().equals("") && allOk.get()) {
                try {
                    game.setLifeWorth(Integer.parseInt(lifeWorth.getText()));
                } catch (WrongUserInputException e) {
                    allOk.set(false);
                    msg.set(e.getMessage());
                    e.printStackTrace();
                }
            }

            if (!enemyCount.getText().equals("") && allOk.get()) {
                try {
                    game.setEnemyCount(Integer.parseInt(enemyCount.getText()));
                } catch (WrongUserInputException e) {
                    allOk.set(false);
                    msg.set(e.getMessage());
                    e.printStackTrace();
                }
            }
            if (allOk.get() && game.getOverallX() > game.getGenerationX()) {
                customFieldsGroup.getChildren().clear();
                group.setVisible(true);
                newGame();
            }
            else {
                showError(msg.get());
                GameLogger.getLOGGER().log(Level.WARNING, "Game could not be customized - user has set wrong input");
            }
        });

        stage.setScene(scene1);
        stage.setResizable(false);
        stage.show();
    }

    private static TextField addTF(int x, int y, String lblText, String hint, Group group) {
        lblCounter++;
        CustomSettingsField cf = new CustomSettingsField(lblText, hint, x, y, TF_CENTER, LABEL_WIDTH, group, BG_COLOR);

        return cf.getTextField();
    }

    private static void showError(String message) {
        errors.getChildren().clear();
        Text errormsg = new Text(LABEL_X, (lblCounter+1) * TF_CENTER, message);
        errors.getChildren().add(errormsg);
    }

    private static void setGame() {
        game = new Game();
        game.setCurrentX(-1);
        game.setWindowTop(App.getScreenTop());
        game.setWindowX((int) App.getSceneWidth());
        game.setWindowY((int) App.getSceneHeight());

        GameLoopHandler.setGame(game);
    }


    static void newGame(){
        resetGame();

        Player player = new Player(PLAYER_STARTING_POSITION, (int) (App.getSceneHeight() - App.getScreenTop()) / 2);

        game.getPlayerPositionableManager().add(player, true, Images.PLAYER);

        startUpTimeline();

        game.generatePickables();
        game.generateEnemies();
        game.setgPlayer(player);

        prepareStage();
    }

    public static void continueGame(Game cGame) {
        game = cGame;

        prepareSavedGame();
    }

    public static void startPresetGame(Game pGame) {
        resetGame();
        game = pGame;

        prepareSavedGame();
    }

    private static void prepareSavedGame() {
        GameLoopHandler.setGame(game);

        game.setWindowTop(App.getScreenTop());
        game.setWindowX((int) App.getSceneWidth());
        game.setWindowY((int) App.getSceneHeight());

        game.getPlayerPositionableManager().add(game.getgPlayer(), true, Images.PLAYER);
        prepareStage();
        startUpTimeline();
    }

    private static void prepareStage() {
        GameLoopHandler.setgPlayer(game.getgPlayer());

        heartsManager = new HeartsManager();

        GameLoopHandler.setActive(true);

        scene1.setOnKeyPressed(new UserEventHandler(game.getgPlayer(),false, game));

        stage.setScene(scene1);
        stage.setResizable(false);
        stage.show();
    }


    private static void startUpTimeline() {
        if (timeline == null) {
            GameLoopHandler gameLoopHandler = new GameLoopHandler();
            int duration = 1000 / FPS;
            final KeyFrame oneFrame = new KeyFrame(Duration.millis(duration), gameLoopHandler);
            timeline = new Timeline(oneFrame);
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }
    }

    public static void gameOver() {
        new StateScreen(game, true, "Game Over");
    }

    public static void winningScreen() {
        new StateScreen(game, true, "You won");
    }

    public static double getSceneWidth() {
        return scene1.getWidth();
    }

    public static double getSceneHeight() {return scene1.getHeight();}

    public static Group getGroup(){
        return group;
    }

    public static void main(String[] args) {
        launch();
    }

    public static int getFPS() {
        return FPS;
    }

    public static int getScreenTop() {
        return SCREEN_TOP;
    }

    public static Game getGame() {
        return game;
    }

    public static HeartsManager getHeartsManager() {
        return heartsManager;
    }

    public static Group getHeartsSubgroup() {
        return heartsSubgroup;
    }

    public static Group getMenuGroup() {
        return menuGroup;
    }

    public static Group getCustomFieldsGroup() {
        return customFieldsGroup;
    }
}