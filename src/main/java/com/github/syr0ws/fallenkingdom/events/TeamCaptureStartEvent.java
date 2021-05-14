package com.github.syr0ws.fallenkingdom.events;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import org.bukkit.event.HandlerList;

public class TeamCaptureStartEvent extends TeamCaptureEvent {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public TeamCaptureStartEvent(GameModel game, Team team, Team capturer) {
        super(game, team, capturer);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
