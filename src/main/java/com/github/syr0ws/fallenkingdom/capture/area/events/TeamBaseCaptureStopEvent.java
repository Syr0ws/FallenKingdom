package com.github.syr0ws.fallenkingdom.capture.area.events;

import com.github.syr0ws.fallenkingdom.events.TeamBaseCaptureEvent;
import com.github.syr0ws.fallenkingdom.game.model.v2.teams.FKTeam;
import org.bukkit.event.HandlerList;

public class TeamBaseCaptureStopEvent extends TeamBaseCaptureEvent {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public TeamBaseCaptureStopEvent(FKTeam team, FKTeam catcher) {
        super(team, catcher);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
