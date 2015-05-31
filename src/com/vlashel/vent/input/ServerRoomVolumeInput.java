package com.vlashel.vent.input;

import com.vlashel.vent.DataModule;
import javafx.scene.control.TextField;

public class ServerRoomVolumeInput extends TextField {
    private DataModule dataModule;

    public ServerRoomVolumeInput(DataModule dataModule) {
        this.dataModule = dataModule;

        init();
    }

    private void init() {
        this.setText(String.valueOf(dataModule.getServerRoomVolume()));
    }
}
