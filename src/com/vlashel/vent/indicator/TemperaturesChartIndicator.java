package com.vlashel.vent.indicator;

import com.vlashel.vent.Animatable;
import com.vlashel.vent.AnimationMediator;
import com.vlashel.vent.DataModule;
import com.vlashel.vent.Refreshable;
import javafx.animation.Timeline;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class TemperaturesChartIndicator extends LineChart<Number, Number> implements Animatable, Refreshable {
    private XYChart.Series<Number, Number> roomASeries;
    private XYChart.Series<Number, Number> roomBSeries;
    private Timeline timeline;
    private DataModule dataModule;

    public TemperaturesChartIndicator(DataModule dataModule) {
        super(new NumberAxis(), new NumberAxis());
        this.dataModule = dataModule;
        this.getStylesheets().add(getClass().getResource("../css/stylesheets.css").toExternalForm());
        this.setCreateSymbols(false);

        NumberAxis xAxis = (NumberAxis) this.getXAxis();
        xAxis.setAutoRanging(false);
        xAxis.setLabel("Время в секундах");

        NumberAxis yAxis = (NumberAxis) this.getYAxis();
        yAxis.setAutoRanging(false);
        yAxis.setLabel("Температуры по цельсию");

        roomASeries = new XYChart.Series<>();
        roomBSeries = new XYChart.Series<>();

        roomASeries.setName("Комната А");
        roomBSeries.setName("Комната Б");

        this.setAnimated(false);
        this.setTitle("Изменение температур во времени");

        init();
    }

    private void init() {
        this.getData().add(roomASeries);
        this.getData().add(roomBSeries);
        timeline = new Timeline();

        ((NumberAxis)this.getYAxis()).setUpperBound(Math.floor(dataModule.getHighestTemperature()) + 10);
    }

    public void plot(int timePoint, double roomATemperature, double roomBTemperature) {
        roomASeries.getData().add(new Data<>(timePoint, roomATemperature));
        roomBSeries.getData().add(new Data<>(timePoint, roomBTemperature));
    }

    @Override
    public void refresh() {
        roomASeries.getData().clear();
        roomBSeries.getData().clear();
        this.getData().clear();
        this.getData().clear();
        // plotting first data at 0 point of time
        plot(0, dataModule.getRoomATemperatures().get(0), dataModule.getRoomBTemperatures().get(0));
        init();
    }

    public void setTimeUpperBound(double bound) {
        ((NumberAxis) this.getXAxis()).setUpperBound(bound);
    }

    public Timeline getAnimation() {
        return timeline;
    }

    @Override
    public void play() {
        timeline.play();
    }

    @Override
    public void stop() {
        timeline.stop();
    }
}
