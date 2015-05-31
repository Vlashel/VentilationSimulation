package com.vlashel.vent.button;

import com.vlashel.vent.ControllerMediator;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class StartButton extends Button {

    public StartButton(ControllerMediator controllerMediator) {
        this.setOnAction((ActionEvent event) -> {
                    controllerMediator.setIsRecuperationOn(true);
                    controllerMediator.enableStopButton();
                    disable();
                }

        );
        this.setText("Запустить рекуперацию");
    }

    public void enable() {
        this.setDisabled(false);
    }

    public void disable() {
        this.setDisabled(true);
    }
}
