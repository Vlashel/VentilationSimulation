package com.vlashel.vent;

import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

/**
 * @author vshel
 * @version 1.0
 * @since 1/26/2015.
 */
public class PathLine extends Path {

    private FadeTransition fadeTransition;

    public PathLine(Position choice, Color color) {
        if (choice.equals(Position.UP)) {
            createPathUp(color);
        } else {
            createPathDown(color);
        }
        initFadeTransition();
    }

    public void initFadeTransition() {
        this.fadeTransition = FadeTransitionBuilder.create()
                .duration(Duration.millis(700))
                .node(this)
                .fromValue(1)
                .toValue(0)
                .cycleCount(Timeline.INDEFINITE)
                .autoReverse(true)
                .build();
        fadeTransition.play();
    }

    public void createPathUp(Color color) {
        buildPath(color);
    }

    public void createPathDown(Color color) {
        buildPath(color);

        this.setRotate(180);
        this.setScaleX(-1);
        this.setTranslateY(12);
    }

    private void buildPath(Color color) {
        this.setStroke(color);
        this.setStrokeWidth(5);
        this.getElements().add(new MoveTo(200, 383));
        this.getElements().add(new HLineTo(275));
        this.getElements().add(new VLineTo(120));
        this.getElements().add(new HLineTo(200));
        this.getElements().add(new LineTo(225, 105));
        this.getElements().add(new LineTo(200, 120));
        this.getElements().add(new LineTo(225, 135));
    }

    public FadeTransition getFadeTransition() {
        return fadeTransition;
    }
}
