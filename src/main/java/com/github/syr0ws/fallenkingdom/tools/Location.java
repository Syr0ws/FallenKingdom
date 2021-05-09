package com.github.syr0ws.fallenkingdom.tools;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class Location {

    private final double x, y, z;
    private final String world;

    public Location(double x, double y, double z, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public Location(org.bukkit.Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.world = location.getWorld().getName();
    }

    public Location(ConfigurationSection section) {
        this.x = section.getDouble("x");
        this.y = section.getDouble("y");
        this.z = section.getDouble("z");
        this.world = section.getString("world");
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public String getWorldName() {
        return this.world;
    }

    public World getWorld() {
        return Bukkit.getWorld(this.world);
    }

    public org.bukkit.Location toBukkitLocation() {
        return new org.bukkit.Location(this.getWorld(), this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        return String.format("Location { x=%f, y=%f, z=%f, world=%s }", this.x, this.y, this.z, this.world);
    }
}
