package com.vlashel.vent;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Simulation extends Application {

    private void init(Stage primaryStage) {
        AnimationMediator animationMediator = new AnimationMediator();
        DataModule dataModule = new DataModule();
        TemperaturesChart chart = new TemperaturesChart(dataModule);
        Ventilator roomAVentilatorIn = new Ventilator();
        Ventilator roomAVentilatorOut = new Ventilator(-360);
        Ventilator roomBVentilatorIn = new Ventilator();
        Ventilator roomBVentilatorOut = new Ventilator(-360);
        StartButton button = new StartButton(animationMediator);
        TemperatureIndicator roomATemperatureIndicator = new TemperatureIndicator();
        TemperatureIndicator roomBTemperatureIndicator = new TemperatureIndicator();


        animationMediator.registerDataModule(dataModule);
        animationMediator.registerTemperaturesChart(chart);
        animationMediator.registerVentilators(
                roomAVentilatorIn,
                roomAVentilatorOut,
                roomBVentilatorIn,
                roomBVentilatorOut
        );
        animationMediator.registerToggleSwitch(button);
        animationMediator.registerRoomATemperatureIndicator(roomATemperatureIndicator);
        animationMediator.registerRoomBTemperatureIndicator(roomBTemperatureIndicator);

        HBox mainHBox = new HBox();
        mainHBox.getChildren().add(chart);

        HBox rightHBox = new HBox();

        mainHBox.getChildren().add(rightHBox);

        VBox leftVBox = new VBox();
        leftVBox.getChildren().add(roomATemperatureIndicator);
        leftVBox.getChildren().add(roomBTemperatureIndicator);

        VBox rightVBox = new VBox();
        HBox topHBox = new HBox();
        topHBox.getChildren().add(roomAVentilatorIn);
        topHBox.getChildren().add(roomAVentilatorOut);

        HBox bottomHBox = new HBox();
        bottomHBox.getChildren().add(roomBVentilatorIn);
        bottomHBox.getChildren().add(roomBVentilatorOut);
        rightVBox.getChildren().add(topHBox);
        rightVBox.getChildren().add(bottomHBox);
        rightVBox.getChildren().add(button);

        rightHBox.getChildren().add(leftVBox);
        rightHBox.getChildren().add(rightVBox);

        primaryStage.setScene(new Scene(mainHBox));
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
