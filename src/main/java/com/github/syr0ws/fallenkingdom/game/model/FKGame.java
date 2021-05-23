package com.github.syr0ws.fallenkingdom.game.model;

import com.github.syr0ws.fallenkingdom.attributes.Attribute;
import com.github.syr0ws.fallenkingdom.attributes.AttributeObserver;
import com.github.syr0ws.fallenkingdom.game.GameSettings;
import com.github.syr0ws.fallenkingdom.game.model.attributes.GameAttribute;
import com.github.syr0ws.fallenkingdom.game.model.capture.Capture;
import com.github.syr0ws.fallenkingdom.game.model.cycles.GameCycle;
import com.github.syr0ws.fallenkingdom.game.model.modes.Mode;
import com.github.syr0ws.fallenkingdom.game.model.players.CraftGamePlayer;
import com.github.syr0ws.fallenkingdom.game.model.players.GamePlayer;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeamPlayer;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;
import com.github.syr0ws.fallenkingdom.settings.impl.LocationSetting;
import com.github.syr0ws.fallenkingdom.settings.manager.SettingManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FKGame implements GameModel {

    private final SettingManager settings;
    private final List<FKTeam> teams;

    private final List<Capture> captures = new ArrayList<>();
    private final List<CraftGamePlayer> players = new ArrayList<>();
    private final List<AttributeObserver> observers = new ArrayList<>();

    private GameCycle cycle;
    private boolean pvp, assaults;
    private int time;

    public FKGame(SettingManager settings, List<FKTeam> teams) {

        if(settings == null)
            throw new IllegalArgumentException("SettingsManager cannot be null.");

        if(teams.size() < 2)
            throw new IllegalArgumentException("Number of teams must be at least 2.");

        this.settings = settings;
        this.teams = teams;
    }

    public boolean isGamePlayer(UUID uuid) {
        return this.players.stream().anyMatch(gamePlayer -> gamePlayer.getUUID().equals(uuid));
    }

    public void setCycle(GameCycle cycle) {

        if(cycle == null)
            throw new IllegalArgumentException("GameCycle cannot be null.");

        this.cycle = cycle;
        this.notifyChange(GameAttribute.CYCLE_CHANGE);
    }

    @Override
    public TeamPlayer setTeam(GamePlayer player, String teamName) {

        if(this.isStarted() || this.isFinished())
            throw new IllegalArgumentException("Cannot set a team to a player when a game is started or finished.");

        Optional<Team> optional = this.getTeamByName(teamName);

        if(!optional.isPresent())
            throw new IllegalArgumentException(String.format("Team '%s' doesn't exist.", teamName));

        Optional<Team> optionalTeam = this.getTeam(player.getUUID());
        optionalTeam.map(team -> (FKTeam) team).ifPresent(team -> team.removePlayer(player.getUUID()));

        FKTeam team = (FKTeam) optional.get();

        return team.addPlayer(player);
    }

    @Override
    public FKTeamPlayer removeTeam(GamePlayer player) {

        if(this.isStarted() || this.isFinished())
            throw new IllegalArgumentException("Cannot remove a player from a team when a game is started or finished.");

        Optional<? extends TeamPlayer> optional = this.getTeamPlayer(player);

        if(!optional.isPresent())
            throw new IllegalArgumentException("GamePlayer hasn't team.");

        FKTeamPlayer teamPlayer = (FKTeamPlayer) optional.get();
        FKTeam team = (FKTeam) teamPlayer.getTeam();

        team.removePlayer(teamPlayer.getUUID());

        return teamPlayer;
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
    public void addCapture(Capture capture) {

        if(this.captures.contains(capture))
            throw new IllegalArgumentException("Capture already exists.");

        this.captures.add(capture);
    }

    @Override
    public void removeCapture(Capture capture) {

        if(!this.captures.contains(capture))
            throw new IllegalArgumentException("Capture doesn't exist.");

        this.captures.remove(capture);
    }

    @Override
    public void setGamePlayerMode(UUID uuid, Mode mode) {

        if(!this.isGamePlayer(uuid))
            throw new IllegalArgumentException("Player is not a GamePlayer.");

        CraftGamePlayer gamePlayer = this.getGamePlayer(uuid);
        gamePlayer.setMode(mode);
    }

    @Override
    public void addGamePlayer(Player player, Mode mode) {

        if(this.isGamePlayer(player.getUniqueId()))
            throw new IllegalArgumentException("A GamePlayer already exists.");

        CraftGamePlayer gamePlayer = new CraftGamePlayer(player);
        gamePlayer.setMode(mode);

        this.players.add(gamePlayer);
    }

    @Override
    public void removeGamePlayer(Player player) {

        if(!this.isGamePlayer(player.getUniqueId()))
            throw new IllegalArgumentException("No GamePlayer exists.");

        GamePlayer gamePlayer = this.getGamePlayer(player.getUniqueId());

        if(gamePlayer.isPlaying())
            throw new IllegalArgumentException("Cannot remove a GamePlayer which is playing.");

        this.players.remove(gamePlayer);

        gamePlayer.getMode().remove();
    }

    @Override
    public void addTime() {
        this.time++;
        this.notifyChange(GameAttribute.TIME_CHANGE);
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
    public boolean hasTeam(UUID uuid) {
        return this.teams.stream().anyMatch(team -> team.contains(uuid));
    }

    @Override
    public boolean hasTeam(GamePlayer player) {
        return this.teams.stream().anyMatch(team -> team.contains(player));
    }

    @Override
    public boolean isTeamPlayer(UUID uuid) {
        return this.getTeamPlayers().stream().anyMatch(teamPlayer -> teamPlayer.getUUID().equals(uuid));
    }

    @Override
    public boolean isTeamPlayer(GamePlayer player) {
        return this.getTeamPlayers().stream().anyMatch(teamPlayer -> teamPlayer.getPlayer().equals(player.getPlayer()));
    }

    @Override
    public int getTime() {
        return this.time;
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
        LocationSetting setting = this.settings.getSetting(GameSettings.SPAWN_LOCATION, LocationSetting.class);
        return setting.getValue();
    }

    @Override
    public SettingManager getSettings() {
        return this.settings;
    }

    @Override
    public CraftGamePlayer getGamePlayer(UUID uuid) {

        Optional<CraftGamePlayer> optional = this.players.stream()
                .filter(gamePlayer -> gamePlayer.getUUID().equals(uuid))
                .findFirst();

        if(!optional.isPresent())
            throw new NullPointerException("GamePlayer not found. This is an abnormal behavior.");

        return optional.get();
    }

    @Override
    public Optional<GamePlayer> getGamePlayer(String name) {
        return this.players.stream()
                .filter(player -> player.getName().equals(name))
                .map(player -> (GamePlayer) player)
                .findFirst();
    }

    @Override
    public Optional<? extends TeamPlayer> getTeamPlayer(UUID uuid) {
        return this.teams.stream()
                .map(team -> team.getTeamPlayer(uuid))
                .flatMap(optional -> optional.map(Stream::of).orElseGet(Stream::empty))
                .findFirst();
    }

    @Override
    public Optional<? extends TeamPlayer> getTeamPlayer(GamePlayer player) {
        return this.teams.stream()
                .map(team -> team.getTeamPlayer(player))
                .flatMap(optional -> optional.map(Stream::of).orElseGet(Stream::empty))
                .findFirst();
    }

    @Override
    public Optional<Team> getTeam(UUID uuid) {
        return this.teams.stream()
                .filter(team -> team.contains(uuid))
                .map(team -> (Team) team)
                .findFirst();
    }

    @Override
    public Optional<Team> getTeam(GamePlayer player) {
        return this.teams.stream()
                .filter(team -> team.contains(player))
                .map(team -> (Team) team)
                .findFirst();
    }

    @Override
    public Optional<Team> getTeamByName(String name) {
        return this.teams.stream()
                .filter(team -> team.getName().equalsIgnoreCase(name))
                .map(team -> (Team) team)
                .findFirst();
    }

    @Override
    public Collection<Capture> getCaptures() {
        return Collections.unmodifiableList(this.captures);
    }

    @Override
    public Collection<? extends Team> getTeams() {
        return Collections.unmodifiableList(this.teams);
    }

    @Override
    public Collection<? extends TeamPlayer> getTeamPlayers() {
        return this.teams.stream()
                .flatMap(team -> team.getPlayers().stream())
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GamePlayer> getPlayers() {
        return Collections.unmodifiableList(this.players);
    }

    @Override
    public Collection<? extends GamePlayer> getOnlinePlayers() {
        return this.players.stream()
                .filter(CraftGamePlayer::isOnline)
                .collect(Collectors.toList());
    }

    @Override
    public void notifyChange(Attribute attribute) {
        new ArrayList<>(this.observers).stream()
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
}
