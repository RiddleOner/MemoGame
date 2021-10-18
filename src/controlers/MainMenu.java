package controlers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


public class MainMenu {

    @FXML
    Button exit;

    public void initialize() {
        CloseWindow.setGlobalEventHandler(exit);
    }

    public void preGame() throws IOException {
        Stage okno = (Stage) exit.getScene().getWindow();
        Parent structure = FXMLLoader.load(getClass().getResource("/fxml/preGame.fxml"));

        Scene preGame = new Scene(structure, okno.getWidth(), okno.getHeight());
        okno.setScene(preGame);
    }

    public void openRecords() throws IOException {
        Scene aktualna = exit.getScene();
        Stage aktualneokno = (Stage) aktualna.getWindow();
        Parent structure = FXMLLoader.load(getClass().getResource("/fxml/records.fxml"));
        Scene nowa = new Scene(structure, 350, aktualna.getHeight());

        Stage noweOkno = new Stage();
        noweOkno.setScene(nowa);
        noweOkno.setTitle("Records");

        noweOkno.setX(aktualneokno.getX() + 5);
        noweOkno.setY(aktualneokno.getY() + 15);
        noweOkno.show();
    }

    public void exit() {
        System.out.println("EXIT");
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }

}
