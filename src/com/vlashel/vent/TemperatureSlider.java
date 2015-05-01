package com.vlashel.vent;

import javafx.scene.control.Slider;

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

        init();
    }

    private void init() {
        double lowestTemperature = dataModule.getLowestTemperature();
        double maximumAchievableTemperature = dataModule.getMaximumAchievableTemperature();
        this.setMin(Math.floor(lowestTemperature));
        this.setMax(Math.floor(maximumAchievableTemperature));
        this.setValue(Math.floor(lowestTemperature + maximumAchievableTemperature) / 2);
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
