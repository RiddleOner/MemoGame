package controlers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Game extends Thread {

    String path = "/memoimages";

    // Time
    private static int memoTime;
    private static String currentMemoTime;
    private static int bonusPoints;
    private static String finaltime;

    private final MyRectangle[] tmpChecker = new MyRectangle[2];
    private int revealed = 0;           // currently reavel tiles - less or equal (2)
    private int successConuter = 0;     // good shots
    final int pairAmount = PreGame.xVal * PreGame.yVal / 2;

    // Game
    ArrayList<MyRectangle> tilesArrayList;  //Tiles
    @FXML
    VBox vBox;
    @FXML
    GridPane gridGame;

    @FXML
    Label labelTimeFXML;

    @FXML
    Button mainmenu;

    // Dev test
    Boolean boolShowPictures = false;
    @FXML
    Button showPictures;

    // ---------------------------------------------------------------------

    // Time label - TODO: 13.10.2021 Bind label value
    public void initialize() {
        memoTime = 0;
        bonusPoints = 0;
        finaltime = "88:88";

//        labelTimeFXML.textProperty().bind(currentMemoTime.);
//        labelTimeFXML.textProperty().bindBidirectional(currentMemoTime);

        CloseWindow.setGlobalEventHandler( vBox );
        buildGrid();

//        vBox.getChildren().add( labelTimeFXML );
//        labelTimeFXML.textProperty().bind( new StringBinding() {
//            {
//                super.bind( labelTimeFXML.textProperty() );
//            }
//
//            @Override
//            protected String computeValue() {
//                return finaltime;
//            }
//        } );
    }

    // Time counter - TODO: 01.07.2021 - Update label every second
    @Override
    public void run() {
        while ( !currentThread().isInterrupted() ) {
            try {
                System.out.print( "\r" + buildTimeFromInt( memoTime ) );
                currentMemoTime = buildTimeFromInt( memoTime );
                sleep( 1000 );
                memoTime++;

            } catch ( InterruptedException e ) {
                System.out.println( "--> Game thread - interupted! <--" );
            }

        }
    }

    public void stopRunning() {
        PreGame.game.stop();
        PreGame.game.interrupt();
    }

    //build time
    private String buildTimeFromInt( int timeStamp ) {
        int minutes = timeStamp / 60;
        int seconds = timeStamp % 60;

        return ( minutes < 10 ? "0" : "" ) + minutes + ":" + ( seconds < 10 ? "0" : "" ) + seconds;
    }

    //build time including bonus points
    private String buildTimeFromInt( int timeStamp, int bonusPoints ) {
        int calcBonusTime = ( Math.min( PreGame.xVal, PreGame.yVal ) ) * bonusPoints;
        int bonusTime = timeStamp - calcBonusTime;

        if ( timeStamp <= 0 ) {
            return "00:00";
        } else {
            return buildTimeFromInt( bonusTime );
        }
    }

    public String getFinalTime() {
        return finaltime;
    }

    public int getBonusPoints() {
        return bonusPoints;
    }

    // Build Game Grid
    private ArrayList<MyRectangle> prepairRectangleList( int x, int y ) {

        ArrayList<MyRectangle> tmpRectangleArray = new ArrayList<>();

        // x*y/2 - pairs amount
        for ( int i = 1; i <= ( x * y / 2 ); i++ ) {
            ImagePattern imgPattern = new ImagePattern( new Image( path + "/" + i + ".jpg" ) );

            MyRectangle shape1 = new MyRectangle( (int) ( PreGame.tileSize ), imgPattern );
            MyRectangle shape2 = new MyRectangle( (int) ( PreGame.tileSize ), imgPattern );

            tmpRectangleArray.add( shape1 );
            tmpRectangleArray.add( shape2 );
        }

        return tmpRectangleArray;
    }

    private void buildGrid() {
        tilesArrayList = prepairRectangleList( PreGame.xVal, PreGame.yVal );

        gridGame.alignmentProperty().setValue( Pos.CENTER );
        gridGame.setVgap( PreGame.tilesMargin );
        gridGame.setHgap( PreGame.tilesMargin );

        Collections.shuffle( tilesArrayList );

        //xVal - columns, yVal - rows
        for ( int i = 0; i < PreGame.xVal; i++ ) {
            for ( int j = 0; j < PreGame.yVal; j++ ) {

                MyRectangle shape = tilesArrayList.get( i + PreGame.xVal * j );
                setRectangleEvent( shape );

                // gridGame.add(shape, x-columns, y-rows, 1, 1);
                gridGame.add( shape, i, j, 1, 1 );
            }
        }

    }

    // Game functional events
    private void setRectangleEvent( MyRectangle shape ) {
        shape.setOnMouseClicked( event -> {
                    try {
                        revealEngine( shape );
                    } catch ( IOException e ) {
                        e.printStackTrace();
                    }
                }
        );
    }

    public void disableTilesStatus() {
        for ( MyRectangle myRec : tilesArrayList ) {
            if ( !myRec.ifMatched )
                myRec.tilesDisableStatus( !myRec.isDisable() );
        }
    }

    private void hideImages() {
        for ( int i = 0; i < gridGame.getRowCount(); i++ ) {
            for ( int j = 0; j < gridGame.getColumnCount(); j++ ) {

                MyRectangle tmp = (MyRectangle) gridGame.getChildren().get( j * gridGame.getRowCount() + i );

                if ( !tmp.isDisable() && !tmp.ifMatched ) {
                    tmp.setFill( tmp.getBaseColor() );
                }
            }
        }
    }

    private void waitTwoSecond() {
        Thread thread = new Thread( () -> {
            try {
                disableTilesStatus();
                sleep( 2000 );
                disableTilesStatus();

                Platform.runLater( this::hideImages );
            } catch ( InterruptedException e ) {
                e.printStackTrace();
            }
        } );
        thread.start();
    }

    // Rectangle reveal events ----------------------------------------------
    private void revealEngine( MyRectangle shape ) throws IOException {

        revealed++;
        shape.incrementTryAmount();

        if ( revealed == 1 ) {
            tmpChecker[0] = shape;
            tmpChecker[0].displayImage();

        } else if ( revealed == 2 ) {
            tmpChecker[1] = shape;
            tmpChecker[1].displayImage();

            if ( tmpChecker[0].getMemoImage() == tmpChecker[1].getMemoImage() ) {
                tmpChecker[0].goodShot();
                tmpChecker[1].goodShot();
                successConuter++;
//                System.out.print("\r-Pair: " + successConuter + "/" + pairAmount);
                if ( tmpChecker[0].getTryAmount() == 1 && tmpChecker[1].getTryAmount() == 1 ) {
                    bonusPoints++;
//                    System.out.println("Bonus point: " + bonusPoints);
                }
            } else {
                waitTwoSecond();
//                System.out.println( " -Different pictures - " + tmpChecker[0].getMemoImage().toString().substring( 18 ) + " vs " + tmpChecker[1].getMemoImage().toString().substring( 18 ) );
                tmpChecker[0].badShot();
                tmpChecker[1].badShot();
            }
            clearTemporary();

        }

        if ( successConuter == pairAmount ) {
            finaltime = buildTimeFromInt( memoTime, bonusPoints );
            openSaveResult();
            System.out.println( "\n-" );
            System.out.println( "Final time: " + finaltime + ", with " + bonusPoints + " bonus points!" );
        }

    }

    private void clearTemporary() {
        tmpChecker[0] = null;
        tmpChecker[1] = null;

        revealed = 0;
    }

    // Save result ----------------------------------------------------------
    private void openSaveResult() throws IOException {
        stopRunning();

        Scene aktualna = mainmenu.getScene();
        Stage aktualneokno = (Stage) aktualna.getWindow();
        Parent structure = FXMLLoader.load( getClass().getResource( "/fxml/saveResult.fxml" ) );
        Scene nowa = new Scene( structure, 250, 150 );

        Stage noweOkno = new Stage();
        noweOkno.setScene( nowa );
        noweOkno.setTitle( "Congratulation" );

        noweOkno.setX( aktualneokno.getX() + ( aktualna.getWidth() - nowa.getWidth() ) / 2 );
        noweOkno.setY( aktualneokno.getY() + ( aktualna.getHeight() - nowa.getHeight() ) / 2 );
        noweOkno.show();
    }

    // Main menu ------------------------------------------------------------
    public void mainMenu() throws IOException {
        stopRunning();

        Stage okno = (Stage) mainmenu.getScene().getWindow();
        Parent structure = FXMLLoader.load( getClass().getResource( "/fxml/mainmenu.fxml" ) );

        Scene mainmenu = new Scene( structure, 300, 275 );

        double xpos = ( Screen.getPrimary().getBounds().getWidth() - 300 ) / 2;
        double ypos = ( Screen.getPrimary().getBounds().getHeight() - 375 ) / 2;
        okno.setX( xpos );
        okno.setY( ypos );

        okno.setScene( mainmenu );
    }

    //Dev test --------------------------------------------------------------
    public void showPictures() {
        for ( int i = 0; i < PreGame.xVal; i++ ) {
            for ( int j = 0; j < PreGame.yVal; j++ ) {
                ( (MyRectangle) gridGame.getChildren().get( i * PreGame.yVal + j ) ).devDisplay( boolShowPictures );
            }
        }
        successConuter = 0;
        boolShowPictures = !boolShowPictures;
    }
}
