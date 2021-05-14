package com.github.syr0ws.fallenkingdom.game.model.teams;

import com.github.syr0ws.fallenkingdom.game.model.GamePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Team {

    private final String name, displayName;
    private final TeamBase base;
    private final TeamColor color;
    private final List<TeamPlayer> players;

    public Team(String name, String displayName, TeamBase base, TeamColor color) {
        this.name = name;
        this.displayName = displayName;
        this.base = base;
        this.color = color;
        this.players = new ArrayList<>();
    }

    public TeamPlayer addPlayer(GamePlayer player) {

        if(this.contains(player)) return null;

        TeamPlayer teamPlayer = new TeamPlayer(player, this);
        this.players.add(teamPlayer);

        return teamPlayer;
    }

    public void removePlayer(GamePlayer player) {
        this.players.removeIf(teamPlayer -> teamPlayer.getGamePlayer().equals(player));
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.color.getChatColor() + this.displayName;
    }

    public int size() {
        return this.players.size();
    }

    public TeamBase getBase() {
        return this.base;
    }

    public TeamColor getColor() {
        return this.color;
    }

    public boolean contains(Player player) {
        return this.players.stream().anyMatch(teamPlayer -> teamPlayer.getGamePlayer().isPlayer(player));
    }

    public boolean contains(GamePlayer player) {
        return this.players.stream().anyMatch(teamPlayer -> teamPlayer.getGamePlayer().equals(player));
    }

    public boolean contains(TeamPlayer player) {
        return this.players.contains(player);
    }

    public boolean isEliminated() {
        return this.players.stream().allMatch(TeamPlayer::isEliminated);
    }

    public Collection<TeamPlayer> getPlayers() {
        return Collections.unmodifiableList(this.players);
    }
}
