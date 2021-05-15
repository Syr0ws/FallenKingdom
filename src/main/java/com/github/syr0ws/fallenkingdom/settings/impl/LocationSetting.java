package com.github.syr0ws.fallenkingdom.settings.impl;

import com.github.syr0ws.fallenkingdom.settings.Readable;
import com.github.syr0ws.fallenkingdom.settings.SettingFilter;
import com.github.syr0ws.fallenkingdom.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class LocationSetting extends MutableSetting<Location> implements Readable {

    public LocationSetting(String name) {
        super(name, Bukkit.getWorld("world").getSpawnLocation());
    }

    public LocationSetting(String name, SettingFilter<Location> filter) {
        super(name, Bukkit.getWorld("world").getSpawnLocation(), filter);
    }

    public LocationSetting(String name, Location defaultValue, SettingFilter<Location> filter) {
        super(name, defaultValue, filter);
    }

    public LocationSetting(String name, Location value, Location defaultValue, SettingFilter<Location> filter) {
        super(name, value, defaultValue, filter);
    }

    @Override
    public void read(ConfigurationSection section, String key) {
        Location location = LocationUtils.getLocation(section);
        super.setValue(location);
    }
}
