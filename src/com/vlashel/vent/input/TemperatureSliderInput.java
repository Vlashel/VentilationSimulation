package com.vlashel.vent.input;

import com.vlashel.vent.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;

public class TemperatureSliderInput extends Slider implements Refreshable {

    private DataModule dataModule;

    public TemperatureSliderInput(DataModule dataModule, ControllerMediator controllerMediator) {
        this.dataModule = dataModule;
        this.setShowTickLabels(true);
        this.setShowTickMarks(true);
        this.setMajorTickUnit(1);
        this.setMaxWidth(355);

        init();

        this.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
              //  animationMediator.makeTimeLeftPrediction();
            }
        });
    }

    private void init() {
        double lowestTemperature = Helper.pack(dataModule.getLowestTemperature());
        double maximumAchievableTemperature = Helper.pack(dataModule.getMaximumAchievableTemperature());

        this.setMin(lowestTemperature);
        this.setMax(maximumAchievableTemperature);
    }

    @Override
    public void refresh() {
        init();
    }

    public void disable() {
        this.setDisable(true);
    }

    public void enable() {
        this.setDisable(false);
    }
}
