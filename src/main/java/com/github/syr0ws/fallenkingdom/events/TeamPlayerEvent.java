package com.github.syr0ws.fallenkingdom.events;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;

public abstract class TeamPlayerEvent extends TeamEvent {

    private final TeamPlayer player;

    public TeamPlayerEvent(GameModel game, Team team, TeamPlayer player) {
        super(game, team);

        if(player == null)
            throw new IllegalArgumentException("TeamPlayer cannot be null.");

        this.player = player;
    }

    public TeamPlayer getPlayer() {
        return this.player;
    }
}
