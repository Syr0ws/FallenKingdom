package com.github.syr0ws.fallenkingdom.game.model.cycle;

import com.github.syr0ws.fallenkingdom.game.model.GameState;

public abstract class GameCycle {

    private boolean finished;

    public abstract void start();

    public abstract void stop();

    public abstract GameState getState();

    public void finish() {
        this.finished = true;
    }

    public boolean isFinished() {
        return this.finished;
    }
}
