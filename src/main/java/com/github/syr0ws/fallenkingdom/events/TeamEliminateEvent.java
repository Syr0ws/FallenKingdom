package com.github.syr0ws.fallenkingdom.events;

import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeam;
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
