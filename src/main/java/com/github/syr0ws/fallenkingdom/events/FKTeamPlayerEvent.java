package com.github.syr0ws.fallenkingdom.events;

import com.github.syr0ws.fallenkingdom.game.model.v2.teams.FKTeamPlayer;

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
