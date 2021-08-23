package com.github.syr0ws.fallenkingdom.api.capture.area.model;

import com.github.syr0ws.fallenkingdom.api.capture.area.model.settings.CaptureSettingsAccessor;
import com.github.syr0ws.fallenkingdom.api.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.api.model.teams.FKTeamPlayer;

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
