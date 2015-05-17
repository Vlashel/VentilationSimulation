package com.vlashel.vent.indicator;


import com.vlashel.vent.DataModule;
import com.vlashel.vent.Refreshable;
import javafx.scene.control.Label;

public class RoomBVolumeIndicator extends Label implements Refreshable {

    private DataModule dataModule;

    public RoomBVolumeIndicator(DataModule dataModule) {
        this.dataModule = dataModule;
        init();
    }

    private void init() {
        this.setText(String.valueOf(dataModule.getRoomBVolume()));
    }

    @Override
    public void refresh() {
        init();
    }
}
