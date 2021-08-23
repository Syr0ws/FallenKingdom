package com.github.syr0ws.fallenkingdom.plugin.capture.area.model;

import com.github.syr0ws.fallenkingdom.api.capture.area.model.AreaCaptureModel;
import com.github.syr0ws.fallenkingdom.api.capture.area.model.settings.CaptureSettingsAccessor;
import com.github.syr0ws.fallenkingdom.api.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.api.model.teams.FKTeamPlayer;

import java.util.*;

public class CraftAreaCaptureModel implements AreaCaptureModel {

    private final CaptureSettingsAccessor settings;
    private final List<CraftAreaCapture> captures = new ArrayList<>();

    public CraftAreaCaptureModel(CaptureSettingsAccessor settings) {

        if(settings == null)
            throw new IllegalArgumentException("CaptureSettingsAccessor cannot be null.");

        this.settings = settings;
    }

    public void capture(FKTeamPlayer player, FKTeam captured) {

        if(captured.contains(player))
            throw new IllegalArgumentException("Catcher cannot be a member to the team he's capturing.");

        Optional<CraftAreaCapture> optional = this.captures.stream()
                .filter(capture -> capture.getCapturedTeam().equals(captured))
                .findFirst();

        // If a capture already exists, using it.
        if(!optional.isPresent()) {
            CraftAreaCapture capture = new CraftAreaCapture(captured, player);
            this.captures.add(capture);
            return;
        }

        CraftAreaCapture capture = optional.get();

        // Only one team can capture a base. If the player isn't a member of the
        // current catcher team, throwing an exception.
        if(!capture.getCatcherTeam().contains(player))
            throw new IllegalArgumentException("Only one team can capture a base.");

        capture.addCatcher(player);
    }

    public CraftAreaCapture removeCapture(FKTeamPlayer player) {

        Optional<CraftAreaCapture> optional = this.captures.stream()
                .filter(capture -> capture.getCatchers().contains(player))
                .findFirst();

        if(!optional.isPresent())
            throw new IllegalArgumentException("Player not capturing.");

        CraftAreaCapture capture = optional.get();
        capture.removeCatcher(player);

        if(capture.getCatchers().size() == 0) this.captures.remove(capture);

        return capture;
    }

    public CraftAreaCapture removeCapture(FKTeam captured) {

        Optional<CraftAreaCapture> optional = this.getCapture(captured);

        if(!optional.isPresent())
            throw new IllegalArgumentException("Team not captured");

        CraftAreaCapture capture = optional.get();

        this.captures.remove(capture);

        return capture;
    }

    @Override
    public boolean canCapture(FKTeam team, FKTeamPlayer player) {

        // An eliminated team cannot be captured.
        if(team.isEliminated()) return false;

        // A dead player cannot capture a team.
        if(!player.isAlive()) return false;

        Optional<CraftAreaCapture> optional = this.getCapture(team);

        // The team is not currently captured so it can be captured by the player.
        if(!optional.isPresent()) return true;

        CraftAreaCapture capture = optional.get();

        // Only one team can capture another one. If the player is a member
        // of the capturing team, he can captures the team.
        return capture.getCatcherTeam().contains(player);
    }

    @Override
    public CaptureSettingsAccessor getSettings() {
        return this.settings;
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
