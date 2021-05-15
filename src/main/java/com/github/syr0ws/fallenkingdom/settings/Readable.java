package com.github.syr0ws.fallenkingdom.settings;

import org.bukkit.configuration.ConfigurationSection;

public interface Readable {

    void read(ConfigurationSection section, String key);
}
