package com.github.syr0ws.fallenkingdom.game.model;

import com.github.syr0ws.fallenkingdom.attributes.Attribute;
import com.github.syr0ws.fallenkingdom.attributes.AttributeObserver;
import com.github.syr0ws.fallenkingdom.game.model.attributes.GameAttribute;
import com.github.syr0ws.fallenkingdom.game.model.cycle.GameCycle;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class FKGame implements GameModel {

    private final Location spawn;
    private final List<Team> teams;
    private final List<GamePlayer> players;
    private final List<AttributeObserver> observers;

    private GameCycle cycle;
    private boolean pvp, assaults;

    public FKGame(Location spawn, List<Team> teams) {
        this.spawn = spawn;
        this.teams = teams;
        this.players = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    @Override
    public void notifyChange(Attribute attribute) {
        this.observers.stream()
                .filter(observer -> observer.observed().contains(attribute))
                .forEach(observer -> observer.onUpdate(attribute));
    }

    @Override
    public void addObserver(AttributeObserver observer) {
        if(!this.observers.contains(observer)) this.observers.add(observer);
    }

    @Override
    public void removeObserver(AttributeObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public Collection<AttributeObserver> getObservers() {
        return Collections.unmodifiableList(this.observers);
    }

    @Override
    public void join(GamePlayer player) {
        this.players.add(player);
    }

    @Override
    public void leave(GamePlayer player) {
        this.players.remove(player);
    }

    @Override
    public TeamPlayer setTeam(GamePlayer player, Team team) {

        Optional<TeamPlayer> optional = this.getTeamPlayer(player);
        optional.ifPresent(this::removeTeam);

        return team.addPlayer(player);
    }

    @Override
    public Team removeTeam(TeamPlayer player) {

        Team team = player.getTeam();
        team.removePlayer(player.getGamePlayer());

        return team;
    }

    @Override
    public void setCycle(GameCycle cycle) {
        this.cycle = cycle;
        this.notifyChange(GameAttribute.CYCLE_CHANGE);
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
    public GameCycle getCycle() {
        return this.cycle;
    }

    @Override
    public GameState getState() {
        return this.cycle.getState();
    }

    @Override
    public Location getSpawn() {
        return this.spawn;
    }

    @Override
    public boolean isStarted() {
        return this.getState() == GameState.RUNNING;
    }

    @Override
    public boolean isFinished() {
        return this.getState() == GameState.FINISHED;
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
    public int countTeams() {
        return this.teams.size();
    }

    @Override
    public boolean hasTeam(Player player) {
        return this.teams.stream().anyMatch(team -> team.contains(player));
    }

    @Override
    public GamePlayer getGamePlayer(Player player) {
        // orElse should not happen because all the online players
        // have always GamePlayer data.
        return this.players.stream()
                .filter(gamePlayer -> gamePlayer.isPlayer(player))
                .findFirst().orElse(new GamePlayer(player));
    }

    @Override
    public Optional<TeamPlayer> getTeamPlayer(Player player) {
        return this.getTeamPlayers().stream()
                .filter(teamPlayer -> teamPlayer.getGamePlayer().isPlayer(player))
                .findFirst();
    }

    @Override
    public Optional<TeamPlayer> getTeamPlayer(GamePlayer player) {
        return this.getTeamPlayers().stream()
                .filter(teamPlayer -> teamPlayer.getGamePlayer().equals(player))
                .findFirst();
    }

    @Override
    public Optional<Team> getTeam(Player player) {
        return this.teams.stream()
                .filter(team -> team.contains(player))
                .map(team -> (Team) team)
                .findFirst();
    }

    @Override
    public Optional<Team> getTeam(String name) {
        return this.teams.stream()
                .filter(team -> team.getName().equals(name))
                .map(team -> (Team) team)
                .findFirst();
    }

    @Override
    public Collection<GamePlayer> getGamePlayers() {
        return Collections.unmodifiableList(this.players);
    }

    @Override
    public Collection<TeamPlayer> getTeamPlayers() {
        return this.teams.stream()
                .flatMap(team -> team.getPlayers().stream())
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Team> getTeams() {
        return Collections.unmodifiableList(this.teams);
    }
}
