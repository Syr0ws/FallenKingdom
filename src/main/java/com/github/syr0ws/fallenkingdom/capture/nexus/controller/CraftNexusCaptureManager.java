package com.github.syr0ws.fallenkingdom.capture.nexus.controller;

import com.github.syr0ws.fallenkingdom.capture.CaptureManager;
import com.github.syr0ws.fallenkingdom.capture.CaptureType;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeam;
import com.github.syr0ws.universe.sdk.game.model.GameException;

public class CraftNexusCaptureManager implements CaptureManager {

    @Override
    public void capture(FKTeam team, FKTeam catcher) throws GameException {

    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public CaptureType getCaptureType() {
        return CaptureType.NEXUS;
    }
}
