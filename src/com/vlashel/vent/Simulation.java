package com.vlashel.vent;

import javafx.animation.ParallelTransition;
import javafx.application.Application;


import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Simulation extends Application {



    private void init(Stage primaryStage) {
        Mediator mediator = new Mediator();

        TemperaturesChart chart = new TemperaturesChart(new ComputationModule(), mediator);
        mediator.registerTemperaturesChart(chart);

        Ventilator roomAVentilatorIn = new Ventilator();
        Ventilator roomAVentilatorOut = new Ventilator(-360);


        Ventilator roomBVentilatorIn = new Ventilator();
        Ventilator roomBVentilatorOut = new Ventilator(-360);

        List<Ventilator> ventilators = new ArrayList<>();
        ventilators.add(roomAVentilatorIn);
        ventilators.add(roomAVentilatorOut);
        ventilators.add(roomBVentilatorIn);
        ventilators.add(roomBVentilatorOut);

        ParallelTransition parallelTransition = new ParallelTransition();

        HBox hBox = new HBox();
        ventilators.forEach((it) -> {
            parallelTransition.getChildren().add(it.getAnimation());
            hBox.getChildren().add(it);
        });

        parallelTransition.getChildren().add(chart.getAnimation());
        hBox.getChildren().add(chart);

        mediator.registerAnimation(parallelTransition);

        ToggleSwitch button = new ToggleSwitch(mediator);
        mediator.registerToggleSwitch(button);

        TemperatureIndicator roomATemperatureIndicator = new TemperatureIndicator();
        mediator.registerRoomATemperature(roomATemperatureIndicator);

        TemperatureIndicator roomBTemperatureIndicator = new TemperatureIndicator();
        mediator.registerRoomBTemperature(roomBTemperatureIndicator);

        mediator.registerVentilators(ventilators);
        hBox.getChildren().add(button);
        hBox.getChildren().add(roomATemperatureIndicator);
        hBox.getChildren().add(roomBTemperatureIndicator);

        primaryStage.setScene(new Scene(hBox));
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
