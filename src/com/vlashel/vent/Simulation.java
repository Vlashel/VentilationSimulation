package com.vlashel.vent;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Simulation extends Application {

    private void init(Stage primaryStage) {
        AnimationMediator animationMediator = new AnimationMediator();
        DataModule dataModule = new DataModule();
        TemperaturesChart chart = new TemperaturesChart(dataModule);
        Ventilator roomAVentilatorIn = new Ventilator();
        Ventilator roomAVentilatorOut = new Ventilator(-360);
        StartButton startButton = new StartButton(animationMediator);
        StopButton stopButton = new StopButton(animationMediator);
        CurrentTemperatureIndicator roomACurrentTemperatureIndicator = new CurrentTemperatureIndicator();
        CurrentTemperatureIndicator roomBCurrentTemperatureIndicator = new CurrentTemperatureIndicator();
        DesiredTemperatureIndicator desiredTemperatureIndicator = new DesiredTemperatureIndicator();
        ElapsedTimeIndicator elapsedTimeIndicator = new ElapsedTimeIndicator();
        TemperatureSlider temperatureSlider = new TemperatureSlider(dataModule);


        animationMediator.registerDataModule(dataModule);
        animationMediator.registerTemperaturesChart(chart);
        animationMediator.registerVentilators(
                roomAVentilatorIn,
                roomAVentilatorOut
        );
        animationMediator.registerStartButton(startButton);
        animationMediator.registerStopButton(stopButton);
        animationMediator.registerRoomATemperatureIndicator(roomACurrentTemperatureIndicator);
        animationMediator.registerRoomBTemperatureIndicator(roomBCurrentTemperatureIndicator);
        animationMediator.registerTemperatureSlider(temperatureSlider);
        animationMediator.registerDesiredTemperatureIndicator(desiredTemperatureIndicator);
        animationMediator.registerElapsedTimeIndicator(elapsedTimeIndicator);

        HBox mainHBox = new HBox(chart, new HBox(
                new VBox(
                        new HBox(
                                new VBox(
                                        new HBox(new Label("Room A current temperature: "), roomACurrentTemperatureIndicator)
                                ),
                                roomAVentilatorIn
                        ),
                        new HBox(
                                new VBox(
                                        new HBox(new Label("Room B current temperature: "), roomBCurrentTemperatureIndicator)
                                ),
                                roomAVentilatorOut
                        ),
                        temperatureSlider,
                        new HBox(new Label("Desired temperature: "), desiredTemperatureIndicator, new HBox(new Label("Time elapsed, in seconds: "), elapsedTimeIndicator)),
                        new HBox(
                                new VBox(startButton, stopButton),
                                new VBox(
                                        new HBox(new Label("Volumetric flow rate in m3 / sec: "), new Text(String.valueOf(dataModule.getVolumetricFlowRate()))),
                                        new HBox(new Label("Room A volume, in m3: "), new Text(String.valueOf(dataModule.getRoomAVolume()))),
                                        new HBox(new Label("Room B volume, in m3: "), new Text(String.valueOf(dataModule.getRoomBVolume())))
                                )
                        )


                )
        ));
        primaryStage.setScene(new Scene(mainHBox, 850, 450));
        stopButton.disable();
    }


    @Override
    public void stop() {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
