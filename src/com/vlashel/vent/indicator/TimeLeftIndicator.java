package com.vlashel.vent.indicator;


import com.vlashel.vent.Refreshable;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class TimeLeftIndicator extends Label implements Refreshable {

    public TimeLeftIndicator() {
        this.setFont(Font.font(30));
        this.setMinSize(50, 50);

        disable();

        refresh();
    }

    public void setElapsedTime(int time) {
        int hours = (time / 60) / 60;
        int minutes = (time - (hours * 60) * 60) / 60;
        int seconds = time - (minutes * 60) - (hours * 60 * 60);

        this.setText(String.format("%02d:%02d:%02d" , hours, minutes, seconds));
    }

    public void enable() {
        this.setDisable(false);
    }

    public void disable() {
        this.setDisable(true);
    }

    @Override
    public void refresh() {
        setElapsedTime(0);
    }
}
