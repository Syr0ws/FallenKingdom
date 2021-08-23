package com.github.syr0ws.fallenkingdom.plugin.capture.nexus.controller;

import com.github.syr0ws.fallenkingdom.api.capture.CaptureManager;
import com.github.syr0ws.fallenkingdom.api.capture.CaptureType;
import com.github.syr0ws.fallenkingdom.api.model.teams.FKTeam;
import com.github.syr0ws.universe.api.game.model.GameException;

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
