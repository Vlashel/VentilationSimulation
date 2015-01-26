package com.vlashel.vent;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author vshel
 * @version 1.0
 * @since 12/25/2014
 */
public class Ventilator extends ImageView {
    public static final double FAST_SPEED = 0.9;
    public static final double MEDIUM_SPEED = 0.3;
    private RotateTransition rotateTransition;
    private boolean isOn = true;

    public Ventilator(double translateX, double translateY) {
        super(new Image(Ventilator.class.getResource("images/propeller.png").toExternalForm()));
        rotateTransition = new RotateTransition();
        rotateTransition.setNode(this);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.setCycleCount(Timeline.INDEFINITE);
        rotateTransition.setByAngle(360);
        rotateTransition.setRate(Ventilator.MEDIUM_SPEED);
        rotateTransition.play();

        this.setTranslateX(translateX);
        this.setTranslateY(translateY);
    }

    public void changeRotationDirection() {
        double currentAngle = rotateTransition.getByAngle();
        rotateTransition.setByAngle(currentAngle * -1);
        rotateTransition.stop();
        rotateTransition.play();
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean isOn) {
        this.isOn = isOn;
    }

    public RotateTransition getRotateTransition() {
        return rotateTransition;
    }

    public void setRotateTransition(RotateTransition rotateTransition) {
        this.rotateTransition = rotateTransition;
    }
}
