package com.github.syr0ws.fallenkingdom.events;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import org.bukkit.event.Event;

public abstract class GameEvent extends Event {

    private final GameModel game;

    public GameEvent(GameModel game) {

        if(game == null)
            throw new IllegalArgumentException("Game cannot be null.");

        this.game = game;
    }

    public GameModel getGame() {
        return this.game;
    }
}
