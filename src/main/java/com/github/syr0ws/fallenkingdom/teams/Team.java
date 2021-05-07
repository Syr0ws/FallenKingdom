package com.github.syr0ws.fallenkingdom.teams;

import com.github.syr0ws.fallenkingdom.tools.Cuboid;
import com.github.syr0ws.fallenkingdom.tools.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class Team {

    private final String name, displayName;
    private final Location spawn;
    private final Cuboid base, vault;
    private final List<TeamParticipant> players;

    public Team(String name, String displayName, Location spawn, Cuboid base, Cuboid vault) {
        this.name = name;
        this.displayName = displayName;
        this.spawn = spawn;
        this.base = base;
        this.vault = vault;
        this.players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        if(!this.participate(player)) this.players.add(new TeamParticipant(player));
    }

    public void removePlayer(Player player) {
        this.players.removeIf(participant -> participant.getPlayer().equals(player));
    }

    public boolean participate(Player player) {
        return this.players.stream().anyMatch(participant -> participant.getPlayer().equals(player));
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public Location getSpawn() {
        return this.spawn;
    }

    public Cuboid getBase() {
        return this.base;
    }

    public Cuboid getVault() {
        return this.vault;
    }

    public Optional<TeamParticipant> getParticipant(Player player) {
        return this.players.stream()
                .filter(participant -> participant.getPlayer().equals(player))
                .findFirst();
    }

    public List<TeamParticipant> getParticipants() {
        return this.players;
    }
}
