package com.github.syr0ws.fallenkingdom.capture.nexus.model;

import com.github.syr0ws.fallenkingdom.capture.CaptureType;
import org.bukkit.Location;

public class CraftNexusModel implements NexusModel {

    private final Location location;
    private int health;

    public CraftNexusModel(Location location, int health) {

        if(location == null)
            throw new IllegalArgumentException("Location cannot be null.");

        if(health <= 0)
            throw new IllegalArgumentException("Health must be positive.");

        this.location = location;
        this.health = health;
    }

    @Override
    public void addHealth(int health) {

        if(health <= 0)
            throw new IllegalArgumentException("Health must be positive.");

        this.health += health;
    }

    @Override
    public void removeHealth(int health) {

        if(health <= 0)
            throw new IllegalArgumentException("Health must be positive.");

        this.health = Math.max(this.health - health, 0);
    }

    @Override
    public boolean isAlive() {
        return this.health > 0;
    }

    @Override
    public int getHealth() {
        return this.health;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public CaptureType getCaptureType() {
        return CaptureType.NEXUS;
    }
}
