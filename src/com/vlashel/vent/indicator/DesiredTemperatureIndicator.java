package com.vlashel.vent.indicator;


import javafx.scene.control.Label;
import javafx.scene.text.Font;


public class DesiredTemperatureIndicator extends Label {
    public DesiredTemperatureIndicator() {
        this.setFont(Font.font(30));
        this.setMinSize(50, 50);

        disable();
    }

    public void enable() {
        this.setDisable(false);
    }

    public void disable() {
        this.setDisable(true);
    }
}
