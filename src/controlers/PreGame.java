package controlers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PreGame {

    // Game Grid
    static int xVal;    //columns
    static int yVal;    //rows
    // Tiles parameter
    static double tileSize;
    static double tilesMargin;
    // Game thread + alert
    static Game game;

    // ---------------------------------------------------------------------

    final private String path = "C:/Users/Edwin/Desktop/GUI/ProjectMemoV2/src/memoimages";
    final Alert a = new Alert(Alert.AlertType.NONE);
    // screen dimension excluding windows bar
    final int screenWidth = (int) GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getWidth();
    final int screenHeight = (int) GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getHeight();
    // window dimmensions
    int gameWindowWidth;
    int gameWindowHeight;

    @FXML
    Button exit;
    @FXML
    Button newGame;
    @FXML
    Spinner<Integer> xValue;
    @FXML
    Spinner<Integer> yValue;
    @FXML
    VBox vBox;

    // ---------------------------------------------------------------------

    public void initialize() {
        CloseWindow.setGlobalEventHandler(vBox);
    }

    // Start Game
    public void startGame(int x, int y) throws IOException {
        // Set static game dimension
        xVal = x;
        yVal = y;

        setGameWindowDimensions();

        // New window
        Stage okno = (Stage) exit.getScene().getWindow();
        Parent structure = FXMLLoader.load(getClass().getResource("/fxml/game.fxml"));

        // Scene ( element; columns -> width; rows -> height )
        Scene startGameScene = new Scene(structure, gameWindowWidth, gameWindowHeight);

        okno.setX((screenWidth - gameWindowWidth) >> 1);
        okno.setY((screenHeight - gameWindowHeight - 20) >> 1);

        // Run new Window
        okno.setScene(startGameScene);

        // Run Game Thread
        game = new Game();
        startGameThread();
    }

    public void startGameThread() {
        game.start();
        System.out.println("\n-----------------");
        System.out.println("--> GAME - #" + game.getId());
    }

    public void setGameWindowDimensions() {
        // basics sizes of components
        tileSize = 100;
        tilesMargin = tileSize / 20;

        // window dimension calculated with basics sizes
        int tmpWidth = (int) (20 + ((tileSize)) * xVal + (tilesMargin * (xVal - 1)));
        int tmpHeight = (int) (135 + ((tileSize)) * yVal + (tilesMargin * (yVal - 1)));

        if (tmpWidth > screenWidth || tmpHeight > screenHeight) {
            double xDiff = screenWidth - tmpWidth, yDiff = screenHeight - tmpHeight;
            if (xDiff < yDiff && xDiff < 0) {
                tileSize = (int) ((screenWidth - 20) / (xVal + ((double) (xVal - 1) / 20)));
            } else if (yDiff < xDiff && yDiff < 0) {
                tileSize = (int) ((screenHeight - 145) / (yVal + ((double) (yVal - 1) / 20)));
            }
        }

        tilesMargin = tileSize / 20;

        gameWindowWidth = (int) (20 + (tileSize * xVal) + (tilesMargin * (xVal - 1)));
        gameWindowHeight = (int) (135 + (tileSize * yVal) + (tilesMargin * (yVal - 1)));

    }

    // Grid sizes exception
    public void checkTilesAmount() throws IOException {

        File imagesDirectory = new File(path); //Images folder
        File[] fileList = imagesDirectory.listFiles(); //Images list

        // amount of tiles
        int gridXY = xValue.getValue() * yValue.getValue();

        // ODD amounts of tiles
        if (gridXY % 2 != 0) {
            int result = xValue.getValue() * yValue.getValue();
            tilesAmountAlert("Tiles amount value should be divisible by 2!", xValue.getValue() + " * " + yValue.getValue() + " = " + (result - 1) + " + " + 1 + '.');
        }
        // not enought images
        else if ((gridXY / 2) > fileList.length) {
            tilesAmountAlert("Not enought images for selected size!", "Required at least " + gridXY / 2 + " for selected grid.");
        }
        // not enought tiles
        else if (gridXY < 4) {
            tilesAmountAlert("Wrong grid dimension!", "The product of two numbers should be at least 4.");
        }
        // no possible error
        else {
            startGame(xValue.getValue(), yValue.getValue());  //(columns,rows)
        }

    }

    private void tilesAmountAlert(String header, String textContent) {
        a.setAlertType(Alert.AlertType.WARNING);
        a.setTitle("ERROR");
        a.setHeaderText(header);
        a.setContentText(textContent);
        a.show();
    }

    // Main menu
    public void mainMenu() throws IOException {

        Stage okno = (Stage) exit.getScene().getWindow();
        Parent structure = FXMLLoader.load(getClass().getResource("/fxml/mainmenu.fxml"));

        Scene mainmenu = new Scene(structure, 300, 275);
        okno.setScene(mainmenu);
    }

    // Exit
    public void exit() {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }


}
