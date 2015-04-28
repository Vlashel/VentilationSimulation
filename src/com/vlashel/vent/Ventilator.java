package com.vlashel.vent;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Ventilator extends ImageView {
    public static final double MEDIUM_SPEED = 0.3;
    private RotateTransition rotateTransition;
    private double angle = 360;

    public Ventilator() {
        super(new Image(Ventilator.class.getResource("images/propeller.png").toExternalForm()));
        createRotateTransition();
    }

    public Ventilator(double angle) {
        super(new Image(Ventilator.class.getResource("images/propeller.png").toExternalForm()));
        this.angle = angle;
        createRotateTransition();
    }


    public void stopVentilatorGradually() {
        createRotateTransition();
        rotateTransition.play();

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(500),
                        new KeyValue(rotateTransition.rateProperty(), 0.2)));
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(rotateTransition.rateProperty(), 0.1)));
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(1400),
                        new KeyValue(rotateTransition.rateProperty(), 0)));

        timeline.play();
        timeline.setOnFinished((ActionEvent event) -> timeline.stop());
    }

    public void createRotateTransition() {
        rotateTransition = new RotateTransition();
        rotateTransition.setNode(this);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.setCycleCount(Timeline.INDEFINITE);
        rotateTransition.setByAngle(angle);
        rotateTransition.setRate(Ventilator.MEDIUM_SPEED);
    }

    public Animation getAnimation() {
        return rotateTransition;
    }
}
