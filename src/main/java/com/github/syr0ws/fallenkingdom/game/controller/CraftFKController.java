package com.github.syr0ws.fallenkingdom.game.controller;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.capture.CaptureFactory;
import com.github.syr0ws.fallenkingdom.capture.CaptureManager;
import com.github.syr0ws.fallenkingdom.capture.CaptureType;
import com.github.syr0ws.fallenkingdom.events.*;
import com.github.syr0ws.fallenkingdom.game.cycles.FKCycleFactory;
import com.github.syr0ws.fallenkingdom.game.model.CraftFKModel;
import com.github.syr0ws.fallenkingdom.game.model.FKPlayer;
import com.github.syr0ws.fallenkingdom.game.model.settings.FKSettings;
import com.github.syr0ws.fallenkingdom.game.model.teams.*;
import com.github.syr0ws.universe.commons.controller.DefaultGameController;
import com.github.syr0ws.universe.commons.mode.DefaultModeType;
import com.github.syr0ws.universe.sdk.game.cycle.GameCycleFactory;
import com.github.syr0ws.universe.sdk.game.model.GameException;
import com.github.syr0ws.universe.sdk.game.model.GamePlayer;
import com.github.syr0ws.universe.sdk.game.model.GameState;
import com.github.syr0ws.universe.sdk.settings.types.MutableSetting;
import org.bukkit.Bukkit;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CraftFKController extends DefaultGameController implements FKController {

    private final FKGame game;
    private final CraftFKModel model;
    private final CaptureManager captureManager;
    private final GameCycleFactory factory;

    public CraftFKController(FKGame game, CraftFKModel model) {
        super(game, model);

        this.game = game;
        this.model = model;

        // Initializing cycle factory.
        this.factory = new FKCycleFactory(game, model, this);

        // Initializing capture manager.
        this.captureManager = this.createCaptureManager();

        // Handling game state.
        super.setGameState(GameState.WAITING);
    }

    @Override
    public GameCycleFactory getCycleFactory() {
        return this.factory;
    }

    @Override
    protected void setGameState(GameState state) {

        if(state == GameState.RUNNING) this.onGameStart();

        super.setGameState(state);
    }

    private void onGameStart() {

        // Adding players without team to a team.
        this.model.getPlayers().stream()
                .map(player -> (FKPlayer) player)
                .filter(player -> !this.model.hasTeam(player))
                .forEach(this::addToRandomTeam);

        // Eliminating empty teams.
        this.model.getTeams().stream()
                .filter(team -> team.getTeamPlayers().size() == 0)
                .forEach(CraftFKTeam::eliminate);

        // Setting playing mode to all players.
        this.model.getTeams().stream()
                .flatMap(team -> team.getTeamPlayers().stream())
                .forEach(player -> {

                    FKTeam team = player.getTeam();
                    GamePlayer gamePlayer = player.getFKPlayer();

                    this.setMode(gamePlayer, DefaultModeType.PLAYING);
                    this.model.getPlayer(player.getUUID()).setPlaying();

                    player.getPlayer().teleport(team.getBase().getSpawn());
                });
    }

    private void addToRandomTeam(FKPlayer player) {

        // Finding the team with the minimum of players.
        FKTeam team = this.model.getTeams().stream()
                .min(Comparator.comparingInt(FKTeam::size))
                .orElse(null); // Should not happen.

        try { this.addTeam(player, team);
        } catch (GameException e) { e.printStackTrace(); }
    }

    private CaptureManager createCaptureManager() {

        FKSettings accessor = this.model.getSettings();
        MutableSetting<CaptureType> setting = accessor.getCaptureTypeSetting();

        CaptureFactory factory = new CaptureFactory(this.game, this.model, this);

        return factory.getCaptureManager(setting.getValue());
    }

    @Override
    public FKTeamPlayer addTeam(FKPlayer player, FKTeam team) throws GameException {

        if(player == null)
            throw new IllegalArgumentException("GamePlayer cannot be null.");

        if(!this.model.isValid(team))
            throw new GameException("Invalid team.");

        if(this.model.isStarted())
            throw new GameException("A player can be added to a team only when a game is not started.");

        // If the player is already in a team, removing him from it.
        if(this.model.hasTeam(player)) this.removeTeam(player);

        FKTeamPlayer teamPlayer = this.model.setTeam(player, team);

        // Throwing an event.
        TeamPlayerAddEvent event = new TeamPlayerAddEvent(team, teamPlayer);
        Bukkit.getPluginManager().callEvent(event);

        return teamPlayer;
    }

    @Override
    public FKTeamPlayer removeTeam(FKPlayer player) throws GameException {

        if(player == null)
            throw new IllegalArgumentException("GamePlayer cannot be null.");

        if(this.model.isStarted())
            throw new GameException("A player can be removed from a team only when a game is not started.");

        FKTeamPlayer teamPlayer = this.model.removeTeam(player);

        // Throwing an event.
        TeamPlayerRemoveEvent event = new TeamPlayerRemoveEvent(teamPlayer.getTeam(), teamPlayer);
        Bukkit.getPluginManager().callEvent(event);

        return teamPlayer;
    }

    @Override
    public void setBaseCaptured(FKTeam team, FKTeam catcher) throws GameException {

        if(!this.model.isRunning())
            throw new GameException("Game not running.");

        if(!this.model.isValid(team))
            throw new GameException("Invalid team.");

        if(team.getState() != TeamState.ALIVE)
            throw new GameException("Team not alive.");

        CraftFKTeam fkTeam = (CraftFKTeam) team;
        fkTeam.setBaseCaptured();

        TeamBaseCapturedEvent event = new TeamBaseCapturedEvent(team, catcher);
        Bukkit.getPluginManager().callEvent(event);
    }

    @Override
    public void win(FKTeam team) throws GameException {

        if(!this.model.isStarted())
            throw new GameException("Game not started.");

        if(!this.model.isValid(team))
            throw new GameException("Invalid team.");

        if(team.isEliminated())
            throw new GameException("An eliminated team cannot win.");

        // Eliminating other remaining teams.
        this.eliminateTeams(remaining -> !remaining.equals(team) && !remaining.isEliminated());

        // Throwing an event.
        TeamWinEvent event = new TeamWinEvent(team);
        Bukkit.getPluginManager().callEvent(event);

        // When a team has win, the game is finished.
        super.setGameState(GameState.FINISHED);
    }

    @Override
    public void eliminate(FKTeam team) throws GameException {

        if(!this.model.isStarted())
            throw new GameException("Game not started.");

        if(!this.model.isValid(team))
            throw new GameException("Invalid FKTeam.");

        if(team.isEliminated())
            throw new GameException("FKTeam already eliminated.");

        // If the team is valid, it is an instance of CraftFKTeam.
        CraftFKTeam fkTeam = (CraftFKTeam) team;
        fkTeam.eliminate();

        // When a team is eliminated, all the alive players of the team must be eliminated too.
        this.eliminatePlayers(fkTeam);

        // Checking if a team has win.
        this.checkTeamWin();

        // Throwing an event.
        TeamEliminateEvent event = new TeamEliminateEvent(team);
        Bukkit.getPluginManager().callEvent(event);
    }

    @Override
    public void eliminate(FKTeamPlayer player) throws GameException {

        if(!this.model.isStarted())
            throw new GameException("Game not started.");

        if(!this.model.isValid(player))
            throw new GameException("Invalid FKTeamPlayer.");

        if(!player.isAlive())
            throw new GameException("FKTeamPlayer already eliminated.");

        // If the player is valid, it is an instance of CraftFKTeamPlayer.
        CraftFKTeamPlayer craftFKTeamPlayer = (CraftFKTeamPlayer) player;
        craftFKTeamPlayer.setAlive(false);

        // Throwing an event.
        PlayerEliminateEvent event = new PlayerEliminateEvent(player);
        Bukkit.getPluginManager().callEvent(event);

        // Checking if the team has players alive.
        long playersAlive = player.getTeam().getTeamPlayers().stream()
                .filter(FKTeamPlayer::isAlive)
                .count();

        // If there is no player alive, eliminating the team.
        if(playersAlive == 0) this.eliminate(player.getTeam());
    }

    @Override
    public CaptureManager getCaptureManager() {
        return this.captureManager;
    }

    private void checkTeamWin() throws GameException {

        List<CraftFKTeam> teams = this.model.getTeams()
                .stream()
                .filter(team -> !team.isEliminated())
                .collect(Collectors.toList());

        // Checking if there is only one remaining team.
        if(teams.size() != 1) return;

        // Retrieving the last remaining team.
        CraftFKTeam team = teams.get(0);

        this.win(team);
    }

    private void eliminatePlayers(CraftFKTeam team) {

        for(CraftFKTeamPlayer player : team.getTeamPlayers()) {

            if(!player.isAlive()) continue;

            try { this.eliminate(player);
            } catch (GameException e) { e.printStackTrace(); }
        }
    }

    private void eliminateTeams(Predicate<FKTeam> predicate) {

        for(CraftFKTeam team : this.model.getTeams()) {

            if(!predicate.test(team)) continue;

            try { this.eliminate(team);
            } catch (GameException e) { e.printStackTrace(); }
        }
    }
}
