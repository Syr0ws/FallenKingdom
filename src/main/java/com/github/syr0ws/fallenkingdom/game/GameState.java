package com.github.syr0ws.fallenkingdom.game;

import java.util.Arrays;
import java.util.Optional;

public enum GameState {

    WAITING(0), STARTING(1), RUNNING(2), FINISHED(3);

    private final int position;

    GameState(int position) {
        this.position = position;
    }

    public Optional<GameState> getNext() {
        return Arrays.stream(GameState.values())
                .filter(state -> state.position > this.position)
                .findFirst();
    }

    public int getPosition() {
        return this.position;
    }
}
