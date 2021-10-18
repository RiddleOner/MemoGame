package controlers;

import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class MyRectangle extends Rectangle {
    private final Paint baseColor = Color.web( "0xAAD3E6FF" );
    private final String baseStroke = "0x64A0FA";
    private final ImagePattern memoImage;
    public boolean ifMatched;
    private int tryAmount;

    public MyRectangle( int dimension, ImagePattern memoImage ) {
        super( dimension, dimension );

        int arcDim = dimension / 10;
        this.setArcWidth( arcDim );
        this.setArcHeight( arcDim );

        this.setFill( baseColor );
        this.memoImage = memoImage;

        tryAmount = 0;
        ifMatched = false;

        setEvents();
    }

    // Basic funcionality
    public void displayImage() {
        this.setFill( memoImage );
        tilesDisableStatus( true );
    }

    public void goodShot() {
        this.setOpacity( 0.50 );
        ifMatched = true;
    }

    public void hideImage() {
        this.setFill( baseColor );
        tilesDisableStatus( false );
    }

    public void badShot() {
        this.setOpacity( 1 );
        tilesDisableStatus( false );
    }

    public void tilesDisableStatus( boolean bool ) {
        this.setDisable( bool );
    }

    // Events
    private void setEvents() {
        this.setOnMouseEntered( event -> hoverOn() );
        this.setOnMouseExited( event -> hoverOff() );
    }

    private void hoverOn() {
        this.setStroke( Color.web( baseStroke, 1.0 ) );
        this.setStrokeType( StrokeType.INSIDE );
        this.setStrokeWidth( 2.05 );
    }

    private void hoverOff() {
        this.setStroke( Color.web( baseStroke, 0.0 ) );
    }

    // Try amount
    public void incrementTryAmount() {
        tryAmount++;
    }

    public int getTryAmount() {
        return tryAmount;
    }

    // Memo image
    public ImagePattern getMemoImage() {
        return memoImage;
    }

    public Paint getBaseColor() {
        return baseColor;
    }

    @Override
    public String toString() {
        return "MyRectangle{" + this.getClass() + " - memoImage=" + memoImage +
                "} " + baseColor.toString();
    }

    // Dev Tools
    public void devDisplay( Boolean tmpBool ) {
        this.setOpacity( 1 );
        ifMatched = false;

        if ( !tmpBool ) {
            this.setFill( memoImage );
            this.tilesDisableStatus( true );
        } else {
            this.setFill( baseColor );
            tilesDisableStatus( false );
        }
    }

}
