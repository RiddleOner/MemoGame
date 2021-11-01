package controlers;

import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class SaveResult {

    @FXML
    VBox vBox;
    @FXML
    TextField name;
    @FXML
    Button save;

    // ---------------------------------------------------------------------

    public void initialize() {
        CloseWindow.setGlobalEventHandler( save );
        onStart();
    }

    public void saveToFile() throws IOException {

        //New File
        String tmpPath = "./src/data/records.txt";
        File file = new File( tmpPath );

        String allSavedRecords = "";

        if ( !file.isFile() ) {
            file.createNewFile();
        }
        // Read - current records
        else {
            Scanner sc = new Scanner( file );
            StringBuilder sb = new StringBuilder();

            while ( sc.hasNextLine() ) {
                sb.append( sc.nextLine() ).append( "\n" );
            }
            allSavedRecords = sb.toString();
            sc.close();
        }
        // Write - newest on top
        PrintWriter pwriter = new PrintWriter( file );
        pwriter.println( "Time: " + PreGame.game.getFinalTime() + ", Bonus points: " + PreGame.game.getBonusPoints() + ", Size: " + PreGame.xVal + "x" + PreGame.yVal + ", Player: " + name.getText() );

        // Write - everything else
        if ( !allSavedRecords.isEmpty() ) {
            pwriter.print( allSavedRecords );
        }
        pwriter.close();

        Stage stage = (Stage) save.getScene().getWindow();
        stage.close();
    }

    public void onStart() {
        RotateTransition rt = new RotateTransition( Duration.seconds( 0.25 ), vBox );
        RotateTransition rt2 = new RotateTransition( Duration.seconds( 0.25 ), vBox );

        rt.setFromAngle( -3 );
        rt.setToAngle( 3 );

        rt2.setFromAngle( 3 );
        rt2.setToAngle( 0 );

        SequentialTransition st = new SequentialTransition( rt, rt2 );
        st.play();
    }

}
