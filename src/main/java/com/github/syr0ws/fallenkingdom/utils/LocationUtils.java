package com.github.syr0ws.fallenkingdom.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class LocationUtils {

    public static Location getLocation(ConfigurationSection section) {

        double x = section.getDouble("x");
        double y = section.getDouble("y");
        double z = section.getDouble("z");

        float yaw = (float) section.getDouble("yaw");
        float pitch = (float) section.getDouble("pitch");

        String worldName = section.getString("world");
        World world = Bukkit.getWorld(worldName);

        if(world == null)
            throw new IllegalArgumentException(String.format("World '%s' doesn't exist.", worldName));

        return new Location(world, x, y, z, yaw, pitch);
    }
}
