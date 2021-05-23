package com.github.syr0ws.fallenkingdom.events;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.players.GamePlayer;
import org.bukkit.event.HandlerList;

public class GamePlayerLeaveEvent extends GamePlayerEvent {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public GamePlayerLeaveEvent(GameModel game, GamePlayer player) {
        super(game, player);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
