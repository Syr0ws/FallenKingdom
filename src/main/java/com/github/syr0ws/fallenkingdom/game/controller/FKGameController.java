package com.github.syr0ws.fallenkingdom.game.controller;

import com.github.syr0ws.fallenkingdom.attributes.Attribute;
import com.github.syr0ws.fallenkingdom.attributes.AttributeObserver;
import com.github.syr0ws.fallenkingdom.events.*;
import com.github.syr0ws.fallenkingdom.game.GameException;
import com.github.syr0ws.fallenkingdom.game.GameInitializer;
import com.github.syr0ws.fallenkingdom.game.GameSettings;
import com.github.syr0ws.fallenkingdom.game.model.FKGame;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.GameState;
import com.github.syr0ws.fallenkingdom.game.model.attributes.GameCycleAttribute;
import com.github.syr0ws.fallenkingdom.game.model.capture.FKCapture;
import com.github.syr0ws.fallenkingdom.game.model.cycles.GameCycle;
import com.github.syr0ws.fallenkingdom.game.model.cycles.GameCycleFactory;
import com.github.syr0ws.fallenkingdom.game.model.modes.Mode;
import com.github.syr0ws.fallenkingdom.game.model.modes.impl.SpectatorMode;
import com.github.syr0ws.fallenkingdom.game.model.players.CraftGamePlayer;
import com.github.syr0ws.fallenkingdom.game.model.players.GamePlayer;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamState;
import com.github.syr0ws.fallenkingdom.listeners.ListenerManager;
import com.github.syr0ws.fallenkingdom.listeners.TeamListener;
import com.github.syr0ws.fallenkingdom.settings.Setting;
import com.github.syr0ws.fallenkingdom.settings.manager.SettingManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.*;

public class FKGameController implements GameController, AttributeObserver {

    private final Plugin plugin;
    private final ListenerManager manager;

    private FKGame game;
    private GameCycleFactory factory;

    public FKGameController(Plugin plugin) {
        this.plugin = plugin;
        this.manager = new ListenerManager(plugin);
    }

    public void init() throws GameException {

        GameInitializer initializer = new GameInitializer(this.plugin);

        // Initializing game.
        this.game = initializer.getGame();

        // Initializing factory.
        this.factory = new GameCycleFactory(this.game, this, this.plugin);

        // Adding listeners.
        this.manager.addListener(new GameListener());
        this.manager.addListener(new TeamListener(this.plugin));

        // Setting waiting cycle.
        this.setGameState(GameState.WAITING);
    }

    private void setGameState(GameState state) {

        this.game.setState(state);

        GameCycle current = this.game.getCycle();
        GameCycle cycle = this.factory.getCycle(state);

        if(cycle != null && current != null) {

            current.stop();
            current.unload();
            current.removeObserver(this);
        }

        if(cycle != null) {

            this.game.setCycle(cycle);

            cycle.addObserver(this);
            cycle.load();
            cycle.start();
        }
    }

    private void onGameState(GameState state) throws GameException {

        switch (state) {
            case STARTING:
                this.preStart();
                break;
            case RUNNING:
                this.startGame();
                break;
            case FINISHED:
                this.stopGame();
                break;
        }
    }

    private void addTeams() {

        for(GamePlayer player : this.game.getPlayers()) {

            if(this.game.hasTeam(player.getUUID())) continue;

            Team team = this.game.getTeams().stream()
                    .min(Comparator.comparingInt(Team::size))
                    .orElse(null); // Should not happen.

            try { this.setTeam(player, team);
            } catch (GameException e) { e.printStackTrace(); }
        }
    }

    @Override
    public void preStart() throws GameException {

        if(this.game.getState() != GameState.WAITING)
            throw new GameException("Game is not waiting.");

        this.setGameState(GameState.STARTING);
    }

    @Override
    public void startGame() throws GameException {

        if(this.game.isStarted())
            throw new GameException("A game is starting or already started.");

        this.addTeams();
        this.setGameState(GameState.RUNNING);
    }

    @Override
    public void stopGame() throws GameException {

        GameState current = this.game.getState();

        if(!this.game.isStarted())
            throw new GameException("No game started.");

        if(this.game.isFinished())
            throw new GameException("Game already finished.");

        if(current == GameState.STARTING) this.setGameState(GameState.WAITING);
        else this.setGameState(GameState.FINISHED);
    }

    @Override
    public void onJoin(Player player) throws GameException {

        UUID uuid = player.getUniqueId();

        GamePlayer gamePlayer = this.game.isGamePlayer(uuid) ? this.game.getGamePlayer(uuid) : new CraftGamePlayer(player);

        GamePlayerJoinEvent event = new GamePlayerJoinEvent(this.game, gamePlayer);

        PluginManager manager = Bukkit.getPluginManager();
        manager.callEvent(event);

        Mode mode = event.getMode();

        if(mode == null)
            throw new GameException("Mode cannot be null.");

        if(!this.game.isGamePlayer(uuid)) this.game.addGamePlayer(player, mode);
        else this.game.setGamePlayerMode(uuid, mode);
    }

    @Override
    public void onQuit(Player player) {

        UUID uuid = player.getUniqueId();
        GamePlayer gamePlayer = this.game.getGamePlayer(uuid);

        GamePlayerLeaveEvent event = new GamePlayerLeaveEvent(this.game, gamePlayer);

        PluginManager manager = Bukkit.getPluginManager();
        manager.callEvent(event);

        if(!gamePlayer.isPlaying()) this.game.removeGamePlayer(player);
    }

