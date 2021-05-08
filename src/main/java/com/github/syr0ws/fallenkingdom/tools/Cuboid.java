package com.github.syr0ws.fallenkingdom.tools;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class Cuboid {

    private final Location loc1, loc2;

    public Cuboid(Location loc1, Location loc2) {

        if(!loc1.getWorldName().equals(loc2.getWorldName()))
            throw new IllegalArgumentException("Worlds must be the same.");

        this.loc1 = loc1;
        this.loc2 = loc2;
    }

    public Cuboid(ConfigurationSection section) {

        String world = section.getString("world");

        this.loc1 = this.getLocationFromSection(section.getConfigurationSection("loc1"), world);
        this.loc2 = this.getLocationFromSection(section.getConfigurationSection("loc2"), world);
    }

    private Location getLocationFromSection(ConfigurationSection section, String world) {

        double x = section.getDouble("x");
        double y = section.getDouble("y");
        double z = section.getDouble("z");

        return new Location(x, y, z, world);
    }

    public boolean isIn(Location location) {

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return this.loc1.getX() >= x && this.loc2.getX() <= x
                && this.loc1.getY() >= y && this.loc2.getY() <= y
                && this.loc1.getZ() >= z && this.loc2.getZ() <= z
                && location.getWorld().equals(this.getWorld());
    }

    public boolean isIn(org.bukkit.Location location) {
        return this.isIn(new Location(location));
    }

    public Location getLoc1() {
        return this.loc1;
    }

    public Location getLoc2() {
        return this.loc2;
    }

    public World getWorld() {
        return this.loc1.getWorld();
    }
}
