package com.github.syr0ws.fallenkingdom.game.controller;

import com.github.syr0ws.fallenkingdom.game.GameException;

public interface GameController {

    void startGame() throws GameException;

    void stopGame() throws GameException;
}
