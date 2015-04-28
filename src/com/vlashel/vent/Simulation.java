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
        AnimationMediator animationMediator = new AnimationMediator();

        DataModule dataModule = new DataModule();
        animationMediator.registerDataModule(dataModule);

        TemperaturesChart chart = new TemperaturesChart(dataModule);
        animationMediator.registerTemperaturesChart(chart);

        Ventilator roomAVentilatorIn = new Ventilator();
        Ventilator roomAVentilatorOut = new Ventilator(-360);
        Ventilator roomBVentilatorIn = new Ventilator();
        Ventilator roomBVentilatorOut = new Ventilator(-360);

        animationMediator.registerVentilators(
                roomAVentilatorIn,
                roomAVentilatorOut,
                roomBVentilatorIn,
                roomBVentilatorOut
        );

        List<Ventilator> ventilators = new ArrayList<>();
        ventilators.add(roomAVentilatorIn);
        ventilators.add(roomAVentilatorOut);
        ventilators.add(roomBVentilatorIn);
        ventilators.add(roomBVentilatorOut);

        ParallelTransition parallelTransition = new ParallelTransition();

        HBox hBox = new HBox();
        ventilators.forEach((it) -> {
            hBox.getChildren().add(it);
        });

        parallelTransition.getChildren().add(chart.getAnimation());
        hBox.getChildren().add(chart);

        animationMediator.registerApplicationAnimation(parallelTransition);

        ToggleSwitch button = new ToggleSwitch(animationMediator);
        animationMediator.registerToggleSwitch(button);

        TemperatureIndicator roomATemperatureIndicator = new TemperatureIndicator();
        animationMediator.registerRoomATemperatureIndicator(roomATemperatureIndicator);

        TemperatureIndicator roomBTemperatureIndicator = new TemperatureIndicator();
        animationMediator.registerRoomBTemperatureIndicator(roomBTemperatureIndicator);

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
