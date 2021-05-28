package com.github.syr0ws.fallenkingdom.events;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;

public abstract class TeamBaseCaptureEvent extends TeamEvent {

    private final Team catcher;

    public TeamBaseCaptureEvent(GameModel game, Team team, Team catcher) {
        super(game, team);

        if(catcher == null)
            throw new IllegalArgumentException("Catcher team cannot be null.");

        this.catcher = catcher;
    }

    public Team getCatcher() {
        return this.catcher;
    }
}
