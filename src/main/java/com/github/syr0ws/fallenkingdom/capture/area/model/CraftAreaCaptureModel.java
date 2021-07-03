package com.github.syr0ws.fallenkingdom.capture.area.model;

import com.github.syr0ws.fallenkingdom.game.model.v2.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.v2.teams.FKTeamPlayer;

import java.util.*;

public class CraftAreaCaptureModel implements AreaCaptureModel {

    private final List<CraftAreaCapture> captures = new ArrayList<>();

    public void addCapture(FKTeam captured, FKTeamPlayer catcher) {

        Optional<CraftAreaCapture> optional = this.captures.stream()
                .filter(capture -> capture.getCapturedTeam().equals(captured))
                .findFirst();

        // If a capture already exists, using it.
        // Else, creating and storing a new one.
        if(optional.isPresent()) {

            CraftAreaCapture capture = optional.get();

            // Only one team can capture a base. If the player isn't a member of the
            // current catcher team, throwing an exception.
            if(!capture.getCatcherTeam().contains(catcher))
                throw new IllegalArgumentException("Only one team can capture a base.");

            capture.addCatcher(catcher);

        } else {

            CraftAreaCapture capture = new CraftAreaCapture(captured, catcher);
            this.captures.add(capture);
        }
    }

    public void removeCapture(FKTeamPlayer player) {

        Optional<CraftAreaCapture> optional = this.captures.stream()
                .filter(capture -> capture.getCatchers().contains(player))
                .findFirst();

        if(!optional.isPresent()) return;

        CraftAreaCapture capture = optional.get();
        capture.removeCatcher(player);

        if(capture.getCatchers().size() == 0) this.captures.remove(capture);
    }

    @Override
    public boolean isCaptured(FKTeam team) {
        return this.captures.stream().anyMatch(capture -> capture.getCapturedTeam().equals(team));
    }

    @Override
    public boolean isCapturing(FKTeamPlayer player) {
        return this.captures.stream().anyMatch(capture -> capture.getCatchers().contains(player));
    }

    @Override
    public Optional<CraftAreaCapture> getCapture(FKTeam captured) {
        return this.captures.stream()
                .filter(capture -> capture.getCapturedTeam().equals(captured))
                .findFirst();
    }

    @Override
    public Collection<CraftAreaCapture> getCaptures() {
        return Collections.unmodifiableCollection(this.captures);
    }
}
