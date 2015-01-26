package com.vlashel.vent;

import javafx.geometry.Orientation;
import javafx.scene.control.Slider;

/**
 * @author vshel
 * @version 1.0
 * @since 12/26/2014
 */
public class TemperatureSlider extends Slider {

    public TemperatureSlider(double translateX, double translateY) {
        super(10, 30, 20);
        this.setTranslateX(translateX);
        this.setTranslateY(translateY);
        this.setOrientation(Orientation.VERTICAL);
        this.setShowTickLabels(true);
        this.setShowTickMarks(true);
        this.setMajorTickUnit(5);
    }

}
