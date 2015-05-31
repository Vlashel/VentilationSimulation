package com.vlashel.vent.indicator;


import com.vlashel.vent.DataModule;
import com.vlashel.vent.Refreshable;
import javafx.scene.control.Label;

public class ServerRoomVolumeIndicator extends Label implements Refreshable {

    private DataModule dataModule;

    public ServerRoomVolumeIndicator(DataModule dataModule) {
        this.dataModule = dataModule;
        init();
    }

    private void init() {
        this.setText(String.valueOf(dataModule.getServerRoomVolume()));
    }

    @Override
    public void refresh() {
        init();
    }
}
