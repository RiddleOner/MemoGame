package controlers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Records {

    @FXML
    Button exit;
    @FXML
    VBox vBox;
    @FXML
    Text records;

    // ---------------------------------------------------------------------

    public void initialize() throws IOException {
        updateRecords();

        onStartAnimation();
        CloseWindow.setGlobalEventHandler(exit);
    }

    public void exit() {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }

    public void onStartAnimation() {
        FadeTransition fd = new FadeTransition(Duration.seconds(0.33), vBox);
        fd.setFromValue(1);
        fd.setToValue(0.50);
        fd.setCycleCount(4);
        fd.setAutoReverse(true);
        fd.play();
    }

    public void updateRecords() throws IOException {
        String tmpPath = "./src/data/records.txt";
        File file = new File(tmpPath);

        if (file.isFile()) {

            Scanner sc = new Scanner(file);
            StringBuilder sb = new StringBuilder();

            while (sc.hasNextLine()) {
                sb.append(sc.nextLine()).append("\n");
            }
            String tmpText = sb.toString();
            sc.close();

            setRecordsText(tmpText);
        } else {
            setRecordsText("");
        }

    }

    public void setRecordsText(String str) {
        if (!str.isEmpty())
            records.setText(str);
        else {
            records.setText("No records");
        }
    }
}
