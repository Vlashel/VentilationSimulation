package com.vlashel.vent.input;

import com.vlashel.vent.DataModule;
import com.vlashel.vent.Helper;
import javafx.scene.control.TextField;

public class SimulationSpeedInput extends TextField {
    private DataModule dataModule;

    public SimulationSpeedInput(DataModule dataModule) {
        this.dataModule = dataModule;

        init();
    }

    private void init() {
        this.setText(String.valueOf(Helper.pack(dataModule.getSpeed())));
    }
}
