package com.vlashel.vent.indicator;

import com.vlashel.vent.DataModule;
import com.vlashel.vent.Refreshable;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

import java.util.Locale;

/**
 * @author Vlashel
 * @version 1.0
 * @since 04.05.2015.
 */
public class RoomBTemperatureIndicator extends Label implements Refreshable {
    private DataModule dataModule;

    public RoomBTemperatureIndicator(DataModule dataModule) {
        this.dataModule = dataModule;
        this.setFont(Font.font(20));
        this.setMinSize(50, 50);

        init();
    }

    private void init() {
        this.setText(String.valueOf(cutPrecision(dataModule.getRoomBInitialTemperature())));
    }

    @Override
    public void refresh() {
        init();
    }

    private double cutPrecision(double value) {
        return Double.valueOf(String.format(Locale.ENGLISH, "%.1f", value));
    }
}
