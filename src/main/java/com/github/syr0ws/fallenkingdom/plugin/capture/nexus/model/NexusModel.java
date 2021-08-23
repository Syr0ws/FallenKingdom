package com.github.syr0ws.fallenkingdom.plugin.capture.nexus.model;

import com.github.syr0ws.fallenkingdom.api.capture.Capturable;
import org.bukkit.Location;

public interface NexusModel extends Capturable {

    void addHealth(int health);

    void removeHealth(int heath);

    boolean isAlive();

    int getHealth();

    Location getLocation();
}
