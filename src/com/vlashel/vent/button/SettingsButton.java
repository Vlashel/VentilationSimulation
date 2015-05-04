package com.vlashel.vent.button;

import com.vlashel.vent.AnimationMediator;
import javafx.scene.control.Button;

/**
 * @author Vlashel
 * @version 1.0
 * @since 03.05.2015.
 */
public class SettingsButton extends Button {
    public SettingsButton(AnimationMediator animationMediator) {
        this.setText("Settings...");
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
