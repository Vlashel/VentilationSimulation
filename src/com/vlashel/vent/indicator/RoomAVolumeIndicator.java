package com.vlashel.vent.indicator;


import com.vlashel.vent.DataModule;
import com.vlashel.vent.Refreshable;
import javafx.scene.control.Label;

public class RoomAVolumeIndicator extends Label implements Refreshable {

    private DataModule dataModule;

    public RoomAVolumeIndicator(DataModule dataModule) {
        this.dataModule = dataModule;
        init();
    }

    private void init() {
        this.setText(String.valueOf(dataModule.getRoomAVolume()));
    }

    @Override
    public void refresh() {
        init();
    }
}
