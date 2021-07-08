package com.github.syr0ws.fallenkingdom.capture;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.capture.area.manager.CraftAreaCaptureManager;
import com.github.syr0ws.fallenkingdom.capture.nexus.controller.CraftNexusCaptureManager;
import com.github.syr0ws.fallenkingdom.game.controller.FKController;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;

public class CaptureFactory {

    private final FKGame game;
    private final FKModel model;
    private final FKController controller;

    public CaptureFactory(FKGame game, FKModel model, FKController controller) {

        if (game == null)
            throw new IllegalArgumentException("FKGame cannot be null.");

        if (model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        if (controller == null)
            throw new IllegalArgumentException("FKController cannot be null.");

        this.game = game;
        this.model = model;
        this.controller = controller;
    }

    public CaptureManager getCaptureManager(CaptureType type) {

        switch (type) {
            case AREA:
                return new CraftAreaCaptureManager(this.game, this.model, this.controller);
            case NEXUS:
                return new CraftNexusCaptureManager();
            default:
                throw new IllegalArgumentException(String.format("No capture manager found for type '%s'.", type.name()));
        }
    }
}
