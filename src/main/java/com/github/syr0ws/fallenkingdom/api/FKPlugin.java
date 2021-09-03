package com.github.syr0ws.fallenkingdom.api;

import com.github.syr0ws.fallenkingdom.api.controller.FKController;
import com.github.syr0ws.fallenkingdom.api.model.FKModel;
import com.github.syr0ws.universe.api.GamePlugin;

public interface FKPlugin extends GamePlugin {

    @Override
    FKModel getGameModel();

    @Override
    FKController getGameController();
}
