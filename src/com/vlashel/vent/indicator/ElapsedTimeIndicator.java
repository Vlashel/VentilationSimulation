package com.vlashel.vent.indicator;


import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class ElapsedTimeIndicator extends Label {

    public ElapsedTimeIndicator() {
        this.setFont(Font.font(30));
        this.setMinSize(50, 50);
    }

    public void setElapsedTime(int time) {
        int hours = (time / 60) / 60;
        int minutes = (time - (hours * 60) * 60) / 60;
        int seconds = time - (minutes * 60) - (hours * 60 * 60);

        this.setText(String.format("%02d:%02d:%02d" , hours, minutes, seconds));
    }
}
