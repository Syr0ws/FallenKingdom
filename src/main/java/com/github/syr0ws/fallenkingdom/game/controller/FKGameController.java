package com.github.syr0ws.fallenkingdom.game.controller;

import com.github.syr0ws.fallenkingdom.attributes.Attribute;
import com.github.syr0ws.fallenkingdom.attributes.AttributeObserver;
import com.github.syr0ws.fallenkingdom.events.GamePlayerJoinEvent;
import com.github.syr0ws.fallenkingdom.events.GamePlayerLeaveEvent;
import com.github.syr0ws.fallenkingdom.events.TeamPlayerAddEvent;
import com.github.syr0ws.fallenkingdom.events.TeamPlayerRemoveEvent;
import com.github.syr0ws.fallenkingdom.game.GameException;
import com.github.syr0ws.fallenkingdom.game.GameInitializer;
import com.github.syr0ws.fallenkingdom.game.model.FKGame;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.GameState;
import com.github.syr0ws.fallenkingdom.game.model.attributes.GameCycleAttribute;
import com.github.syr0ws.fallenkingdom.game.model.cycles.GameCycle;
import com.github.syr0ws.fallenkingdom.game.model.cycles.GameCycleFactory;
import com.github.syr0ws.fallenkingdom.game.model.modes.Mode;
import com.github.syr0ws.fallenkingdom.game.model.modes.impl.PlayingMode;
import com.github.syr0ws.fallenkingdom.game.model.modes.impl.SpectatorMode;
import com.github.syr0ws.fallenkingdom.game.model.players.CraftGamePlayer;
import com.github.syr0ws.fallenkingdom.game.model.players.GamePlayer;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;
import com.github.syr0ws.fallenkingdom.listeners.ListenerManager;
import com.github.syr0ws.fallenkingdom.listeners.TeamListener;
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
        this.factory = new GameCycleFactory(this.game, plugin);

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

            try { this.setTeam(player, team.getName());
            } catch (GameException e) { e.printStackTrace(); }
        }
    }

    private void setPlayingMode() {

        for(TeamPlayer player : this.game.getTeamPlayers()) {

            PlayingMode mode = new PlayingMode(player, this.game);

            this.game.setGamePlayerMode(player.getUUID(), mode);
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
        this.setPlayingMode();
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
    public TeamPlayer setTeam(GamePlayer player, String teamName) throws GameException {

        if(this.game.isStarted())
            throw new GameException("A game is starting or already started.");

        TeamPlayer teamPlayer = this.game.setTeam(player, teamName);

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
