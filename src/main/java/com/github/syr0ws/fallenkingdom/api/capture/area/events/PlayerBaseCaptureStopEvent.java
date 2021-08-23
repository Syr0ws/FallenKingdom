package com.github.syr0ws.fallenkingdom.api.capture.area.events;

import com.github.syr0ws.fallenkingdom.api.events.FKTeamPlayerEvent;
import com.github.syr0ws.fallenkingdom.api.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.api.model.teams.FKTeamPlayer;
import org.bukkit.event.HandlerList;

public class PlayerBaseCaptureStopEvent extends FKTeamPlayerEvent {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private final FKTeam team;

    public PlayerBaseCaptureStopEvent(FKTeamPlayer player, FKTeam team) {
        super(player);

        if(team == null)
            throw new IllegalArgumentException("Captured team cannot be null.");

        this.team = team;
    }

    public FKTeam getCapturedTeam() {
        return this.team;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
