package com.github.syr0ws.fallenkingdom.plugin.game.model.teams;

import com.github.syr0ws.fallenkingdom.api.model.FKPlayer;
import com.github.syr0ws.fallenkingdom.api.model.teams.*;
import com.github.syr0ws.universe.api.displays.Display;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CraftFKTeam implements FKTeam {

    private final String name, displayName;
    private final FKTeamBase base;
    private final TeamColor color;
    private final Map<UUID, CraftFKTeamPlayer> players = new HashMap<>();

    private TeamState state;

    public CraftFKTeam(String name, String displayName, FKTeamBase base, TeamColor color) {

        if(name == null || name.isEmpty())
            throw new IllegalArgumentException("Name cannot be null or empty.");

        if(displayName == null || displayName.isEmpty())
            throw new IllegalArgumentException("Display name cannot be null or empty.");

        if(base == null)
            throw new IllegalArgumentException("CraftFKTeamBase cannot be null.");

        if(color == null)
            throw new IllegalArgumentException("TeamColor cannot be null.");

        this.name = name;
        this.displayName = displayName;
        this.base = base;
        this.color = color;
        this.state = TeamState.ALIVE;
    }

    public CraftFKTeamPlayer addPlayer(FKPlayer player) {

        if(this.contains(player.getUUID()))
            throw new IllegalArgumentException("Player already exists.");

        CraftFKTeamPlayer teamPlayer = new CraftFKTeamPlayer(this, player);
        this.players.put(player.getUUID(), teamPlayer);

        return teamPlayer;
    }

    public void removePlayer(UUID uuid) {

        if(!this.contains(uuid))
            throw new IllegalArgumentException("Player not in team.");

        this.players.remove(uuid);
    }

    public void eliminate() {
        this.state = TeamState.ELIMINATED;
    }

    public void setBaseCaptured() {
        this.state = TeamState.BASE_CAPTURED;
    }

    @Override
    public void sendDisplay(Display display) {
        this.getOnlineTeamPlayers().forEach(player -> display.displayTo(player.getPlayer()));
    }

    @Override
    public void sendDisplay(Display display, Predicate<FKTeamPlayer> predicate) {
        this.getOnlineTeamPlayers().stream()
                .filter(predicate)
                .map(FKTeamPlayer::getPlayer)
                .forEach(display::displayTo);
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
    public boolean hasBaseCaptured() {
        return this.state == TeamState.BASE_CAPTURED;
    }

    @Override
    public boolean contains(UUID uuid) {
        return this.players.containsKey(uuid);
    }

    @Override
    public boolean contains(FKTeamPlayer player) {
        return player instanceof CraftFKTeamPlayer && this.getTeamPlayers().contains(player);
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
    public FKTeamBase getBase() {
        return this.base;
    }

    @Override
    public TeamState getState() {
        return this.state;
    }

    @Override
    public Optional<CraftFKTeamPlayer> getTeamPlayer(UUID uuid) {
        return Optional.ofNullable(this.players.get(uuid));
    }

    @Override
    public Collection<CraftFKTeamPlayer> getOnlineTeamPlayers() {
        return this.players.values().stream()
                .filter(CraftFKTeamPlayer::isOnline)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<CraftFKTeamPlayer> getTeamPlayers() {
        return this.players.values();
    }
}
