package com.vlashel.vent;

import javafx.scene.control.Label;

/**
 * @author vshel
 * @version 1.0
 * @since 12/29/2014
 */
public class TemperatureIndicator extends Label {

    public TemperatureIndicator(double translateX, double translateY) {
        this.setTranslateX(translateX);
        this.setTranslateY(translateY);
    }
}
