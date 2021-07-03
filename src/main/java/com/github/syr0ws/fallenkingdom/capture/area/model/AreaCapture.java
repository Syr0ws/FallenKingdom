package com.github.syr0ws.fallenkingdom.capture.area.model;

import com.github.syr0ws.fallenkingdom.game.model.v2.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.v2.teams.FKTeamPlayer;

import java.util.Collection;

public interface AreaCapture {

    FKTeam getCapturedTeam();

    FKTeam getCatcherTeam();

    long getStartTime();

    boolean isCapturing(FKTeamPlayer player);

    Collection<FKTeamPlayer> getCatchers();
}
