package com.vlashel.vent;

/**
 * @author Vlashel
 * @version 1.0
 * @since 02.05.2015.
 */
public class TimeLeftCounter implements Refreshable {
    private volatile int timeLeft;

    public TimeLeftCounter() {
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
