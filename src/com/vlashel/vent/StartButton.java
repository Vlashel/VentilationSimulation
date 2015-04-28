package com.vlashel.vent;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class StartButton extends Button {

    public StartButton(AnimationMediator animationMediator) {
        this.setOnAction((ActionEvent event) -> {
                    animationMediator.startAnimation();
                    this.setDisabled(true);
                }

        );
        this.setText("Start");
    }
}
