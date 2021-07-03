package com.github.syr0ws.fallenkingdom.capture.area.model;

import com.github.syr0ws.fallenkingdom.game.model.v2.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.v2.teams.FKTeamPlayer;

import java.util.Collection;
import java.util.Optional;

public interface AreaCaptureModel {

    boolean isCaptured(FKTeam team);

    boolean isCapturing(FKTeamPlayer player);

    Optional<? extends AreaCapture> getCapture(FKTeam captured);

    Collection<? extends AreaCapture> getCaptures();
}
