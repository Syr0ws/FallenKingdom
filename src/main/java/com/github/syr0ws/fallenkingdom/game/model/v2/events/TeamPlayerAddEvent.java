package com.github.syr0ws.fallenkingdom.game.model.v2.events;

import com.github.syr0ws.fallenkingdom.game.model.v2.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.v2.teams.FKTeamPlayer;
import org.bukkit.event.HandlerList;

public class TeamPlayerAddEvent extends FKTeamEvent {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private final FKTeamPlayer player;

    public TeamPlayerAddEvent(FKTeam team, FKTeamPlayer player) {
        super(team);

        if(player == null)
            throw new IllegalArgumentException("FKTeamPlayer cannot be null.");

        this.player = player;
    }

    public FKTeamPlayer getPlayer() {
        return this.player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
