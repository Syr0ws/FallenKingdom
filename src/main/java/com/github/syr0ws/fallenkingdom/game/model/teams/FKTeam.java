package com.github.syr0ws.fallenkingdom.game.model.teams;

import com.github.syr0ws.fallenkingdom.game.model.players.GamePlayer;

import java.util.*;

public class FKTeam implements Team {

    private final String name, displayName;
    private final TeamBase base;
    private final TeamColor color;
    private final List<FKTeamPlayer> players = new ArrayList<>();

    private TeamState state;

    public FKTeam(String name, String displayName, TeamBase base, TeamColor color) {

        if(name == null || name.isEmpty())
            throw new IllegalArgumentException("Name cannot be null or empty.");

        if(displayName == null || displayName.isEmpty())
            throw new IllegalArgumentException("Display name cannot be null or empty.");

        if(base == null)
            throw new IllegalArgumentException("TeamBase cannot be null.");

        if(color == null)
            throw new IllegalArgumentException("TeamColor cannot be null.");

        this.name = name;
        this.displayName = displayName;
        this.base = base;
        this.color = color;
        this.state = TeamState.ALIVE;
    }

    public FKTeamPlayer addPlayer(GamePlayer player) {

        if(this.contains(player))
            throw new IllegalArgumentException("TeamPlayer already exists.");

        FKTeamPlayer teamPlayer = new FKTeamPlayer(this, player);
        this.players.add(teamPlayer);

        return teamPlayer;
    }

    public void removePlayer(UUID uuid) {

        if(!this.contains(uuid))
            throw new IllegalArgumentException("Player not in team.");

        this.players.removeIf(teamPlayer -> teamPlayer.getUUID().equals(uuid));
    }

    public void eliminate() {
        this.state = TeamState.ELIMINATED;
        this.players.forEach(player -> player.setAlive(false));
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDisplayName() {
        return this.color.getChatColor() + this.displayName;
    }

    @Override
    public boolean isEliminated() {
        return this.state == TeamState.ELIMINATED;
    }

    @Override
    public boolean contains(UUID uuid) {
        return this.players.stream().anyMatch(teamPlayer -> teamPlayer.getUUID().equals(uuid));
    }

    @Override
    public boolean contains(GamePlayer player) {
        return this.players.stream().anyMatch(fkTeamPlayer -> fkTeamPlayer.getPlayer().equals(player.getPlayer()));
    }

    @Override
    public boolean contains(TeamPlayer player) {
        return this.getPlayers().contains(player);
    }

    @Override
    public int size() {
        return this.players.size();
    }

    @Override
    public TeamColor getColor() {
        return this.color;
    }

    @Override
    public TeamBase getBase() {
        return this.base;
    }

    @Override
    public TeamState getState() {
        return this.state;
    }

    @Override
    public Optional<TeamPlayer> getTeamPlayer(UUID uuid) {
        return this.players.stream()
                .filter(teamPlayer -> teamPlayer.getUUID().equals(uuid))
                .map(teamPlayer -> (TeamPlayer) teamPlayer)
                .findFirst();
    }

    @Override
    public Optional<TeamPlayer> getTeamPlayer(GamePlayer player) {
        return this.players.stream()
                .filter(teamPlayer -> teamPlayer.getPlayer().equals(player.getPlayer()))
                .map(teamPlayer -> (TeamPlayer) teamPlayer)
                .findFirst();
    }

    @Override
    public List<? extends TeamPlayer> getPlayers() {
        return Collections.unmodifiableList(this.players);
    }
}
