package com.github.syr0ws.fallenkingdom.events;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.players.GamePlayer;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;
import org.bukkit.event.HandlerList;

public class PlayerCaptureBaseStartEvent extends PlayerCaptureBaseEvent {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public PlayerCaptureBaseStartEvent(GameModel game, GamePlayer gamePlayer, TeamPlayer catcher, Team captured) {
        super(game, gamePlayer, catcher, captured);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
