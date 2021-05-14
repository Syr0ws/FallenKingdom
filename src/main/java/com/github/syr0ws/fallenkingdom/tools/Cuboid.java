package com.github.syr0ws.fallenkingdom.tools;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class Cuboid {

    private final int minX, maxX;
    private final int minY, maxY;
    private final int minZ, maxZ;
    private final String world;

    public Cuboid(int x1, int x2, int y1, int y2, int z1, int z2, String world) {

        int[] xArray = this.order(x1, x2);
        int[] yArray = this.order(y1, y2);
        int[] zArray = this.order(z1, z2);

        this.minX = xArray[0];
        this.minY = yArray[0];
        this.minZ = zArray[0];

        this.maxX = xArray[1];
        this.maxY = yArray[1];
        this.maxZ = zArray[1];

        this.world = world;
    }

    public Cuboid(Location loc1, Location loc2) {

        if(!loc1.getWorld().equals(loc2.getWorld()))
            throw new IllegalArgumentException("Worlds must be the same.");

        int[] xArray = this.order(loc1.getBlockX(), loc2.getBlockX());
        int[] yArray = this.order(loc1.getBlockY(), loc2.getBlockY());
        int[] zArray = this.order(loc1.getBlockZ(), loc2.getBlockZ());

        this.minX = xArray[0];
        this.minY = yArray[0];
        this.minZ = zArray[0];

        this.maxX = xArray[1];
        this.maxY = yArray[1];
        this.maxZ = zArray[1];

        this.world = loc1.getWorld().getName();
    }

    public Cuboid(ConfigurationSection section) {

        if(!section.isSet("world"))
            throw new NullPointerException(String.format("World not found in '%s'", section.getName()));

        int[] xArray = this.order(section.getInt("x1"), section.getInt("x2"));
        int[] yArray = this.order(section.getInt("y1"), section.getInt("y2"));
        int[] zArray = this.order(section.getInt("z1"), section.getInt("z2"));

        this.minX = xArray[0];
        this.minY = yArray[0];
        this.minZ = zArray[0];

        this.maxX = xArray[1];
        this.maxY = yArray[1];
        this.maxZ = zArray[1];

        this.world = section.getString("world");
    }

    private int[] order(int v1, int v2) {
        return new int[]{ Math.min(v1, v2), Math.max(v1, v2) };
    }

    public boolean isIn(Location location) {

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return x >= this.minX && x <= this.maxX
                && y >= this.minY && y <= this.maxY
                && z >= this.minZ && z <= this.maxZ
                && location.getWorld().equals(this.getWorld());
    }

    public Location getMinLocation() {
        return new Location(this.getWorld(), this.minX, this.minY, this.minZ);
    }

    public Location getMaxLocation() {
        return new Location(this.getWorld(), this.maxX, this.maxY, this.maxZ);
    }

    public String getWorldName() {
        return this.world;
    }

    public World getWorld() {
        return Bukkit.getWorld(this.world);
    }

    @Override
    public String toString() {
        return String.format("Cuboid{ minX=%d, minY=%d, minZ=%d, maxX=%d, maxY=%d, maxZ=%d, world=%s}",
                this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.minZ, this.world);
    }
}
