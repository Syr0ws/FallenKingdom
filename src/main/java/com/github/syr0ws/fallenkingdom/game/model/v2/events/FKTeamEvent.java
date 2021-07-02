package com.github.syr0ws.fallenkingdom.game.model.v2.events;

import com.github.syr0ws.fallenkingdom.game.model.v2.teams.FKTeam;

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
