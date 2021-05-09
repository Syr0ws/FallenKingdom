package com.github.syr0ws.fallenkingdom.game.model;

import com.github.syr0ws.fallenkingdom.attributes.Attribute;
import com.github.syr0ws.fallenkingdom.attributes.AttributeObservable;
import com.github.syr0ws.fallenkingdom.attributes.AttributeObserver;
import com.github.syr0ws.fallenkingdom.game.cycle.GameCycle;
import com.github.syr0ws.fallenkingdom.game.cycle.GameCycleAttribute;
import com.github.syr0ws.fallenkingdom.game.cycle.GameCycleFactory;
import com.github.syr0ws.fallenkingdom.teams.Team;
import com.github.syr0ws.fallenkingdom.tools.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class Game implements GameModel, AttributeObserver, AttributeObservable {

    private boolean pvp, assaults;

    private GameState state;
    private GameCycle cycle;

    private final GameCycleFactory factory;
    private final Location spawn;
    private final List<Team> teams;
    private final List<AttributeObserver> observers;

    // TODO Should plugin be here ?
    public Game(Plugin plugin, Location spawn, List<Team> teams) {
        this.spawn = spawn;
        this.teams = teams;
        this.observers = new ArrayList<>();
        this.factory = new GameCycleFactory(plugin, this);
        this.setState(GameState.WAITING); // Should be executed in last.
    }

    public void setState(GameState state) {

        // Prevent error when the game is initialized.
        if(this.cycle != null) this.cycle.stop();

        this.state = state;
        this.cycle = this.factory.getCycle(state);
        this.cycle.addObserver(this);
        this.cycle.start(); // Starting the new cycle.
    }

    @Override
    public GameCycle getCycle() {
        return this.cycle;
    }

    @Override
    public GameState getState() {
        return this.state;
    }

    @Override
    public Location getSpawn() {
        return this.spawn;
    }

    @Override
    public boolean isPvPEnabled() {
        return this.pvp;
    }

    @Override
    public boolean areAssaultsEnabled() {
        return this.assaults;
    }

    @Override
    public boolean isInsideEnemyBase(Player player) {
        return false; // TODO to change.
    }

    @Override
    public boolean isInsideBase(Location location) {
        return this.teams.stream()
                .map(team -> team.getBase().getCuboid())
                .anyMatch(cuboid -> cuboid.isIn(location));
    }

    @Override
    public boolean hasTeam(Player player) {
        return this.teams.stream().anyMatch(team -> team.contains(player));
    }

    @Override
    public Optional<Team> getTeamFromLocation(Location location) {
        return this.teams.stream()
                .filter(team -> team.getBase().getCuboid().isIn(location))
                .findFirst();
    }

    @Override
    public Optional<Team> getTeam(Player player) {
        return this.teams.stream()
                .filter(team -> team.contains(player))
                .findFirst();
    }

    @Override
    public Optional<Team> getTeamByName(String name) {
        return this.teams.stream()
                .filter(team -> team.getName().equals(name))
                .findFirst();
    }

    @Override
    public List<Team> getTeams() {
        return Collections.unmodifiableList(this.teams);
    }

    @Override
    public void setTeam(Player player, Team team) {

        // Removing player from his current team if it has one.
        this.removeTeam(player);

        team.addPlayer(player);
    }

    @Override
    public void removeTeam(Player player) {
        this.getTeam(player).ifPresent(current -> current.removePlayer(player));
    }

    @Override
    public void setPvPEnabled(boolean enabled) {
        this.pvp = enabled;
        this.notifyChange(GameAttribute.PVP_STATE);
    }

    @Override
    public void setAssaultsEnabled(boolean enabled) {
        this.assaults = enabled;
        this.notifyChange(GameAttribute.ASSAULTS_STATE);
    }

    @Override
    public void onUpdate(Attribute attribute) {

        Optional<GameState> optional = this.state.getNext();
        optional.ifPresent(this::setState);
    }

    @Override
    public Collection<Attribute> observed() {
        return Collections.singleton(GameCycleAttribute.FINISH_STATE);
    }

    @Override
    public void notifyChange(Attribute attribute) {
        this.observers.stream()
                .filter(observer -> observer.observed().contains(attribute))
                .forEach(observer -> observer.onUpdate(attribute));
    }

    @Override
    public void addObserver(AttributeObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(AttributeObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public Collection<AttributeObserver> getObservers() {
        return Collections.unmodifiableList(this.observers);
    }
}
