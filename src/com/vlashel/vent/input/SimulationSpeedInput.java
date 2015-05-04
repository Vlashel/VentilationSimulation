package com.vlashel.vent.input;

import com.vlashel.vent.DataModule;
import com.vlashel.vent.Helper;
import javafx.scene.control.TextField;

/**
 * @author Vlashel
 * @version 1.0
 * @since 04.05.2015.
 */
public class SimulationSpeedInput extends TextField {
    private DataModule dataModule;

    public SimulationSpeedInput(DataModule dataModule) {
        this.dataModule = dataModule;

        init();
    }

    private void init() {
        this.setText(String.valueOf(Helper.cutPrecision(dataModule.getSpeed())));
    }
}
