package com.vlashel.vent.button;

import com.vlashel.vent.AnimationMediator;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class StopButton extends Button {
    public StopButton(AnimationMediator animationMediator) {
        this.setOnAction(
                (ActionEvent event) -> {
                    animationMediator.finishAnimation();
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
