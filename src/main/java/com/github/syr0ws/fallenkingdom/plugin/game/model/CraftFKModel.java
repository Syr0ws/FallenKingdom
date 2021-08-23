package com.github.syr0ws.fallenkingdom.plugin.game.model;

import com.github.syr0ws.fallenkingdom.api.model.FKModel;
import com.github.syr0ws.fallenkingdom.api.model.FKPlayer;
import com.github.syr0ws.fallenkingdom.api.model.FKSettings;
import com.github.syr0ws.fallenkingdom.api.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.api.model.teams.FKTeamPlayer;
import com.github.syr0ws.fallenkingdom.plugin.game.model.teams.CraftFKTeam;
import com.github.syr0ws.fallenkingdom.plugin.game.model.teams.CraftFKTeamPlayer;
import com.github.syr0ws.universe.api.game.model.GamePlayer;
import com.github.syr0ws.universe.sdk.game.model.DefaultGameModel;
import com.github.syr0ws.universe.sdk.game.model.DefaultGamePlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CraftFKModel extends DefaultGameModel implements FKModel {

    private final List<CraftFKTeam> teams;
    private boolean pvp, assaults, nether, end;

    public CraftFKModel(FKSettings settings, List<CraftFKTeam> teams) {
        super(settings);

        if(teams.size() < 2)
            throw new IllegalArgumentException("Number of teams must be at least 2.");

        this.teams = teams;
    }

    @Override
    public CraftFKTeamPlayer setTeam(FKPlayer player, FKTeam team) {

        if(this.isStarted())
            throw new IllegalArgumentException("A player can be assigned to a team only when a game isn't started.");

        if(!this.isValid(player))
            throw new IllegalArgumentException("Invalid player.");

        if(!this.isValid(team))
            throw new IllegalArgumentException("FKTeam not registered.");

        // If the team exists, it is an instance of the CraftFKTeam class.
        CraftFKTeam fkTeam = (CraftFKTeam) team;

        return fkTeam.addPlayer(player);
    }

    @Override
    public CraftFKTeamPlayer removeTeam(FKPlayer player) {

        if(this.isStarted())
            throw new IllegalArgumentException("A player can be removed from a team only when a game isn't started.");

        Optional<CraftFKTeamPlayer> optional = this.getTeamPlayer(player.getUUID());

        if(!optional.isPresent())
            throw new IllegalArgumentException("GamePlayer hasn't team.");

        CraftFKTeamPlayer teamPlayer = optional.get();
        CraftFKTeam team = (CraftFKTeam) teamPlayer.getTeam();

        team.removePlayer(teamPlayer.getFKPlayer().getUUID());

        return teamPlayer;
    }

    @Override
    public void setPvPEnabled(boolean enabled) {
        this.pvp = enabled;
        this.notifyChange(FKAttribute.PVP_STATE);
    }

    @Override
    public void setAssaultsEnabled(boolean enabled) {
        this.assaults = enabled;
        this.notifyChange(FKAttribute.ASSAULTS_STATE);
    }

    @Override
    public void setNetherEnabled(boolean enabled) {
        this.nether = enabled;
        this.notifyChange(FKAttribute.NETHER_STATE);
    }

    @Override
    public void setEndEnabled(boolean enabled) {
        this.end = enabled;
        this.notifyChange(FKAttribute.END_STATE);
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
    public boolean isNetherEnabled() {
        return this.nether;
    }

    @Override
    public boolean isEndEnabled() {
        return this.end;
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
    public boolean hasTeam(FKPlayer player) {
        return this.teams.stream().anyMatch(team -> team.contains(player.getUUID()));
    }

    @Override
    public boolean isTeamPlayer(UUID uuid) {
        return this.getTeamPlayers().stream()
                .map(CraftFKTeamPlayer::getFKPlayer)
                .anyMatch(player -> player.getUUID().equals(uuid));
    }

    @Override
    public boolean isTeamPlayer(FKPlayer player) {
        return this.getTeams().stream().anyMatch(team -> team.contains(player.getUUID()));
    }

    @Override
    public boolean isValid(GamePlayer player) {
        return player instanceof CraftFKPlayer;
    }

    @Override
    public FKPlayer getFKPlayer(UUID uuid) {
        return (FKPlayer) super.getPlayer(uuid);
    }

    @Override
    public DefaultGamePlayer createPlayer(Player player) {
        return new CraftFKPlayer(player);
    }

    @Override
    public FKSettings getSettings() {
        return (FKSettings) super.getSettings();
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
}
