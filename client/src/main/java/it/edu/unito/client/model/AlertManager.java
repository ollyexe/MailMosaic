package it.edu.unito.client.model;

import it.edu.unito.eclientlib.AlertText;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public class AlertManager {

    public synchronized static void hideAlert(TextFlow alert, int duration) {
        Timeline idlestage = new Timeline(new KeyFrame(
                Duration.seconds(duration), event -> alert.setVisible(false))
        );
        idlestage.play();
    }

    public synchronized static void showAlert(TextFlow alert, AlertText alertText) {
        ((Text) alert.getChildren().get(0)).setText(alertText.toString());
        alert.setVisible(true);
    }

    public synchronized static void showTemporizedAlert(TextFlow alert, AlertText text, int duration){
        showAlert(alert, text);
        hideAlert(alert, duration);
    }
}
