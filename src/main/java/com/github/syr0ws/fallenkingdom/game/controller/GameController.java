package com.github.syr0ws.fallenkingdom.game.controller;

import com.github.syr0ws.fallenkingdom.game.GameException;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.players.GamePlayer;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;
import org.bukkit.entity.Player;

public interface GameController {

    void startGame() throws GameException;

    void stopGame() throws GameException;

    void onJoin(Player player) throws GameException;

    void onQuit(Player player);

    TeamPlayer setTeam(GamePlayer player, String teamName) throws GameException;

    TeamPlayer removeTeam(GamePlayer player) throws GameException;

    void win(Team team);

    void eliminate(Team team);

    void eliminate(TeamPlayer player);

    GameModel getGame();
}
