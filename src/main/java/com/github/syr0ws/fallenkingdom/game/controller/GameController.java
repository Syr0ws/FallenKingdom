package com.github.syr0ws.fallenkingdom.game.controller;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;

public interface GameController {

    void startGame();

    void stopGame();

    GameModel getModel();
}
