package com.github.syr0ws.fallenkingdom.tools;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class Cuboid {

    private final Location min, max;

    public Cuboid(Location loc1, Location loc2) {

        if(!loc1.getWorldName().equals(loc2.getWorldName()))
            throw new IllegalArgumentException("Worlds must be the same.");

        this.min = this.getMin(loc1, loc2);
        this.max = this.getMax(loc1, loc2);
    }

    public Cuboid(ConfigurationSection section) {

        String world = section.getString("world");

        Location loc1 = this.getLocationFromSection(section.getConfigurationSection("loc1"), world);
        Location loc2 = this.getLocationFromSection(section.getConfigurationSection("loc2"), world);

        this.min = this.getMin(loc1, loc2);
        this.max = this.getMax(loc1, loc2);
    }

    public boolean isIn(Location location) {

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return x >= this.min.getX() && x <= this.max.getX()
                && y >= this.min.getY() && y <= this.max.getY()
                && z >= this.min.getZ() && z <= this.max.getZ()
                && location.getWorld().equals(this.getWorld());
    }

    public boolean isIn(org.bukkit.Location location) {
        return this.isIn(new Location(location));
    }

    private Location getLocationFromSection(ConfigurationSection section, String world) {

        double x = section.getDouble("x");
        double y = section.getDouble("y");
        double z = section.getDouble("z");

        return new Location(x, y, z, world);
    }

    private Location getMin(Location loc1, Location loc2) {

        int minX = (int) Math.min(loc1.getX(), loc2.getX());
        int minY = (int) Math.min(loc1.getY(), loc2.getY());
        int minZ = (int) Math.min(loc1.getZ(), loc2.getZ());

        return new Location(minX, minY, minZ, loc1.getWorld().getName());
    }

    private Location getMax(Location loc1, Location loc2) {

        int maxX = (int) Math.max(loc1.getX(), loc2.getX());
        int maxY = (int) Math.max(loc1.getY(), loc2.getY());
        int maxZ = (int) Math.max(loc1.getZ(), loc2.getZ());

        return new Location(maxX, maxY, maxZ, loc1.getWorld().getName());
    }

    public Location getMinLocation() {
        return this.min;
    }

    public Location getMaxLocation() {
        return this.max;
    }

    public World getWorld() {
        return this.min.getWorld();
    }

    @Override
    public String toString() {
        return String.format("Cuboid { min=%s, max=%s }", this.min, this.max);
    }
}
