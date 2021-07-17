package com.github.syr0ws.fallenkingdom.capture.area.model;

import com.github.syr0ws.fallenkingdom.capture.area.settings.CaptureSettingsAccessor;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeamPlayer;

import java.util.Collection;
import java.util.Optional;

public interface AreaCaptureModel {

    CaptureSettingsAccessor getSettings();

    boolean isCaptured(FKTeam team);

    boolean isCapturing(FKTeamPlayer player);

    boolean canCapture(FKTeam team, FKTeamPlayer player);

    Optional<? extends AreaCapture> getCapture(FKTeam captured);

    Collection<? extends AreaCapture> getCaptures();
}
