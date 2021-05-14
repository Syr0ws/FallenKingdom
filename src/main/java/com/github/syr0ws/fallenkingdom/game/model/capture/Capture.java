package com.github.syr0ws.fallenkingdom.game.model.capture;

import com.github.syr0ws.fallenkingdom.game.model.teams.Team;

public class Capture {

    private final Team captured, capturer;
    private final long startTime;

    public Capture(Team captured, Team capturer) {
        this.captured = captured;
        this.capturer = capturer;
        this.startTime = System.currentTimeMillis();
    }

    public Team getCaptured() {
        return this.captured;
    }

    public Team getCapturer() {
        return this.capturer;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public int getCaptureTimeInSeconds() {
        return (int) ((System.currentTimeMillis() - this.startTime) / 1000);
    }
}
