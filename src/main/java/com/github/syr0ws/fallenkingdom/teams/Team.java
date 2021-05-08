package com.github.syr0ws.fallenkingdom.teams;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Team {

    private final String name, displayName;
    private final TeamBase base;
    private final List<TeamPlayer> players;

    public Team(String name, String displayName, TeamBase base) {
        this.name = name;
        this.displayName = displayName;
        this.base = base;
        this.players = new ArrayList<>();
    }

    public void addPlayer(Player player) {

        if(this.contains(player)) return;

        TeamPlayer teamPlayer = new TeamPlayer(player);
        this.players.add(teamPlayer);
    }

    public void removePlayer(Player player) {
        this.players.removeIf(teamPlayer -> teamPlayer.is(player));
    }

    public boolean contains(Player player) {
        return this.players.stream().anyMatch(teamPlayer -> teamPlayer.is(player));
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public TeamBase getBase() {
        return this.base;
    }

    public Optional<TeamPlayer> getPlayer(Player player) {
        return this.players.stream()
                .filter(teamPlayer -> teamPlayer.is(player))
                .findFirst();
    }

    public List<TeamPlayer> getPlayers() {
        return Collections.unmodifiableList(this.players);
    }
}
