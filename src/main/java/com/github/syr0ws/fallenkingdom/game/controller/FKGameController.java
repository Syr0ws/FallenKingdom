package com.github.syr0ws.fallenkingdom.game.controller;

import com.github.syr0ws.fallenkingdom.attributes.Attribute;
import com.github.syr0ws.fallenkingdom.attributes.AttributeObserver;
import com.github.syr0ws.fallenkingdom.events.*;
import com.github.syr0ws.fallenkingdom.game.GameException;
import com.github.syr0ws.fallenkingdom.game.model.FKGame;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.GamePlayer;
import com.github.syr0ws.fallenkingdom.game.model.GameState;
import com.github.syr0ws.fallenkingdom.game.model.attributes.GameCycleAttribute;
import com.github.syr0ws.fallenkingdom.game.model.cycle.FKCycleFactory;
import com.github.syr0ws.fallenkingdom.game.model.cycle.GameCycle;
import com.github.syr0ws.fallenkingdom.game.model.cycle.GameCycleFactory;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamException;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;
import com.github.syr0ws.fallenkingdom.game.model.teams.dao.ConfigTeamDAO;
import com.github.syr0ws.fallenkingdom.game.model.teams.dao.TeamDAO;
import com.github.syr0ws.fallenkingdom.listeners.GlobalListener;
import com.github.syr0ws.fallenkingdom.listeners.ListenerManager;
import com.github.syr0ws.fallenkingdom.listeners.TeamListener;
import com.github.syr0ws.fallenkingdom.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.*;

public class FKGameController implements GameController, AttributeObserver {

    private final Plugin plugin;
    private final ListenerManager manager;
    private GameModel game;
    private GameCycleFactory factory;

    public FKGameController(Plugin plugin) {
        this.plugin = plugin;
        this.manager = new ListenerManager(plugin);
    }

    public void initGame() throws GameException {

        Location spawn = this.loadSpawn();
        List<Team> teams = this.loadTeams();

        if(teams.size() <= 2)
            throw new GameException("Number of teams cannot be lower than 2.");

        // Initializing game.
        this.game = new FKGame(spawn, teams);

        // Initializing factory.
        this.factory = new FKCycleFactory(this.game, plugin);

        // Adding listeners.
        this.manager.addListener(new GlobalListener(this.plugin, this));
        this.manager.addListener(new TeamListener(this.plugin));

        // Setting waiting cycle.
        GameCycle cycle = this.factory.getCycle(GameState.WAITING);
        this.game.setCycle(cycle);
    }

    @Override
    public void startGame() throws GameException {

        if(this.game.isStarted())
            throw new GameException("A game is already started.");

        this.setGameState(GameState.STARTING);
    }

    @Override
    public void stopGame() throws GameException {

        GameState current = this.game.getState();

        if(current == GameState.STARTING) this.setGameState(GameState.WAITING);
        else this.setGameState(GameState.FINISHED);
    }

    @Override
    public void onJoin(Player player) {

        GamePlayer gamePlayer = new GamePlayer(player);
        this.game.join(gamePlayer);
    }

    @Override
    public void onQuit(Player player) {

        if(this.game.isStarted()) return;

        GamePlayer gamePlayer = this.game.getGamePlayer(player);
        this.game.leave(gamePlayer);
    }

    @Override
    public void setTeam(Player player, Team team) {

        GamePlayer gamePlayer = this.game.getGamePlayer(player);
        TeamPlayer teamPlayer = this.game.setTeam(gamePlayer, team);

        this.callEvent(new TeamPlayerAddEvent(this.game, team, teamPlayer));
    }

    @Override
    public void removeTeam(Player player) {

        Optional<TeamPlayer> optional = this.game.getTeamPlayer(player);

        optional.ifPresent(teamPlayer -> {

            Team team = this.game.removeTeam(teamPlayer);
            this.callEvent(new TeamPlayerRemoveEvent(this.game, team, teamPlayer));
        });
    }

    @Override
    public void win(Team team) {

        this.callEvent(new TeamWinEvent(this.game, team));
    }

    @Override
    public void eliminate(Team team) {

        this.callEvent(new TeamEliminateEvent(this.game, team));
    }

    @Override
    public void eliminate(TeamPlayer player) {

        this.callEvent(new TeamPlayerEliminateEvent(this.game, player.getTeam(), player));
    }

    @Override
    public GameModel getGame() {
        return this.game;
    }

    @Override
    public void onUpdate(Attribute attribute) {

        GameState current = this.game.getState();

        Optional<GameState> optional = current.getNext();
        optional.ifPresent(this::setGameState);
    }

    @Override
    public Collection<Attribute> observed() {
        return Collections.singleton(GameCycleAttribute.FINISH_STATE);
    }

    private void setGameState(GameState state) {

        GameCycle cycle = this.factory.getCycle(state);
        this.game.setCycle(cycle);
    }

    private List<Team> loadTeams() {

        TeamDAO dao = new ConfigTeamDAO(this.plugin);

        List<Team> teams = new ArrayList<>();

        try {

            Collection<Team> collection = dao.loadTeams();
            teams.addAll(collection);

        } catch (TeamException e) { e.printStackTrace(); }

        return teams;
    }

    private Location loadSpawn() {

        FileConfiguration config = this.plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection("spawn");

        return LocationUtils.getLocation(section);
    }

    private void callEvent(GameEvent event) {
        PluginManager manager = Bukkit.getPluginManager();
        manager.callEvent(event);
    }
}
