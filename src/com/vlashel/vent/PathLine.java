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
            createHighPath(color);
        } else {
            createLowPath(color);
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

    public void createHighPath(Color color) {
        this.setStroke(color);
        this.setStrokeWidth(5);
        this.getElements().add(new MoveTo(220, 363));
        this.getElements().add(new HLineTo(280));
        this.getElements().add(new VLineTo(110));
        this.getElements().add(new HLineTo(240));
        this.getElements().add(new LineTo(245, 105));
        this.getElements().add(new MoveTo(240, 110));
        this.getElements().add(new LineTo(245, 115));

    }

    public void createLowPath(Color color) {
        this.setStroke(color);
        this.setStrokeWidth(5);
        this.getElements().add(new MoveTo(220, 363));
        this.getElements().add(new HLineTo(300));
        this.getElements().add(new VLineTo(110));
        this.getElements().add(new HLineTo(240));
        this.getElements().add(new LineTo(245, 105));
        this.getElements().add(new MoveTo(240, 110));
        this.getElements().add(new LineTo(245, 115));

        this.setRotate(180);
        this.setScaleX(-1);
        this.setTranslateY(8);

    }

    public FadeTransition getFadeTransition() {
        return fadeTransition;
    }
}
