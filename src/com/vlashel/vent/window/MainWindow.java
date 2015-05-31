package com.vlashel.vent.window;

import com.vlashel.vent.ControllerMediator;
import com.vlashel.vent.DataModule;
import com.vlashel.vent.indicator.*;
import com.vlashel.vent.Ventilator;
import com.vlashel.vent.button.SettingsButton;
import com.vlashel.vent.button.StartButton;
import com.vlashel.vent.button.StopButton;
import com.vlashel.vent.input.TemperatureSliderInput;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class MainWindow extends Application {

    private ControllerMediator controllerMediator;

    private void init(Stage primaryStage) {
        controllerMediator = new ControllerMediator();
        DataModule dataModule = new DataModule(controllerMediator);
        TemperaturesChartIndicator chart = new TemperaturesChartIndicator(dataModule);
        Ventilator roomAVentilatorIn = new Ventilator();
        Ventilator roomAVentilatorOut = new Ventilator(-360);
        Ventilator roomBVentilatorIn = new Ventilator();
        Ventilator roomBVentilatorOut = new Ventilator(-360);
        StartButton startButton = new StartButton(controllerMediator);
        StopButton stopButton = new StopButton(controllerMediator);
        SettingsButton settingsButton = new SettingsButton(controllerMediator);
        ServerRoomTemperatureIndicator serverRoomTemperatureIndicator = new ServerRoomTemperatureIndicator(dataModule);
        OfficeRoomTemperatureIndicator officeRoomTemperatureIndicator = new OfficeRoomTemperatureIndicator(dataModule);
        DesiredTemperatureIndicator desiredTemperatureIndicator = new DesiredTemperatureIndicator();
        ElapsedTimeIndicator elapsedTimeIndicator = new ElapsedTimeIndicator();

        SettingsWindow settingsWindow = new SettingsWindow(controllerMediator, dataModule);
        ServerRoomVolumeIndicator serverRoomVolumeIndicator = new ServerRoomVolumeIndicator(dataModule);
        OfficeRoomVolumeIndicator officeRoomVolumeIndicator = new OfficeRoomVolumeIndicator(dataModule);
        VolumetricFlowRateIndicator volumetricFlowRateIndicator = new VolumetricFlowRateIndicator(dataModule);

        controllerMediator.registerDataModule(dataModule);

        controllerMediator.registerTemperaturesChart(chart);
        controllerMediator.registerServerRoomVentilators(
                roomAVentilatorIn,
                roomAVentilatorOut
        );
        controllerMediator.registerOfficeRoomVentilators(
                roomBVentilatorIn,
                roomBVentilatorOut
        );
        controllerMediator.registerElapsedTimeIndicator(elapsedTimeIndicator);
        TemperatureSliderInput temperatureSliderInput = new TemperatureSliderInput(dataModule, controllerMediator);
        controllerMediator.registerStartButton(startButton);
        controllerMediator.registerStopButton(stopButton);
        controllerMediator.registerSettingsButton(settingsButton);
        controllerMediator.registerRoomATemperatureIndicator(serverRoomTemperatureIndicator);
        controllerMediator.registerRoomBTemperatureIndicator(officeRoomTemperatureIndicator);
        controllerMediator.registerTemperatureSlider(temperatureSliderInput);
        controllerMediator.registerDesiredTemperatureIndicator(desiredTemperatureIndicator);
        controllerMediator.registerRoomAVolumeIndicator(serverRoomVolumeIndicator);
        controllerMediator.registerRoomBVolumeIndicator(officeRoomVolumeIndicator);
        controllerMediator.registerVolumetricFlowRateIndicator(volumetricFlowRateIndicator);
        controllerMediator.registerInitialConditionsWindow(settingsWindow);


        HBox mainHBox = new HBox(chart,
                new VBox(
                        new HBox(
                                new VBox(
                                        new HBox(new Label("Температура в серверной комнате: "), serverRoomTemperatureIndicator)
                                )

                        ),
                        new HBox(
                                new VBox(
                                        new HBox(new Label("Температура в офисном помещении: "), officeRoomTemperatureIndicator)
                                )
                        ),
                        temperatureSliderInput,
                        new HBox(new HBox(new Label("Желаемая температура: "), desiredTemperatureIndicator), new HBox(new Label("Оставшееся время: "), elapsedTimeIndicator)),
                        new HBox(
                                new VBox(startButton, stopButton),
                                new VBox(
                                        new HBox(new Label("Объемный расход в м3/сек: "), volumetricFlowRateIndicator),
                                        new HBox(new Label("Объем комнаты А, в м3: "), serverRoomVolumeIndicator),
                                        new HBox(new Label("Объем комнаты Б, в м3: "), officeRoomVolumeIndicator),
                                        settingsButton
                                )
                        ),
                        new VBox(new Label("Вентиляторы серверной комнаты:"), new HBox(roomAVentilatorIn, roomAVentilatorOut)),
                        new VBox(new Label("Вентиляторы офисного помещения:"), new HBox(roomBVentilatorIn, roomBVentilatorOut))
                )
        );
        primaryStage.setScene(new Scene(mainHBox));
        stopButton.disable();
        //animationMediator.makeTimeLeftPrediction();
    }


    @Override
    public void stop() {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.setResizable(false);
        primaryStage.setMinWidth(1220);
        primaryStage.setTitle("Thermocycle");
        primaryStage.getIcons().add(new Image(getClass().getResource("icon-png.png").toExternalForm()));
        primaryStage.show();
        controllerMediator.animate();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
