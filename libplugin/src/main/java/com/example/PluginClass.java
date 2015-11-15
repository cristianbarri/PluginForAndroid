package com.example;



public class PluginClass {
    private int PlayCounter = 0;
    private int PauseCounter = 0;
    private long pause = 0;
    private long start = 0;

    public PluginClass() {

    }

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
        return start - pause;
    }
}
