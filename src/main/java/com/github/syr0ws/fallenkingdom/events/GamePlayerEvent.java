package com.github.syr0ws.fallenkingdom.events;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.GamePlayer;

public abstract class GamePlayerEvent extends GameEvent {

    private final GamePlayer player;

    public GamePlayerEvent(GameModel game, GamePlayer player) {
        super(game);
        this.player = player;
    }

    public GamePlayer getPlayer() {
        return this.player;
    }
}
