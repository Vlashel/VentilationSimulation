package com.vlashel.vent.button;

import com.vlashel.vent.ControllerMediator;
import javafx.scene.control.Button;

public class SettingsButton extends Button {
    public SettingsButton(ControllerMediator controllerMediator) {
        this.setText("Настройки...");
        this.setOnAction(e ->
                controllerMediator.displaySettingsWindow()
        );
    }

    public void disable() {
        this.setDisabled(true);
    }

    public void enable() {
        this.setDisabled(false);
    }
}
