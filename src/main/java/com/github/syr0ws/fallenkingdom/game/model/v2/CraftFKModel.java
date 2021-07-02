package com.github.syr0ws.fallenkingdom.game.model.v2;

import com.github.syr0ws.fallenkingdom.game.model.v2.settings.SettingAccessor;
import com.github.syr0ws.fallenkingdom.game.model.v2.teams.CraftFKTeam;
import com.github.syr0ws.fallenkingdom.game.model.v2.teams.CraftFKTeamPlayer;
import com.github.syr0ws.fallenkingdom.game.model.v2.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.v2.teams.FKTeamPlayer;
import com.github.syr0ws.universe.attributes.AbstractAttributeObservable;
import com.github.syr0ws.universe.game.model.GamePlayer;
import com.github.syr0ws.universe.game.model.cycle.GameCycle;
import com.github.syr0ws.universe.settings.types.MutableSetting;
import org.bukkit.Location;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CraftFKModel extends AbstractAttributeObservable implements FKModel {

    private final SettingAccessor settings;
    private final List<CraftFKTeam> teams;

    private final Map<UUID, CraftFKPlayer> players = new HashMap<>();

    private GameCycle cycle;
    private GameState state;

    private boolean pvp, assaults;
    private int time;

    public CraftFKModel(SettingAccessor settings, List<CraftFKTeam> teams) {

        if(settings == null)
            throw new IllegalArgumentException("SettingsManager cannot be null.");

        if(teams.size() < 2)
            throw new IllegalArgumentException("Number of teams must be at least 2.");

        this.settings = settings;
        this.teams = teams;
    }

    public void addPlayer(CraftFKPlayer player) {

        if(this.isGamePlayer(player.getUUID()))
            throw new IllegalArgumentException("Player already exists.");

        this.players.put(player.getUUID(), player);
    }

    public void removePlayer(CraftFKPlayer player) {

        if(!this.isGamePlayer(player.getUUID()))
            throw new IllegalArgumentException("Player doesn't exist.");

        if(player.isPlaying())
            throw new IllegalArgumentException("Cannot remove a Player while he is playing.");

        this.players.remove(player.getUUID());
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

    public boolean isGamePlayer(UUID uuid) {
        return this.players.containsKey(uuid);
    }

    @Override
    public CraftFKTeamPlayer setTeam(GamePlayer player, FKTeam team) {

        if(!this.isWaiting())
            throw new IllegalArgumentException("A player can be assigned to a team only when a game is waiting.");

        if(!this.isValid(team))
            throw new IllegalArgumentException("FKTeam not registered.");

        // If the team exists, it is an instance of the CraftFKTeam class.
        CraftFKTeam fkTeam = (CraftFKTeam) team;

        return fkTeam.addPlayer(player);
    }

    @Override
    public CraftFKTeamPlayer removeTeam(GamePlayer player) {

        if(!this.isWaiting())
            throw new IllegalArgumentException("A player can be removed from a team only when a game is waiting.");

        Optional<CraftFKTeamPlayer> optional = this.getTeamPlayer(player.getUUID());

        if(!optional.isPresent())
            throw new IllegalArgumentException("GamePlayer hasn't team.");

        CraftFKTeamPlayer teamPlayer = optional.get();
        CraftFKTeam team = (CraftFKTeam) teamPlayer.getTeam();

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
    public void addTime() {
        this.time++;
        this.notifyChange(GameAttribute.TIME_CHANGE);
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
    public int getTime() {
        return this.time;
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
    public GameState getState() {
        return this.state;
    }

    @Override
    public boolean isValid(FKTeam team) {
        return team instanceof CraftFKTeam && this.getTeams().contains(team);
    }

    @Override
    public boolean isValid(FKTeamPlayer player) {
        return player instanceof CraftFKTeamPlayer && this.getTeamPlayers().contains(player);
    }

    @Override
    public boolean hasTeam(UUID uuid) {
        return this.teams.stream().anyMatch(team -> team.contains(uuid));
    }

    @Override
    public boolean hasTeam(GamePlayer player) {
        return this.teams.stream().anyMatch(team -> team.contains(player.getUUID()));
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
    public Optional<CraftFKTeamPlayer> getTeamPlayer(UUID uuid) {
        return this.teams.stream()
                .map(team -> team.getTeamPlayer(uuid))
                .flatMap(optional -> optional.map(Stream::of).orElseGet(Stream::empty))
                .findFirst();
    }

    @Override
    public Optional<CraftFKTeam> getTeam(UUID uuid) {
        return this.teams.stream()
                .filter(team -> team.contains(uuid))
                .findFirst();
    }

    @Override
    public Optional<CraftFKTeam> getTeamByName(String name) {
        return this.teams.stream()
                .filter(team -> team.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public Collection<CraftFKTeam> getTeams() {
        return Collections.unmodifiableList(this.teams);
    }

    @Override
    public Collection<CraftFKTeamPlayer> getTeamPlayers() {
        return this.teams.stream()
                .flatMap(team -> team.getTeamPlayers().stream())
                .collect(Collectors.toList());
    }

    @Override
    public GameCycle getCycle() {
        return this.cycle;
    }

    @Override
    public boolean isWaiting() {
        return this.state == GameState.WAITING;
    }

    @Override
    public boolean isRunning() {
        return this.state == GameState.RUNNING;
    }

    @Override
    public boolean isFinished() {
        return this.state == GameState.FINISHED;
    }

    @Override
    public CraftFKPlayer getPlayer(UUID uuid) {
        return this.players.get(uuid);
    }

    @Override
    public Optional<CraftFKPlayer> getPlayer(String name) {
        return this.players.values().stream()
                .filter(player -> player.getName().equals(name))
                .findFirst();
    }

    @Override
    public Collection<CraftFKPlayer> getPlayers() {
        return this.players.values();
    }

    @Override
    public Collection<CraftFKPlayer> getOnlinePlayers() {
        return this.players.values().stream()
                .filter(CraftFKPlayer::isOnline)
                .collect(Collectors.toList());
    }
}
