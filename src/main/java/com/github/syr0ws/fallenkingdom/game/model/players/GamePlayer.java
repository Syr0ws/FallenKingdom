package com.github.syr0ws.fallenkingdom.game.model.players;

import com.github.syr0ws.fallenkingdom.game.model.modes.Mode;

public interface GamePlayer extends AbstractPlayer {

    Mode getMode();

    boolean isPlaying();
}
