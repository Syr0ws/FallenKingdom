package com.github.syr0ws.fallenkingdom.game.model.v2.events;

import com.github.syr0ws.fallenkingdom.game.model.v2.teams.FKTeam;
import org.bukkit.event.HandlerList;

public class TeamEliminateEvent extends FKTeamEvent {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public TeamEliminateEvent(FKTeam team) {
        super(team);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
