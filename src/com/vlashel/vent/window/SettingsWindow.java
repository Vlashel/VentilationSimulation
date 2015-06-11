package com.vlashel.vent.window;

import com.vlashel.vent.ControllerMediator;
import com.vlashel.vent.DataModule;
import com.vlashel.vent.input.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SettingsWindow {

    private DataModule dataModule;
    private ControllerMediator controllerMediator;

    public SettingsWindow(ControllerMediator controllerMediator, DataModule dataModule) {
        this.controllerMediator = controllerMediator;
        this.dataModule = dataModule;
    }

    public void display() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Настройки");

        ServerRoomTemperatureMaxInput serverRoomTemperatureMaxInput = new ServerRoomTemperatureMaxInput(dataModule);
        ServerRoomTemperatureMinInput serverRoomTemperatureMinInput = new ServerRoomTemperatureMinInput(dataModule);

        ServerRoomVolumeInput serverRoomVolumeInput = new ServerRoomVolumeInput(dataModule);
        OfficeRoomVolumeInput officeRoomVolumeInput = new OfficeRoomVolumeInput(dataModule);

        VolumetricFlowRateInput volumetricFlowRateInput = new VolumetricFlowRateInput(dataModule);

        SimulationSpeedInput simulationSpeedInput = new SimulationSpeedInput(dataModule);

        VBox labels = new VBox(
                new Label("Максимальная температура в серверной комнате, по Цельсию:"),
                new Label("Минимальная температура в серверной комнате, по Цельсию:"),
                new Label("Объем серверной комнаты, в м3:"),
                new Label("Объем офисного помещения, в м3:"),
                new Label("Объемный расход, в м3/сек:"),
                new Label("Скорость симуляции:")
        );

        labels.setSpacing(10);

        VBox inputs = new VBox(
                serverRoomTemperatureMinInput,
                serverRoomTemperatureMaxInput,
                serverRoomVolumeInput,
                officeRoomVolumeInput,
                volumetricFlowRateInput,
                simulationSpeedInput
        );

        Button saveButton = new Button("Сохранить");
        saveButton.setOnAction(e -> {
            dataModule.setServerRoomTemperatureMin(Double.valueOf(serverRoomTemperatureMinInput.getText()));
            dataModule.setServerRoomTemperatureMax(Double.valueOf(serverRoomTemperatureMaxInput.getText()));
            dataModule.setServerRoomVolume(Double.valueOf(serverRoomVolumeInput.getText()));
            dataModule.setOfficeRoomVolume(Double.valueOf(officeRoomVolumeInput.getText()));
            dataModule.setVolumetricFlowRate(Double.valueOf(volumetricFlowRateInput.getText()));
            dataModule.setSpeed(Double.valueOf(simulationSpeedInput.getText()));

            controllerMediator.refresh();
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
}
