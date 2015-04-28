package com.vlashel.vent;

import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class TemperatureIndicator extends Label {
    public TemperatureIndicator() {
        this.setFont(Font.font(20));
        this.setMinSize(50, 50);
    }
}
