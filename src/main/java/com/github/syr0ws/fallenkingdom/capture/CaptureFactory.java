package com.github.syr0ws.fallenkingdom.capture;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.capture.area.manager.CraftAreaCaptureManager;
import com.github.syr0ws.fallenkingdom.capture.nexus.controller.CraftNexusCaptureManager;

public class CaptureFactory {

    public static CaptureManager getCaptureManager(FKGame game, CaptureType type) {

        switch (type) {
            case AREA:
                return new CraftAreaCaptureManager(game);
            case NEXUS:
                return new CraftNexusCaptureManager();
            default:
                throw new IllegalArgumentException(String.format("No capture manager found for type '%s'.", type.name()));
        }
    }
}
