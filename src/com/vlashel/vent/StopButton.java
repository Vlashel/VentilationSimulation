package com.vlashel.vent;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class StopButton extends Button {
    public StopButton(AnimationMediator animationMediator) {
        this.setOnAction(
                (ActionEvent event) -> {
                    animationMediator.finishAnimation();
                    animationMediator.enableStartButton();
                    animationMediator.refreshTemperatureSlider();
                    animationMediator.enableTemperatureSlider();
                    disable();
                }
        );
        this.setText("Stop");
    }

    public void enable() {
        this.setDisabled(false);
    }

    public void disable() {
        this.setDisabled(true);
    }
}