package com.github.syr0ws.fallenkingdom.game.model;

import java.util.Arrays;
import java.util.Optional;

public enum GameState {

    WAITING, STARTING, RUNNING, FINISHED;

    public Optional<GameState> getNext() {

        int next = this.ordinal() + 1;

        return Arrays.stream(GameState.values())
                .filter(state -> state.ordinal() == next)
                .findFirst();
    }
}
