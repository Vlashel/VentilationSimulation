package com.vlashel.vent;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;

import java.util.Locale;

/**
 * @author Vlashel
 * @version 1.0
 * @since 01.05.2015.
 */
public class TemperatureSlider extends Slider implements Refreshable {

    private DataModule dataModule;

    public TemperatureSlider(DataModule dataModule) {
        this.dataModule = dataModule;
        this.setShowTickLabels(true);
        this.setShowTickMarks(true);
        this.setMajorTickUnit(1);
        this.setMaxWidth(355);

        init();
    }

    private void init() {
        double lowestTemperature = Double.
                valueOf(String.format(Locale.ENGLISH, "%.1f", dataModule.getLowestTemperature()));
        double maximumAchievableTemperature = Double.
                valueOf(String.format(Locale.ENGLISH, "%.1f", dataModule.getMaximumAchievableTemperature()));

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
