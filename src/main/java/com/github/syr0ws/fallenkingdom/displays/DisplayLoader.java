package com.github.syr0ws.fallenkingdom.displays;

import org.bukkit.configuration.ConfigurationSection;

public interface DisplayLoader {

    Display load(ConfigurationSection section);
}
