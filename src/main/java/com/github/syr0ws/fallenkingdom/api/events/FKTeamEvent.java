package com.github.syr0ws.fallenkingdom.api.events;

import com.github.syr0ws.fallenkingdom.api.model.teams.FKTeam;

public abstract class FKTeamEvent extends FKEvent{

    private final FKTeam team;

    public FKTeamEvent(FKTeam team) {

        if(team == null)
            throw new IllegalArgumentException("FKTeam cannot be null.");

        this.team = team;
    }

    public FKTeam getTeam() {
        return this.team;
    }
}
