import controlers.CloseWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class
Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("fxml/mainmenu.fxml"));
        primaryStage.setTitle("Memo");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        CloseWindow.setGlobalEventHandler(root);

    }

    public static void main(String[] args) {
        launch(args);
    }
}

// Author: Edwin Tarczyński © - 2021