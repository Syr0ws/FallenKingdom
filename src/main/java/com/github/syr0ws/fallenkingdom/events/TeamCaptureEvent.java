package com.github.syr0ws.fallenkingdom.events;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;

public abstract class TeamCaptureEvent extends TeamEvent {

    private final Team capturer;

    public TeamCaptureEvent(GameModel game, Team team, Team capturer) {
        super(game, team);
        this.capturer = capturer;
    }

    public Team getCapturer() {
        return this.capturer;
    }
}
