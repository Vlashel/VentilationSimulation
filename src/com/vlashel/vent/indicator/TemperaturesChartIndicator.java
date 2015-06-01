package com.vlashel.vent.indicator;

import com.vlashel.vent.Refreshable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class TemperaturesChartIndicator extends LineChart<Number, Number> implements Refreshable {
    private XYChart.Series<Number, Number> serverRoomSeries;
    private XYChart.Series<Number, Number> officeRoomSeries;

    public TemperaturesChartIndicator() {
        super(new NumberAxis(), new NumberAxis());
        this.setCreateSymbols(false);

        NumberAxis xAxis = (NumberAxis) this.getXAxis();
        xAxis.setAutoRanging(false);
        xAxis.setLabel("Время в секундах");

        NumberAxis yAxis = (NumberAxis) this.getYAxis();
        yAxis.setAutoRanging(false);
        yAxis.setLabel("Температуры по Цельсию");

        serverRoomSeries = new XYChart.Series<>();
        officeRoomSeries = new XYChart.Series<>();

        serverRoomSeries.setName("Серверная комната");
        officeRoomSeries.setName("Офисное помещение");

        this.setAnimated(false);
        this.setTitle("Изменение температур во времени");

        init();
    }

    private void init() {
        this.setMinSize(800, 700);

        this.getData().add(serverRoomSeries);
        this.getData().add(officeRoomSeries);

        ((NumberAxis)this.getYAxis()).setUpperBound(30);
        ((NumberAxis)this.getYAxis()).setLowerBound(10);
    }

    public void plot(int timePoint, double serverRoomTemperature, double officeRoomTemperature) {
        serverRoomSeries.getData().add(new Data<>(timePoint, serverRoomTemperature));
        officeRoomSeries.getData().add(new Data<>(timePoint, officeRoomTemperature));
    }

    @Override
    public void refresh() {
        serverRoomSeries.getData().clear();
        officeRoomSeries.getData().clear();
        this.getData().clear();
        this.getData().clear();

        init();
    }

    public void setTimeUpperBound(double bound) {
        ((NumberAxis) this.getXAxis()).setUpperBound(bound);
    }

    public void setTimeLowerBound(double bound) {
        ((NumberAxis) this.getXAxis()).setLowerBound(bound);
    }

}
