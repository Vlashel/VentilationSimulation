package com.vlashel.vent.indicator;

import com.vlashel.vent.DataModule;
import com.vlashel.vent.Helper;
import com.vlashel.vent.Refreshable;
import javafx.scene.control.Label;
import javafx.scene.text.Font;


public class RoomATemperatureIndicator extends Label implements Refreshable {

    private DataModule dataModule;

    public RoomATemperatureIndicator(DataModule dataModule) {
        this.dataModule = dataModule;
        this.setFont(Font.font(30));
        this.setMinSize(110, 70);

        init();
    }

    private void init() {
        this.setText(String.valueOf(Helper.pack(dataModule.getRoomAInitialTemperature())));
    }

    @Override
    public void refresh() {
        init();
    }
}
