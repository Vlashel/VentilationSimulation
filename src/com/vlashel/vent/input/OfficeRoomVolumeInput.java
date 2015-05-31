package com.vlashel.vent.input;

import com.vlashel.vent.DataModule;
import javafx.scene.control.TextField;

public class OfficeRoomVolumeInput extends TextField {
    private DataModule dataModule;

    public OfficeRoomVolumeInput(DataModule dataModule) {
        this.dataModule = dataModule;

        init();
    }

    private void init() {
        this.setText(String.valueOf(dataModule.getOfficeRoomVolume()));
    }
}
