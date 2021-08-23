package com.github.syr0ws.fallenkingdom.api.capture;

import org.bukkit.configuration.ConfigurationSection;

public interface CapturableDAO {

    Capturable loadCapturable(ConfigurationSection section);
}
