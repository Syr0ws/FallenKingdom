package com.github.syr0ws.fallenkingdom.game.model.teams;

import com.github.syr0ws.fallenkingdom.capture.Capturable;
import com.github.syr0ws.universe.sdk.tools.Cuboid;
import org.bukkit.Location;

public interface FKTeamBase {

    Cuboid getBase();

    Cuboid getVault();

    Location getSpawn();

    Capturable getCapurable();
}
