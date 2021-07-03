package com.github.syr0ws.fallenkingdom.capture.area.events;

import com.github.syr0ws.fallenkingdom.events.FKTeamPlayerEvent;
import com.github.syr0ws.fallenkingdom.game.model.v2.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.v2.teams.FKTeamPlayer;
import org.bukkit.event.HandlerList;

public class PlayerBaseCaptureStartEvent extends FKTeamPlayerEvent {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private final FKTeam team;

    public PlayerBaseCaptureStartEvent(FKTeamPlayer player, FKTeam team) {
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
