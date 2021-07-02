package com.github.syr0ws.fallenkingdom.game.model.v2.teams.base;

import com.github.syr0ws.fallenkingdom.game.model.v2.teams.base.capture.Capturable;
import com.github.syr0ws.universe.tools.Cuboid;
import org.bukkit.Location;

public class CraftFKTeamBase implements FKTeamBase {

    private final Cuboid cuboid, vault;
    private final Location spawn;
    private final Capturable capturable;

    public CraftFKTeamBase(Cuboid cuboid, Cuboid vault, Location spawn, Capturable capturable) {
        this.cuboid = cuboid;
        this.vault = vault;
        this.spawn = spawn;
        this.capturable = capturable;
    }

    @Override
    public Cuboid getBase() {
        return this.cuboid;
    }

    @Override
    public Cuboid getVault() {
        return this.vault;
    }

    @Override
    public Location getSpawn() {
        return this.spawn;
    }

    @Override
    public Capturable getCapurable() {
        return this.capturable;
    }
}
