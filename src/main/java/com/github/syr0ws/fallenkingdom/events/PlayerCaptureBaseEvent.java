package com.github.syr0ws.fallenkingdom.events;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.players.GamePlayer;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;

public abstract class PlayerCaptureBaseEvent extends GamePlayerEvent {

    private final TeamPlayer catcher;
    private final Team captured;

    public PlayerCaptureBaseEvent(GameModel game, GamePlayer gamePlayer, TeamPlayer catcher, Team captured) {
        super(game, gamePlayer);

        if(catcher == null)
            throw new IllegalArgumentException("TeamPlayer cannot be null.");

        if(captured == null)
            throw new IllegalArgumentException("Captured team cannot be null.");

        this.catcher = catcher;
        this.captured = captured;
    }

    public TeamPlayer getCatcher() {
        return this.catcher;
    }

    public Team getCapturedTeam() {
        return this.captured;
    }
}
