package com.vlashel.vent;

public class ElapsedTimeCounter implements Refreshable {
    private volatile int timeLeft;

    public ElapsedTimeCounter() {
        init();
    }

    private void init() {
        timeLeft = 0;
    }

    public void increment() {
        timeLeft++;
    }

    public void decrement() {
        timeLeft--;
    }

    @Override
    public void refresh() {
        init();
    }

    public int getTimeLeft() {
        return timeLeft;
    }
}
