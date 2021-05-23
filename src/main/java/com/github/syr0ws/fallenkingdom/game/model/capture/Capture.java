package com.github.syr0ws.fallenkingdom.game.model.capture;

import com.github.syr0ws.fallenkingdom.game.model.teams.Team;

public class Capture {

    private final Team captured, capturer;
    private final long startTime;

    public Capture(Team captured, Team capturer) {

        if(captured == null)
            throw new IllegalArgumentException("Captured team cannot be null.");

        if(capturer == null)
            throw new IllegalArgumentException("Capturer team cannot be null.");

        if(captured.equals(capturer))
            throw new IllegalArgumentException("Captured team and capturer team cannot be the sames.");

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

    public int getCaptureTimeSeconds() {
        return (int) ((System.currentTimeMillis() - this.startTime) / 1000);
    }
}
