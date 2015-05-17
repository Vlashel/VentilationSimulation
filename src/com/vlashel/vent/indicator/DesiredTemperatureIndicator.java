package com.vlashel.vent.indicator;


import javafx.scene.control.Label;
import javafx.scene.text.Font;


/**
 * @author Vlashel
 * @version 1.0
 * @since 02.05.2015.
 */
public class DesiredTemperatureIndicator extends Label {
    public DesiredTemperatureIndicator() {
        this.setFont(Font.font(30));
        this.setMinSize(50, 50);
    }
}