    @Override
    public TeamPlayer setTeam(GamePlayer player, Team team) throws GameException {

        if(this.game.isStarted())
            throw new GameException("A game is starting or already started.");

        if(!this.game.isValid(team))
            throw new GameException("Invalid team.");

        TeamPlayer teamPlayer = this.game.setTeam(player,team);

        TeamPlayerAddEvent event = new TeamPlayerAddEvent(this.game, teamPlayer.getTeam(), teamPlayer);

        PluginManager manager = Bukkit.getPluginManager();
        manager.callEvent(event);

        return teamPlayer;
    }

    @Override
    public TeamPlayer removeTeam(GamePlayer player) throws GameException {

        if(this.game.isStarted())
            throw new GameException("A game is starting or already started.");

        TeamPlayer teamPlayer = this.game.removeTeam(player);

        TeamPlayerRemoveEvent event = new TeamPlayerRemoveEvent(this.game, teamPlayer.getTeam(), teamPlayer);

        PluginManager manager = Bukkit.getPluginManager();
        manager.callEvent(event);

        return teamPlayer;
    }

    @Override
    public void startCapture(TeamPlayer catcher, Team captured) throws GameException {

        if(!this.game.isValid(catcher))
            throw new GameException("Invalid TeamPlayer.");

        if(!this.game.isValid(captured))
            throw new GameException("Invalid team.");

        if(captured.getState() != TeamState.ALIVE)
            throw new GameException("Team without base.");

        if(this.game.isCapturing(catcher))
            throw new GameException("TeamPlayer already capturing.");

        Optional<FKCapture> optional = this.game.getCapture(captured);

        if(!optional.isPresent()) {

            FKCapture capture = new FKCapture(catcher, captured);

            this.game.addCapture(capture);

            TeamBaseCaptureStartEvent event = new TeamBaseCaptureStartEvent(this.game, captured, catcher.getTeam());

            PluginManager manager = Bukkit.getPluginManager();
            manager.callEvent(event);

        } else {

            FKCapture capture = optional.get();

            if(!capture.getCapturer().contains(catcher))
                throw new GameException("Only one team can capture a base.");

            capture.addCapturer(catcher);
        }

        GamePlayer gamePlayer = this.game.getGamePlayer(catcher.getUUID());

        PlayerCaptureBaseStartEvent event = new PlayerCaptureBaseStartEvent(this.game, gamePlayer, catcher, catcher.getTeam());

        PluginManager manager = Bukkit.getPluginManager();
        manager.callEvent(event);
    }

    @Override
    public void stopCapture(TeamPlayer catcher) throws GameException {

        if(!this.game.isValid(catcher))
            throw new GameException("Invalid TeamPlayer.");

        Optional<FKCapture> optional = this.game.getCaptures().stream()
                .filter(capture -> capture.getCapturers().contains(catcher))
                .findFirst();

        if(!optional.isPresent())
            throw new GameException("TeamPlayer not capturing.");

        FKCapture capture = optional.get();
        capture.removeCapturer(catcher);

        SettingManager settingManager = this.game.getSettings();
        Setting<Integer> setting = settingManager.getGenericSetting(GameSettings.CATCHER_PERCENTAGE, Integer.class);

        int required = (int) Math.ceil((setting.getValue() * catcher.getTeam().size()) / (double) 100);

        PluginManager manager = Bukkit.getPluginManager();

        if(capture.getCapturers().size() < required) {

            this.game.removeCapture(capture);

            TeamBaseCaptureStopEvent event = new TeamBaseCaptureStopEvent(this.game, capture.getCaptured(), capture.getCapturer());

            manager.callEvent(event);
        }

        GamePlayer gamePlayer = this.game.getGamePlayer(catcher.getUUID());

        PlayerCaptureBaseStopEvent event = new PlayerCaptureBaseStopEvent(this.game, gamePlayer, catcher, catcher.getTeam());

        manager.callEvent(event);
    }

    @Override
    public void onBaseCapture(Team catcher, Team team) throws GameException {

        if(!this.game.isValid(catcher))
            throw new GameException("Invalid catcher team.");

        if(!this.game.isValid(team))
            throw new GameException("Invalid captured team.");

        if(team.getState() != TeamState.ALIVE)
            throw new GameException("Team without base.");

        FKTeam catcherFKTeam = (FKTeam) catcher;
        catcherFKTeam.setBaseCaptured();

        TeamBaseCapturedEvent event = new TeamBaseCapturedEvent(this.game, team, catcher);

        PluginManager manager = Bukkit.getPluginManager();
        manager.callEvent(event);
    }

    @Override
    public void win(Team team) {

    }

    @Override
    public void eliminate(Team team) {

    }

    @Override
    public void eliminate(TeamPlayer player) {

        if(!player.isAlive())
            throw new UnsupportedOperationException("TeamPlayer already eliminated.");

        GamePlayer gamePlayer = this.game.getGamePlayer(player.getUUID());

        Mode mode = new SpectatorMode(gamePlayer, this.game);

        this.game.setGamePlayerMode(player.getUUID(), mode);
    }

    @Override
    public GameModel getGame() {
        return this.game;
    }

    @Override
    public void onUpdate(Attribute attribute) {

        GameState current = this.game.getState();

        Optional<GameState> optional = current.getNext();

        optional.ifPresent(state -> {

            try { this.onGameState(state);
            } catch (GameException e) { e.printStackTrace(); }
        });
    }

    @Override
    public Collection<Attribute> observed() {
        return Collections.singleton(GameCycleAttribute.FINISH_STATE);
    }

    private class GameListener implements Listener {

        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {

            Player player = event.getPlayer();

            try {

                onJoin(player);

            } catch (GameException e) {

                e.printStackTrace();
                player.kickPlayer("Internal error");
            }
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {

            Player player = event.getPlayer();

            onQuit(player);
        }
    }
}
