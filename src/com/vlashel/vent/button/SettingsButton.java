package com.vlashel.vent.button;

import com.vlashel.vent.AnimationMediator;
import javafx.scene.control.Button;

public class SettingsButton extends Button {
    public SettingsButton(AnimationMediator animationMediator) {
        this.setText("Настройки...");
        this.setOnAction(e ->
                animationMediator.displaySettingsWindow()
        );
    }

    public void disable() {
        this.setDisabled(true);
    }

    public void enable() {
        this.setDisabled(false);
    }
}
