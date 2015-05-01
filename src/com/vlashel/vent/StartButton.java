package com.vlashel.vent;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class StartButton extends Button {

    public StartButton(AnimationMediator animationMediator) {
        this.setOnAction((ActionEvent event) -> {
                    animationMediator.startAnimation();
                    animationMediator.enableStopButton();
                    animationMediator.disableTemperatureSlider();
                    disable();
                }

        );
        this.setText("Start");
    }

    public void enable() {
        this.setDisabled(false);
    }

    public void disable() {
        this.setDisabled(true);
    }
}
