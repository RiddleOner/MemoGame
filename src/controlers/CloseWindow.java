package controlers;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class CloseWindow {

    public static void setGlobalEventHandler(Node root) {
        root.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            KeyCombination keyComb = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);

            if (keyComb.match(ev)) {
                ((Stage) root.getScene().getWindow()).close();
                ev.consume();
            }
        });
    }
}
