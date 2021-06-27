package com.github.syr0ws.fallenkingdom.game.model;

import com.github.syr0ws.fallenkingdom.game.model.attributes.GameAttribute;
import com.github.syr0ws.fallenkingdom.game.model.capture.Capture;
import com.github.syr0ws.fallenkingdom.game.model.capture.FKCapture;
import com.github.syr0ws.fallenkingdom.game.model.cycles.GameCycle;
import com.github.syr0ws.fallenkingdom.game.model.modes.Mode;
import com.github.syr0ws.fallenkingdom.game.model.players.CraftGamePlayer;
import com.github.syr0ws.fallenkingdom.game.model.players.GamePlayer;
import com.github.syr0ws.fallenkingdom.game.model.settings.SettingAccessor;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeamPlayer;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;
import com.github.syr0ws.universe.attributes.AbstractAttributeObservable;
import com.github.syr0ws.universe.attributes.Attribute;
import com.github.syr0ws.universe.attributes.AttributeObserver;
import com.github.syr0ws.universe.settings.types.MutableSetting;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FKGame extends AbstractAttributeObservable implements GameModel {

    private final SettingAccessor settings;
    private final List<FKTeam> teams;

    private final List<FKCapture> captures = new ArrayList<>();
    private final List<CraftGamePlayer> players = new ArrayList<>();
    private final List<AttributeObserver> observers = new ArrayList<>();

    private GameCycle cycle;
    private GameState state;

    private boolean pvp, assaults;
    private int time;

    public FKGame(SettingAccessor settings, List<FKTeam> teams) {

        if(settings == null)
            throw new IllegalArgumentException("SettingsManager cannot be null.");

        if(teams.size() < 2)
            throw new IllegalArgumentException("Number of teams must be at least 2.");

        this.settings = settings;
        this.teams = teams;
    }

    public void setCycle(GameCycle cycle) {

        if(cycle == null)
            throw new IllegalArgumentException("GameCycle cannot be null.");

        this.cycle = cycle;
        this.notifyChange(GameAttribute.CYCLE_CHANGE);
    }

    public void setState(GameState state) {

        if(state == null)
            throw new IllegalArgumentException("GameState cannot be null.");

        this.state = state;
        this.notifyChange(GameAttribute.STATE_CHANGE);
    }

    public void addCapture(FKCapture capture) {

        if(this.captures.contains(capture))
            throw new IllegalArgumentException("Capture already exists.");

        this.captures.add(capture);
    }

    public void removeCapture(FKCapture capture) {

        if(!this.captures.contains(capture))
            throw new IllegalArgumentException("Capture doesn't exist.");

        this.captures.remove(capture);
    }

    public boolean isGamePlayer(UUID uuid) {
        return this.players.stream().anyMatch(gamePlayer -> gamePlayer.getUUID().equals(uuid));
    }

    @Override
    public TeamPlayer setTeam(GamePlayer player, Team team) {

        if(this.isStarted() || this.isFinished())
            throw new IllegalArgumentException("Cannot set a team to a player when a game is started or finished.");

        if(!this.isValid(team))
            throw new IllegalArgumentException("Team not registered.");

        // If the team exists, it is an instance of the FKTeam class.
        FKTeam fkTeam = (FKTeam) team;

        return fkTeam.addPlayer(player);
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
    public boolean isValid(Team team) {
        return this.getTeams().contains(team);
    }

    @Override
    public boolean isValid(TeamPlayer player) {
        return this.getTeamPlayers().contains(player);
    }

    @Override
    public boolean isValid(Capture capture) {
        return this.getCaptures().contains(capture);
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
    public boolean isCaptured(Team team) {
        return this.captures.stream()
                .anyMatch(capture -> capture.getCaptured().equals(team));
    }

    @Override
    public boolean isCapturing(TeamPlayer player) {
        return this.captures.stream()
                .anyMatch(capture -> capture.getCapturers().contains(player));
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
        return this.state;
    }

    @Override
    public Location getSpawn() {
        MutableSetting<Location> setting = this.settings.getGameSpawnSetting();
        return setting.getValue();
    }

    @Override
    public SettingAccessor getSettings() {
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
    public Optional<? extends GamePlayer> getGamePlayer(String name) {
        return this.players.stream()
                .filter(player -> player.getName().equals(name))
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
    public Optional<FKTeam> getTeam(UUID uuid) {
        return this.teams.stream()
                .filter(team -> team.contains(uuid))
                .findFirst();
    }

    @Override
    public Optional<FKTeam> getTeam(GamePlayer player) {
        return this.teams.stream()
                .filter(team -> team.contains(player))
                .findFirst();
    }

    @Override
    public Optional<FKTeam> getTeamByName(String name) {
        return this.teams.stream()
                .filter(team -> team.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public Optional<FKCapture> getCapture(Team captured) {
        return this.captures.stream()
                .filter(capture -> capture.getCaptured().equals(captured))
                .findFirst();
    }

    @Override
    public Collection<FKCapture> getCaptures() {
        return Collections.unmodifiableList(this.captures);
    }

    @Override
    public Collection<FKTeam> getTeams() {
        return Collections.unmodifiableList(this.teams);
    }

    @Override
    public Collection<? extends TeamPlayer> getTeamPlayers() {
        return this.teams.stream()
                .flatMap(team -> team.getTeamPlayers().stream())
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
