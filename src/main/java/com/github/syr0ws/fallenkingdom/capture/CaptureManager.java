package com.github.syr0ws.fallenkingdom.capture;

import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeam;
import com.github.syr0ws.universe.sdk.game.model.GameException;

public interface CaptureManager {

    void capture(FKTeam team, FKTeam catcher) throws GameException;

    void enable();

    void disable();

    CaptureType getCaptureType();
}
