package com.github.syr0ws.fallenkingdom.game.controller;

import com.github.syr0ws.fallenkingdom.capture.CaptureManager;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeamPlayer;
import com.github.syr0ws.universe.game.controller.GameController;
import com.github.syr0ws.universe.game.model.GameException;
import com.github.syr0ws.universe.game.model.GamePlayer;

public interface FKController extends GameController {

    FKTeamPlayer addTeam(GamePlayer player, FKTeam team) throws GameException;

    FKTeamPlayer removeTeam(GamePlayer player) throws GameException;

    void setBaseCaptured(FKTeam team);

    void win(FKTeam team) throws GameException;

    void eliminate(FKTeam team) throws GameException;

    void eliminate(FKTeamPlayer player) throws GameException;

    CaptureManager getCaptureManager();
}
