package com.github.syr0ws.fallenkingdom.plugin.capture.area.model;

import com.github.syr0ws.fallenkingdom.api.capture.area.model.AreaCapture;
import com.github.syr0ws.fallenkingdom.api.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.api.model.teams.FKTeamPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CraftAreaCapture implements AreaCapture {

    private final FKTeam captured, catcher;
    private final long startTime;
    private final List<FKTeamPlayer> catchers;

    public CraftAreaCapture(FKTeam captured, FKTeamPlayer catcher) {

        if(captured == null)
            throw new IllegalArgumentException("Captured team cannot be null.");

        if(catcher == null)
            throw new IllegalArgumentException("Catcher cannot be null.");

        this.captured = captured;
        this.catcher = catcher.getTeam();
        this.startTime = System.currentTimeMillis();
        this.catchers = new ArrayList<>();
        this.catchers.add(catcher);
    }

    public void addCatcher(FKTeamPlayer player) {

        if(player == null)
            throw new IllegalArgumentException("Player cannot be null.");

        if(!this.catcher.contains(player))
            throw new IllegalArgumentException("Player is not a member of the catcher team.");

        if(!this.catchers.contains(player)) this.catchers.add(player);
    }

    public void removeCatcher(FKTeamPlayer player) {

        if(player == null)
            throw new IllegalArgumentException("Player cannot be null.");

        if(!this.catcher.contains(player))
            throw new IllegalArgumentException("Player is not a member of the catcher team.");

        this.catchers.remove(player);
    }

    @Override
    public FKTeam getCapturedTeam() {
        return this.captured;
    }

    @Override
    public FKTeam getCatcherTeam() {
        return this.catcher;
    }

    @Override
    public long getStartTime() {
        return this.startTime;
    }

    @Override
    public boolean isCapturing(FKTeamPlayer player) {
        return this.catchers.contains(player);
    }

    @Override
    public List<FKTeamPlayer> getCatchers() {
        return Collections.unmodifiableList(this.catchers);
    }
}
