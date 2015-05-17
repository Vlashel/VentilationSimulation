package com.vlashel.vent.button;

import com.vlashel.vent.AnimationMediator;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class StartButton extends Button {

    public StartButton(AnimationMediator animationMediator) {
        this.setOnAction((ActionEvent event) -> {
                    animationMediator.startAnimation();
                }

        );
        this.setText("Запустить");
    }

    public void enable() {
        this.setDisabled(false);
    }

    public void disable() {
        this.setDisabled(true);
    }
}
