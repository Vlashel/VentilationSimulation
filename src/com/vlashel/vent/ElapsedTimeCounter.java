package com.vlashel.vent;

public class ElapsedTimeCounter implements Refreshable {
    private volatile int elapsedTime;

    public ElapsedTimeCounter() {
        init();
    }

    private void init() {
        elapsedTime = 0;
    }

    public void increment() {
        elapsedTime++;
    }

    public void decrement() {
        elapsedTime--;
    }

    @Override
    public void refresh() {
        init();
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }
}
