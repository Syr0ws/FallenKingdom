package com.github.syr0ws.fallenkingdom.game.model.v2.events;

import com.github.syr0ws.fallenkingdom.game.model.v2.teams.FKTeam;

public abstract class TeamBaseCaptureEvent extends FKTeamEvent {

    private final FKTeam catcher;

    public TeamBaseCaptureEvent(FKTeam team, FKTeam catcher) {
        super(team);

        if(catcher == null)
            throw new IllegalArgumentException("Catcher team cannot be null.");

        this.catcher = catcher;
    }

    public FKTeam getCatcher() {
        return this.catcher;
    }
}
