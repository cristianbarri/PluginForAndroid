package com.example;

import java.sql.Time;

public class PluginClass {
    private int PlayCounter = 0;
    private int PauseCounter = 0;
    private long TimeCounter = 0;
    private long pause = 0;
    private long start = 0;

    public int CountPlay() {
        PlayCounter++;
        start = System.currentTimeMillis();
        return PlayCounter;
    }

    public int CountPause() {
        PauseCounter++;
        pause = System.currentTimeMillis();
        return PauseCounter;
    }

    public long ElapsedTime() {
        TimeCounter = start - pause;
        return TimeCounter;
    }
}
