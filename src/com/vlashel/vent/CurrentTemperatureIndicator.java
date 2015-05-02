package com.vlashel.vent;

import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class CurrentTemperatureIndicator extends Label {
    public CurrentTemperatureIndicator() {
        this.setFont(Font.font(20));
        this.setMinSize(50, 50);
    }
}
