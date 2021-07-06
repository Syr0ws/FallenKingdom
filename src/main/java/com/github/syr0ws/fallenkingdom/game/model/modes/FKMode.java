package com.github.syr0ws.fallenkingdom.game.model.modes;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.universe.game.model.mode.Mode;

public abstract class FKMode implements Mode {

    private final FKGame game;

    public FKMode(FKGame game) {

        if(game == null)
            throw new IllegalArgumentException("FKGame cannot be null.");

        this.game = game;
    }

    public FKGame getGame() {
        return this.game;
    }
}
