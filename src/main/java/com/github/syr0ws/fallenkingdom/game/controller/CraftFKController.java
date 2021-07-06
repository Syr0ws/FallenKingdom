package com.github.syr0ws.fallenkingdom.game.controller;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.capture.CaptureFactory;
import com.github.syr0ws.fallenkingdom.capture.CaptureManager;
import com.github.syr0ws.fallenkingdom.capture.CaptureType;
import com.github.syr0ws.fallenkingdom.events.*;
import com.github.syr0ws.fallenkingdom.game.model.CraftFKModel;
import com.github.syr0ws.fallenkingdom.game.model.CraftFKPlayer;
import com.github.syr0ws.fallenkingdom.game.model.GameState;
import com.github.syr0ws.fallenkingdom.game.model.cycles.GameCycleFactory;
import com.github.syr0ws.fallenkingdom.game.model.settings.SettingAccessor;
import com.github.syr0ws.fallenkingdom.game.model.teams.CraftFKTeam;
import com.github.syr0ws.fallenkingdom.game.model.teams.CraftFKTeamPlayer;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeamPlayer;
import com.github.syr0ws.universe.events.GamePlayerJoinEvent;
import com.github.syr0ws.universe.events.GamePlayerModeChangeEvent;
import com.github.syr0ws.universe.events.GamePlayerQuitEvent;
import com.github.syr0ws.universe.game.model.GameException;
import com.github.syr0ws.universe.game.model.GamePlayer;
import com.github.syr0ws.universe.game.model.cycle.GameCycle;
import com.github.syr0ws.universe.game.model.cycle.GameCycleState;
import com.github.syr0ws.universe.game.model.mode.DefaultModeType;
import com.github.syr0ws.universe.game.model.mode.Mode;
import com.github.syr0ws.universe.game.model.mode.ModeFactory;
import com.github.syr0ws.universe.game.model.mode.ModeType;
import com.github.syr0ws.universe.settings.types.MutableSetting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CraftFKController implements FKController {

    private final CraftFKModel model;
    private final GameCycleFactory factory;
    private final CaptureManager captureManager;

    public CraftFKController(FKGame game, CraftFKModel model) {

        if(game == null)
            throw new IllegalArgumentException("FKGame cannot be null.");

        if(model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        this.model = model;
        this.factory = new GameCycleFactory(game, model, this);

        // Registering listeners.
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new GameListener(), game);

        // Handling capture manager.
        SettingAccessor accessor = model.getSettings();
        MutableSetting<CaptureType> setting = accessor.getCaptureTypeSetting();

        this.captureManager = CaptureFactory.getCaptureManager(game, setting.getValue());

        // Handling game state.
        this.setGameState(GameState.WAITING);
    }

    private void onPlayerJoin(Player player) {

        CraftFKPlayer fkPlayer;

        if(!this.model.isGamePlayer(player.getUniqueId())) {

            fkPlayer = new CraftFKPlayer(player);
            this.model.addPlayer(fkPlayer); // Storing player.

        } else fkPlayer = this.model.getPlayer(player.getUniqueId());

        // Throwing an event.
        GamePlayerJoinEvent event = new GamePlayerJoinEvent(fkPlayer);
        Bukkit.getPluginManager().callEvent(event);

        // Handling mode.
        if(fkPlayer.getModeType() != null) {

            Mode mode = ModeFactory.getMode(fkPlayer.getModeType());
            mode.enable(player);
        }
    }

    private void onPlayerQuit(Player player) {

        CraftFKPlayer fkPlayer = this.model.getPlayer(player.getUniqueId());

        // - Removing the GamePlayer if game is waiting because storing data is useless.
        //   It is not sure that the player will effectively rejoin the game again.
        //
        // - Removing the GamePlayer if he's not playing.
        //   Reduce the amount of data stored.
        if(this.model.isWaiting() || !fkPlayer.isPlaying()) {

            this.model.removePlayer(fkPlayer);

            // If the player is in a team, removing him from it.
            if(this.model.hasTeam(fkPlayer)) {

                try { this.removeTeam(fkPlayer);
                } catch (GameException e) { e.printStackTrace(); }
            }
        }

        // Throwing an event.
        GamePlayerQuitEvent event = new GamePlayerQuitEvent(fkPlayer);
        Bukkit.getPluginManager().callEvent(event);

        // Disabling mode.
        Mode mode = ModeFactory.getMode(fkPlayer.getModeType());
        mode.disable(player);
    }

    private void setGameState(GameState state) {

        this.model.setState(state);

        // Actions on state change.
        if(state == GameState.RUNNING) this.onGameStart();

        Optional<GameCycle> optional = this.factory.getCycle(state);

        optional.ifPresent(cycle -> {

            // Actions on the old GameCycle.
            GameCycle current = this.model.getCycle();

            // Current game state can be null when setting WAITING state.
            if(current != null) {

                // If it is not stopped, stopping it.
                if(current.getState() != GameCycleState.STOPPED) current.stop();

                current.unload(); // Unloading cycle.
            }

            // Actions on the new GameCycle.
            this.model.setCycle(cycle);

            cycle.load(); // Loading cycle.
            cycle.start(); // Starting cycle.
        });
    }

    private void onGameStart() {

        // Adding players without team to a team.
        this.model.getPlayers().stream()
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
                    this.setMode(player, DefaultModeType.PLAYING);
                    this.model.getPlayer(player.getUUID()).setPlaying();
                });
    }

    private void addToRandomTeam(GamePlayer player) {

        // Finding the team with the minimum of players.
        FKTeam team = this.model.getTeams().stream()
                .min(Comparator.comparingInt(FKTeam::size))
                .orElse(null); // Should not happen.

        try { this.addTeam(player, team);
        } catch (GameException e) { e.printStackTrace(); }
    }

    @Override
    public void setMode(GamePlayer player, ModeType type) {

        if(player == null)
            throw new IllegalArgumentException("GamePlayer cannot be null.");

        if(type == null)
            throw new IllegalArgumentException("Mode cannot be null.");

        Mode mode = ModeFactory.getMode(type);

        CraftFKPlayer fkPlayer = (CraftFKPlayer) player;

        // Throwing an event.
        GamePlayerModeChangeEvent event = new GamePlayerModeChangeEvent(fkPlayer, mode);
        Bukkit.getPluginManager().callEvent(event);

        // Handling modes.
        ModeType old = fkPlayer.getModeType();

        // Removing old mode if it exists and is not the same as the new one.
        // Also checking that the player is online.
        if(old != null && !old.equals(mode.getType()) && player.isOnline())
            ModeFactory.getMode(old).disable(player.getPlayer());

        fkPlayer.setModeType(mode.getType());

        // Setting new mode if the player is online.
        if(player.isOnline()) mode.enable(player.getPlayer());
    }

    @Override
    public void startGame() throws GameException {

        if(!this.model.isWaiting())
            throw new GameException("Game already started.");

        this.setGameState(GameState.STARTING);
    }

    @Override
    public void stopGame() throws GameException {

        if(!this.model.isRunning())
            throw new GameException("No game is currently running.");

        this.setGameState(GameState.FINISHED);
    }

    @Override
    public FKTeamPlayer addTeam(GamePlayer player, FKTeam team) throws GameException {

        if(player == null)
            throw new IllegalArgumentException("GamePlayer cannot be null.");

        if(!this.model.isWaiting())
            throw new GameException("A player can be added to a team only when a game is not started.");

        if(!this.model.isValid(team))
            throw new GameException("Invalid team.");

        // If the player is already in a team, removing him from it.
        if(this.model.hasTeam(player)) this.removeTeam(player);

        FKTeamPlayer teamPlayer = this.model.setTeam(player, team);

        // Throwing an event.
        TeamPlayerAddEvent event = new TeamPlayerAddEvent(team, teamPlayer);
        Bukkit.getPluginManager().callEvent(event);

        return teamPlayer;
    }

    @Override
    public FKTeamPlayer removeTeam(GamePlayer player) throws GameException {

        if(player == null)
            throw new IllegalArgumentException("GamePlayer cannot be null.");

        if(!this.model.isWaiting())
            throw new GameException("A player can be removed from a team only when a game is not started.");

        FKTeamPlayer teamPlayer = this.model.removeTeam(player);

        // Throwing an event.
        TeamPlayerRemoveEvent event = new TeamPlayerRemoveEvent(teamPlayer.getTeam(), teamPlayer);
        Bukkit.getPluginManager().callEvent(event);

        return teamPlayer;
    }

    @Override
    public void setBaseCaptured(FKTeam team) {

    }

    @Override
    public void win(FKTeam team) throws GameException {

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
        this.setGameState(GameState.FINISHED);
    }

    @Override
    public void eliminate(FKTeam team) throws GameException {

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

        if(!this.model.isValid(player))
            throw new GameException("Invalid FKTeamPlayer.");

        if(!player.isAlive())
            throw new GameException("FKTeamPlayer already eliminated.");

        // If the player is valid, it is an instance of CraftFKTeamPlayer.
        CraftFKTeamPlayer craftFKTeamPlayer = (CraftFKTeamPlayer) player;
        craftFKTeamPlayer.setAlive(false);

        // Setting player in spectator mode.
        this.setMode(player, DefaultModeType.SPECTATOR);

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

    public class GameListener implements Listener {

        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
            CraftFKController.this.onPlayerJoin(event.getPlayer());
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            CraftFKController.this.onPlayerQuit(event.getPlayer());
        }
    }
}
