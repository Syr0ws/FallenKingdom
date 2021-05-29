package com.github.syr0ws.fallenkingdom.displays.dao;

import com.github.syr0ws.fallenkingdom.displays.Display;
import com.github.syr0ws.fallenkingdom.displays.DisplayException;
import com.github.syr0ws.fallenkingdom.tools.Validate;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TimerDisplayDAO extends ConfigDisplayDAO {

    public TimerDisplayDAO(ConfigurationSection section) {
        super(section);
    }

    public Map<Integer, Collection<Display>> getTimeDisplays(String path) throws DisplayException {

        ConfigurationSection section = super.getSection().getConfigurationSection(path);

        if(section == null)
            throw new DisplayException(String.format("No display section found at '%s'.", path));

        Map<Integer, Collection<Display>> displays = new HashMap<>();

        for(String key : section.getKeys(false)) {

            if(!Validate.isInt(key))
                throw new DisplayException(String.format("Key %s.%s must be a number.", path, key));

            ConfigurationSection displaySection = section.getConfigurationSection(key);

            if(displaySection == null)
                throw new DisplayException(String.format("Invalid display section found at '%s.%s'.", path, key));

            Collection<Display> list = super.getDisplays(path + "." + key);

            displays.put(Integer.parseInt(key), list);
        }
        return displays;
    }
}
