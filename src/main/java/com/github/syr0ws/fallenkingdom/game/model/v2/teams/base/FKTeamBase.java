package com.github.syr0ws.fallenkingdom.game.model.v2.teams.base;

import com.github.syr0ws.fallenkingdom.game.model.v2.teams.base.capture.Capturable;
import com.github.syr0ws.universe.tools.Cuboid;
import org.bukkit.Location;

public interface FKTeamBase {

    Cuboid getBase();

    Cuboid getVault();

    Location getSpawn();

    Capturable getCapurable();
}
