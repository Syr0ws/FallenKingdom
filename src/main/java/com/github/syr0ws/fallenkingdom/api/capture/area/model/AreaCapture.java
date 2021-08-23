package com.github.syr0ws.fallenkingdom.api.capture.area.model;

import com.github.syr0ws.fallenkingdom.api.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.api.model.teams.FKTeamPlayer;

import java.util.Collection;

public interface AreaCapture {

    FKTeam getCapturedTeam();

    FKTeam getCatcherTeam();

    long getStartTime();

    boolean isCapturing(FKTeamPlayer player);

    Collection<FKTeamPlayer> getCatchers();
}
