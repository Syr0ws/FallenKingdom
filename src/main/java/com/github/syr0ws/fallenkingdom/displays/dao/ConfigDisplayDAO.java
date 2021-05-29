package com.github.syr0ws.fallenkingdom.displays.dao;

import com.github.syr0ws.fallenkingdom.displays.Display;
import com.github.syr0ws.fallenkingdom.displays.DisplayException;
import com.github.syr0ws.fallenkingdom.displays.DisplayFactory;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConfigDisplayDAO implements DisplayDAO {

    private final ConfigurationSection section;

    public ConfigDisplayDAO(ConfigurationSection section) {

        if(section == null)
            throw new IllegalArgumentException("ConfigurationSection cannot be null.");

        this.section = section;
    }

    @Override
    public Display getDisplay(String path) throws DisplayException {

        ConfigurationSection section = this.section.getConfigurationSection(path);

        if(section == null)
            throw new DisplayException(String.format("No display section found at '%s.%s'.", this.section.getName(), path));

        return DisplayFactory.getDisplay(section);
    }

    @Override
    public Collection<Display> getDisplays(String path) throws DisplayException {

        ConfigurationSection section = this.section.getConfigurationSection(path);

        if(section == null)
            throw new DisplayException(String.format("No display section found at '%s.%s'.", this.section.getName(), path));

        List<Display> displays = new ArrayList<>();

        for(String key : section.getKeys(false)) {

            ConfigurationSection displaySection = section.getConfigurationSection(key);

            if(displaySection == null)
                throw new DisplayException(String.format("Invalid display section found at '%s.%s'.", section.getCurrentPath(), key));

            Display display = DisplayFactory.getDisplay(displaySection);

            displays.add(display);
        }
        return displays;
    }

    public ConfigurationSection getSection() {
        return this.section;
    }
}
