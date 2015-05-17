package com.vlashel.vent.input;

import com.vlashel.vent.DataModule;
import com.vlashel.vent.Helper;
import javafx.scene.control.TextField;

public class RoomATemperatureInput extends TextField {
    private DataModule dataModule;

    public RoomATemperatureInput(DataModule dataModule) {
        this.dataModule = dataModule;

        init();
    }

    private void init() {
        this.setText(String.valueOf(dataModule.getRoomAInitialTemperature()));
    }
}
