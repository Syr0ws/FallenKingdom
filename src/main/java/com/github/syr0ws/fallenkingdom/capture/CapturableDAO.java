package com.github.syr0ws.fallenkingdom.capture;

import org.bukkit.configuration.ConfigurationSection;

public interface CapturableDAO {

    Capturable loadCapturable(ConfigurationSection section);
}
