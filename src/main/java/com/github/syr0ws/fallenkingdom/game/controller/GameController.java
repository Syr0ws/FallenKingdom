package com.github.syr0ws.fallenkingdom.game.controller;

import com.github.syr0ws.fallenkingdom.game.GameException;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;
import org.bukkit.entity.Player;

public interface GameController {

    void startGame() throws GameException;

    void stopGame() throws GameException;

    void onJoin(Player player);

    void onQuit(Player player);

    void setTeam(Player player, Team team);

    void removeTeam(Player player);

    void win(Team team);

    void eliminate(Team team);

    void eliminate(TeamPlayer player);

    GameModel getGame();
}
