package com.github.syr0ws.fallenkingdom.events;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;

public abstract class TeamEvent extends GameEvent {

    private final Team team;

    public TeamEvent(GameModel game, Team team) {
        super(game);

        if(team == null)
            throw new IllegalArgumentException("Team cannot be null.");

        this.team = team;
    }

    public Team getTeam() {
        return this.team;
    }
}
