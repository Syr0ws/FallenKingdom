package com.github.syr0ws.fallenkingdom.game.model.capture;

import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FKCapture implements Capture {

    private final Team capturer, captured;
    private final List<TeamPlayer> capturers;
    private int time;

    public FKCapture(TeamPlayer capturer, Team captured) {

        if(capturer == null)
            throw new IllegalArgumentException("Capturer cannot be null.");

        if(captured == null)
            throw new IllegalArgumentException("Captured team cannot be null.");

        this.capturer = capturer.getTeam();
        this.captured = captured;
        this.capturers = new ArrayList<>();

        this.addCapturer(capturer);
    }

    public void addCapturer(TeamPlayer player) {

        if(player == null)
            throw new IllegalArgumentException("FKTeamPlayer cannot be null.");

        if(!this.capturer.contains(player))
            throw new IllegalArgumentException("FKTeamPlayer not a member of the capturer team.");

        if(!this.capturers.contains(player)) this.capturers.add(player);
    }

    public void removeCapturer(TeamPlayer player) {
        this.capturers.remove(player);
    }

    @Override
    public void addCaptureTime() {
        this.time++;
    }

    @Override
    public int getCaptureTime() {
        return this.time;
    }

    @Override
    public Team getCapturer() {
        return this.capturer;
    }

    @Override
    public Team getCaptured() {
        return this.captured;
    }

    @Override
    public Collection<TeamPlayer> getCapturers() {
        return Collections.unmodifiableList(this.capturers);
    }
}
