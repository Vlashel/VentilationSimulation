package com.vlashel.vent.window;

import com.vlashel.vent.AnimationMediator;
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

    private void init(Stage primaryStage) {
        DataModule dataModule = new DataModule();

        AnimationMediator animationMediator = new AnimationMediator();
        TemperaturesChartIndicator chart = new TemperaturesChartIndicator(dataModule);
        Ventilator roomAVentilatorIn = new Ventilator();
        Ventilator roomAVentilatorOut = new Ventilator(-360);
        StartButton startButton = new StartButton(animationMediator);
        StopButton stopButton = new StopButton(animationMediator);
        SettingsButton settingsButton = new SettingsButton(animationMediator);
        RoomATemperatureIndicator roomATemperatureIndicator = new RoomATemperatureIndicator(dataModule);
        RoomBTemperatureIndicator roomBTemperatureIndicator = new RoomBTemperatureIndicator(dataModule);
        DesiredTemperatureIndicator desiredTemperatureIndicator = new DesiredTemperatureIndicator();
        ElapsedTimeIndicator elapsedTimeIndicator = new ElapsedTimeIndicator();

        SettingsWindow settingsWindow = new SettingsWindow(animationMediator, dataModule);
        RoomAVolumeIndicator roomAVolumeIndicator = new RoomAVolumeIndicator(dataModule);
        RoomBVolumeIndicator roomBVolumeIndicator = new RoomBVolumeIndicator(dataModule);
        VolumetricFlowRateIndicator volumetricFlowRateIndicator = new VolumetricFlowRateIndicator(dataModule);

        animationMediator.registerDataModule(dataModule);

        animationMediator.registerTemperaturesChart(chart);
        animationMediator.registerVentilators(
                roomAVentilatorIn,
                roomAVentilatorOut
        );
        animationMediator.registerElapsedTimeIndicator(elapsedTimeIndicator);
        TemperatureSliderInput temperatureSliderInput = new TemperatureSliderInput(dataModule, animationMediator);
        animationMediator.registerStartButton(startButton);
        animationMediator.registerStopButton(stopButton);
        animationMediator.registerSettingsButton(settingsButton);
        animationMediator.registerRoomATemperatureIndicator(roomATemperatureIndicator);
        animationMediator.registerRoomBTemperatureIndicator(roomBTemperatureIndicator);
        animationMediator.registerTemperatureSlider(temperatureSliderInput);
        animationMediator.registerDesiredTemperatureIndicator(desiredTemperatureIndicator);
        animationMediator.registerRoomAVolumeIndicator(roomAVolumeIndicator);
        animationMediator.registerRoomBVolumeIndicator(roomBVolumeIndicator);
        animationMediator.registerVolumetricFlowRateIndicator(volumetricFlowRateIndicator);
        animationMediator.registerInitialConditionsWindow(settingsWindow);


        HBox mainHBox = new HBox(chart, new HBox(
                new VBox(
                        new HBox(
                                new VBox(
                                        new HBox(new Label("Температура в комнате А: "), roomATemperatureIndicator)
                                ),
                                roomAVentilatorIn
                        ),
                        new HBox(
                                new VBox(
                                        new HBox(new Label("Температура в комнате Б: "), roomBTemperatureIndicator)
                                ),
                                roomAVentilatorOut
                        ),
                        temperatureSliderInput,
                        new HBox(new HBox(new Label("Желаемая температура: "), desiredTemperatureIndicator), new HBox(new Label("Оставшееся время: "), elapsedTimeIndicator)),
                        new HBox(
                                new VBox(startButton, stopButton),
                                new VBox(
                                        new HBox(new Label("Объемный расход в м3/сек: "), volumetricFlowRateIndicator),
                                        new HBox(new Label("Объем комнаты А, в м3: "), roomAVolumeIndicator),
                                        new HBox(new Label("Объем комнаты Б, в м3: "), roomBVolumeIndicator),
                                        settingsButton
                                )
                        )
                )
        ));
        primaryStage.setScene(new Scene(mainHBox, 910, 460));
        stopButton.disable();
    }


    @Override
    public void stop() {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Thermocycle");
        primaryStage.getIcons().add(new Image(getClass().getResource("icon-png.png").toExternalForm()));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
