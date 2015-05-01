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
        TemperatureIndicator roomATemperatureIndicator = new TemperatureIndicator();
        TemperatureIndicator roomBTemperatureIndicator = new TemperatureIndicator();
        TemperatureSlider temperatureSlider = new TemperatureSlider(dataModule);


        animationMediator.registerDataModule(dataModule);
        animationMediator.registerTemperaturesChart(chart);
        animationMediator.registerVentilators(
                roomAVentilatorIn,
                roomAVentilatorOut
        );
        animationMediator.registerStartButton(startButton);
        animationMediator.registerStopButton(stopButton);
        animationMediator.registerRoomATemperatureIndicator(roomATemperatureIndicator);
        animationMediator.registerRoomBTemperatureIndicator(roomBTemperatureIndicator);
        animationMediator.registerTemperatureSlider(temperatureSlider);

        HBox mainHBox = new HBox(chart, new HBox(
                new VBox(
                        new HBox(
                                new VBox(
                                        new HBox(new Label("Room A current temperature: "), roomATemperatureIndicator)
                                ),
                                roomAVentilatorIn
                        ),
                        new HBox(
                                new VBox(
                                        new HBox(new Label("Room B current temperature: "), roomBTemperatureIndicator),
                                        new HBox(new Label("Maximum achievable temperature: "), new Text(String.format("%.1f", dataModule.getMaximumAchievableTemperature())))
                                ),
                                roomAVentilatorOut
                        ),
                        temperatureSlider,
                        startButton,
                        stopButton
                )
        ));
        primaryStage.setScene(new Scene(mainHBox));
        stopButton.disable();
    }


    @Override
    public void stop() {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
