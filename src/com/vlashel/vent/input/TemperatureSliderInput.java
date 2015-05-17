package com.vlashel.vent.input;

import com.vlashel.vent.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;
import java.util.Locale;

/**
 * @author Vlashel
 * @version 1.0
 * @since 01.05.2015.
 */
public class TemperatureSliderInput extends Slider implements Refreshable {

    private DataModule dataModule;

    public TemperatureSliderInput(DataModule dataModule, AnimationMediator animationMediator) {
        this.dataModule = dataModule;
        this.setShowTickLabels(true);
        this.setShowTickMarks(true);
        this.setMajorTickUnit(1);
        this.setMaxWidth(355);

        this.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                animationMediator.refreshLeftTimeCounter();
                animationMediator.makeTimeLeftPrediction();
            }
        });

        init();
    }

    private void init() {
        double lowestTemperature = Helper.cutPrecision(dataModule.getLowestTemperature());
        double maximumAchievableTemperature = Helper.cutPrecision(dataModule.getMaximumAchievableTemperature());

        this.setMin(lowestTemperature);
        this.setMax(maximumAchievableTemperature);
    }

    @Override
    public void refresh() {
        init();
    }

    public void disable() {
        this.setDisabled(true);
    }

    public void enable() {
        this.setDisabled(false);
    }
}
