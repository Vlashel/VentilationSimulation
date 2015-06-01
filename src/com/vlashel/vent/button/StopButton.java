package com.vlashel.vent.button;

import com.vlashel.vent.ControllerMediator;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class StopButton extends Button {
    public StopButton(ControllerMediator controllerMediator) {
        this.setOnAction(
                (ActionEvent event) -> {
                    controllerMediator.setRecuperate(false);
                    controllerMediator.enableStartButton();
                    controllerMediator.disableTemperatureSlider();
                    controllerMediator.disableDesiredTemperatureIndicator();
                    controllerMediator.disableTimeLeftIndicator();
                    controllerMediator.refreshElapsedTimeCounter();
                    disable();
                }
        );
        this.setText("Остановить рекуперацию");
    }

    public void enable() {
        this.setDisabled(false);
    }

    public void disable() {
        this.setDisabled(true);
    }
}
