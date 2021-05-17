package com.github.syr0ws.fallenkingdom.events;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.GamePlayer;
import com.github.syr0ws.fallenkingdom.game.modes.Mode;
import org.bukkit.event.HandlerList;

public class GamePlayerJoinEvent extends GamePlayerEvent {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private Mode mode;

    public GamePlayerJoinEvent(GameModel game, GamePlayer player) {
        super(game, player);
    }

    public Mode getMode() {
        return this.mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
