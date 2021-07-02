package com.github.syr0ws.fallenkingdom.game.model.v2.teams;

import com.github.syr0ws.universe.tools.Cuboid;
import org.bukkit.Location;

public class TeamBase {

    private final Cuboid cuboid;
    private final Cuboid vault; // Separation of the line above for future uses.
    private final Location spawn;

    public TeamBase(Cuboid cuboid, Cuboid vault, Location spawn) {
        this.cuboid = cuboid;
        this.vault = vault;
        this.spawn = spawn;
    }

    public Cuboid getCuboid() {
        return this.cuboid;
    }

    public Cuboid getVault() {
        return this.vault;
    }

    public Location getSpawn() {
        return this.spawn;
    }
}
