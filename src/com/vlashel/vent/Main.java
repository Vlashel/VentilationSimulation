package com.vlashel.vent;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * @author vshel
 * @version 1.0
 * @since 12/18/2014.
 */
public class Main extends Application {

    private ImageView background = new ImageView(new Image(Ventilator.class.getResource("images/background.png")
            .toExternalForm()));

    private void init(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 690, 490));
        root.getChildren().add(background);

        ClimateControlMediator mediator = new ClimateControlMediator();

        RoomClimateControl room1ClimateControl = new RoomClimateControl(
                new Ventilator(450, 45),
                new TemperatureSlider(10, 50),
                new TemperatureIndicator(87, 60),
                new ToggleSwitch(664, 50),
                mediator, Position.UP);
        RoomClimateControl room2ClimateControl = new RoomClimateControl(
                new Ventilator(450, 300),
                new TemperatureSlider(10, 300),
                new TemperatureIndicator(87, 315),
                new ToggleSwitch(664,305),
                mediator, Position.DOWN);

        mediator.addControl(room1ClimateControl);
        mediator.addControl(room2ClimateControl);

        root.getChildren().add(room1ClimateControl);
        root.getChildren().add(room2ClimateControl);
    }


    @Override
    public void stop() {
    }

    public double getSampleWidth() {
        return 140;
    }

    public double getSampleHeight() {
        return 140;
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
