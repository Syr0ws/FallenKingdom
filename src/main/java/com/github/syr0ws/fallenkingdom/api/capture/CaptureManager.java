package com.github.syr0ws.fallenkingdom.api.capture;

import com.github.syr0ws.fallenkingdom.api.model.teams.FKTeam;
import com.github.syr0ws.universe.api.game.model.GameException;

public interface CaptureManager {

    void capture(FKTeam team, FKTeam catcher) throws GameException;

    void enable();

    void disable();

    CaptureType getCaptureType();
}
