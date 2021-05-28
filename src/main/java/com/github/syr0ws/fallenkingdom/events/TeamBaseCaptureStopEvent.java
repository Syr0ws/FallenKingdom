package com.github.syr0ws.fallenkingdom.events;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import org.bukkit.event.HandlerList;

public class TeamBaseCaptureStopEvent extends TeamBaseCaptureEvent {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public TeamBaseCaptureStopEvent(GameModel game, Team team, Team catcher) {
        super(game, team, catcher);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
