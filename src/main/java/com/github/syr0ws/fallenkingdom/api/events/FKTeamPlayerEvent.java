package com.github.syr0ws.fallenkingdom.api.events;

import com.github.syr0ws.fallenkingdom.api.model.teams.FKTeamPlayer;

public abstract class FKTeamPlayerEvent extends FKEvent {

    private final FKTeamPlayer player;

    public FKTeamPlayerEvent(FKTeamPlayer player) {

        if(player == null)
            throw new IllegalArgumentException("FKTeamPlayer cannot be null.");

        this.player = player;
    }

    public FKTeamPlayer getTeamPlayer() {
        return this.player;
    }
}
