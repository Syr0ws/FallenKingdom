package com.github.syr0ws.fallenkingdom.game.model.v2;

import com.github.syr0ws.universe.game.model.GamePlayer;
import com.github.syr0ws.universe.game.model.mode.ModeType;

public interface FKPlayer extends GamePlayer {

    ModeType getModeType();
}
