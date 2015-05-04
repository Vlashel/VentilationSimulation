package com.vlashel.vent.window;

import com.vlashel.vent.AnimationMediator;
import com.vlashel.vent.DataModule;
import com.vlashel.vent.input.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Vlashel
 * @version 1.0
 * @since 03.05.2015.
 */
public class SettingsWindow {

    private DataModule dataModule;
    private AnimationMediator animationMediator;

    public SettingsWindow(AnimationMediator animationMediator, DataModule dataModule) {
        this.animationMediator = animationMediator;
        this.dataModule = dataModule;
    }

    public void display() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Settings");

        RoomATemperatureInput roomATemperatureInput = new RoomATemperatureInput(dataModule);
        RoomBTemperatureInput roomBTemperatureInput = new RoomBTemperatureInput(dataModule);

        RoomAVolumeInput roomAVolumeInput = new RoomAVolumeInput(dataModule);
        RoomBVolumeInput roomBVolumeInput = new RoomBVolumeInput(dataModule);

        VolumetricFlowRateInput volumetricFlowRateInput = new VolumetricFlowRateInput(dataModule);

        SimulationSpeedInput simulationSpeedInput = new SimulationSpeedInput(dataModule);

        VBox labels = new VBox(
                new Label("Room A temperature, in Celsius:"),
                new Label("Room B temperature, in Celsius:"),
                new Label("Room A volume, in m3:"),
                new Label("Room B volume, in m3:"),
                new Label("Volumetric flow rate, in m3 per second:"),
                new Label("Simulation speed:")
        );

        labels.setSpacing(10);

        VBox inputs = new VBox(
                roomATemperatureInput,
                roomBTemperatureInput,
                roomAVolumeInput,
                roomBVolumeInput,
                volumetricFlowRateInput,
                simulationSpeedInput
        );

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            dataModule.setRoomAInitialTemperature(Double.valueOf(roomATemperatureInput.getText()));
            dataModule.setRoomBInitialTemperature(Double.valueOf(roomBTemperatureInput.getText()));
            dataModule.setRoomAVolume(Double.valueOf(roomAVolumeInput.getText()));
            dataModule.setRoomBVolume(Double.valueOf(roomBVolumeInput.getText()));
            dataModule.setVolumetricFlowRate(Double.valueOf(volumetricFlowRateInput.getText()));
            dataModule.setSpeed(Double.valueOf(simulationSpeedInput.getText()));

            animationMediator.refresh();
            window.close();
        });

        VBox layout = new VBox(
                new HBox(labels, inputs),
                saveButton);

        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

  /*  private String validateInput(String input) {
        if (input.matches("\\d+\\.?\\d*")) {
            return input;
        } else {
            animationMediator.displayAlertBox("Invalid input", "Should be a numerical value!");
            valid = false;
            return "Invalid";
        }
    }*/
}