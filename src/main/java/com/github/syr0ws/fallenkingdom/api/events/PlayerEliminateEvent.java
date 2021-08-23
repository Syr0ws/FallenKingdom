package com.github.syr0ws.fallenkingdom.api.events;

import com.github.syr0ws.fallenkingdom.api.model.teams.FKTeamPlayer;
import org.bukkit.event.HandlerList;

public class PlayerEliminateEvent extends FKTeamPlayerEvent {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public PlayerEliminateEvent(FKTeamPlayer player) {
        super(player);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
