package com.github.syr0ws.fallenkingdom.game.model.v2.teams;

import com.github.syr0ws.fallenkingdom.capture.Capturable;
import com.github.syr0ws.universe.tools.Cuboid;
import org.bukkit.Location;

public class CraftFKTeamBase implements FKTeamBase {

    private final Cuboid cuboid, vault;
    private final Location spawn;
    private final Capturable capturable;

    public CraftFKTeamBase(Cuboid cuboid, Cuboid vault, Location spawn, Capturable capturable) {

        if(cuboid == null)
            throw new IllegalArgumentException("Cuboid cannot be null.");

        if(vault == null)
            throw new IllegalArgumentException("Vault cannot be null.");

        if(spawn == null)
            throw new IllegalArgumentException("Spawn cannot be null.");

        if(capturable == null)
            throw new IllegalArgumentException("Capturable cannot be null.");

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
