package com.github.syr0ws.fallenkingdom.game.model.v2.teams.base.capture;

import org.bukkit.Location;

public interface NexusModel {

    void addHealth(int health);

    void removeHealth(int heath);

    boolean isAlive();

    int getHealth();

    Location getLocation();
}
