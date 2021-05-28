package com.github.syr0ws.fallenkingdom.game.model.capture;

import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;

import java.util.Collection;

public interface Capture {

    void addCaptureTime();

    int getCaptureTime();

    Team getCapturer();

    Team getCaptured();

    Collection<TeamPlayer> getCapturers();
}
