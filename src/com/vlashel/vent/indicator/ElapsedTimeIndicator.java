package com.vlashel.vent.indicator;


import javafx.scene.control.Label;
import javafx.scene.text.Font;

/**
 * @author Vlashel
 * @version 1.0
 * @since 02.05.2015.
 */
public class ElapsedTimeIndicator extends Label {

    public ElapsedTimeIndicator() {
        this.setFont(Font.font(20));
        this.setMinSize(50, 50);
        this.setText("0");
    }
}
