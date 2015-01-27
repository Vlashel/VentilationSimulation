package com.vlashel.vent;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.util.Duration;


/**
 * @author vshel
 * @version 1.0
 * @since 12/26/2014
 */
public class RoomClimateControl extends Parent {

    private Ventilator ventilator;
    private ToggleSwitch toggleSwitch;
    private TemperatureSlider temperatureSlider;
    private TemperatureIndicator temperatureIndicator;
    private ClimateControlMediator mediator;
    private Position position;
    private Timeline sliderAndVentilatorTimeline = new Timeline();
    private final RoomClimateControl currentControl = this;
    private Path pathToBeRemoved;

    private RoomClimateControl partner;

    public RoomClimateControl(Ventilator ventilator,
                              TemperatureSlider temperatureSlider,
                              TemperatureIndicator temperatureIndicator,
                              ToggleSwitch toggleSwitch,
                              ClimateControlMediator mediator, Position position) {
        this.ventilator = ventilator;
        this.temperatureSlider = temperatureSlider;
        this.temperatureIndicator = temperatureIndicator;
        this.temperatureIndicator.setText("20");
        this.toggleSwitch = toggleSwitch;
        this.mediator = mediator;
        this.position = position;
        this.getChildren().add(ventilator);
        this.getChildren().add(temperatureSlider);
        this.getChildren().add(temperatureIndicator);
        this.getChildren().add(toggleSwitch);
        bindInteractions();
    }

    public void receive(int temperature, RoomClimateControl control) {
        if (Integer.valueOf(this.temperatureIndicator.getText()) > temperature
                && control.temperatureSlider.valueProperty().get() > temperature) {
            this.ventilator.changeRotationDirection();
            this.ventilator.getRotateTransition().setRate(Ventilator.FAST_SPEED);
            Path path = new PathLine(control.getPosition(), Color.CRIMSON);
            pathToBeRemoved = path;
            currentControl.partner = control;
            control.partner = currentControl;
            this.getChildren().add(path);
        } else if (Integer.valueOf(this.temperatureIndicator.getText()) < temperature
                && control.temperatureSlider.valueProperty().get() < temperature) {
            this.ventilator.changeRotationDirection();
            this.ventilator.getRotateTransition().setRate(Ventilator.FAST_SPEED);
            Path path = new PathLine(control.getPosition(), Color.AQUA);
            pathToBeRemoved = path;
            currentControl.partner = control;
            control.partner = currentControl;
            this.getChildren().add(path);
        }
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    private void bindSwitchToVentilator() {
        toggleSwitch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stopVentilatorGradually();
            }
        });
    }

    public void stopVentilatorGradually() {
        final Timeline timeline = new Timeline();
        RotateTransition transition = ventilator.getRotateTransition();
        if (toggleSwitch.isSelected()) {
            timeline.stop();
            transition.setRate(Ventilator.MEDIUM_SPEED);
            transition.play();
        } else {
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500),
                    new KeyValue(transition.rateProperty(), 0.3)));
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000),
                    new KeyValue(transition.rateProperty(), 0.1)));
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1400),
                    new KeyValue(transition.rateProperty(), 0)));

            timeline.play();

            timeline.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    timeline.stop();
                }
            });
        }
    }

    private void bindTemperatureSliderToVentilator() {

        temperatureSlider.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                sliderAndVentilatorTimeline.stop();
                sliderAndVentilatorTimeline = new Timeline();
                int desiredTemperature = (int) temperatureSlider.valueProperty().get();
                int currentTemperature = (int) Double.parseDouble(temperatureIndicator.getText());

                mediator.send(currentTemperature, currentControl);

                if (desiredTemperature - currentTemperature > 0) {
                    int seconds = 0;
                    while (currentTemperature <= desiredTemperature) {
                        sliderAndVentilatorTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(seconds++),
                                new KeyValue(temperatureIndicator.textProperty(), String.valueOf(currentTemperature++))));
                    }
                } else {
                    int seconds = 0;
                    while (currentTemperature >= desiredTemperature) {
                        sliderAndVentilatorTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(seconds++),
                                new KeyValue(temperatureIndicator.textProperty(), String.valueOf(currentTemperature--))));
                    }
                }

                ventilator.getRotateTransition().setRate(Ventilator.FAST_SPEED);
                sliderAndVentilatorTimeline.play();
                sliderAndVentilatorTimeline.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        ventilator.getRotateTransition().setRate(Ventilator.MEDIUM_SPEED);
                        if (checkIfPartners()) {
                            mediator.sendTempAchieved(currentControl);
                            unboundPartners();
                        }
                    }
                });
            }
        });
    }

    public void unboundPartners() {
        currentControl.partner.partner = null;
        currentControl.partner = null;
    }

    public boolean checkIfPartners() {
        if (currentControl.partner == null) {
            return false;
        }
        if (currentControl.partner.partner == null) {
            return false;
        }
        if (currentControl.partner.partner == currentControl) {
            return true;
        } else {
            return false;
        }
    }

    public void setNormalRotation() {
        this.ventilator.changeRotationDirection();
        this.ventilator.getRotateTransition().setRate(Ventilator.MEDIUM_SPEED);
        this.getChildren().remove(pathToBeRemoved);
    }

    public void bindInteractions() {
        bindSwitchToVentilator();
        bindTemperatureSliderToVentilator();
    }



}
