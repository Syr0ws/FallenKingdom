package com.github.syr0ws.fallenkingdom.game.model.cycle;

import com.github.syr0ws.fallenkingdom.game.model.GameState;

public interface GameCycleFactory {

    GameCycle getCycle(GameState state);
}
