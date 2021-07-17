package com.github.syr0ws.fallenkingdom.timer;

import com.github.syr0ws.universe.displays.Display;
import com.github.syr0ws.universe.displays.DisplayException;
import com.github.syr0ws.universe.displays.DisplayFactory;
import com.github.syr0ws.universe.displays.dao.ConfigDisplayDAO;
import com.github.syr0ws.universe.tools.Validate;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TimerDisplayDAO extends ConfigDisplayDAO {

    public TimerDisplayDAO(DisplayFactory factory, ConfigurationSection section) {
        super(factory, section);
    }

    public Map<Integer, Collection<Display>> getTimeDisplays(String path) throws DisplayException {

        ConfigurationSection section = super.getSection().getConfigurationSection(path);

        if(section == null)
            throw new DisplayException(String.format("No display section found at '%s.%s'.", super.getSection().getName(), path));

        Map<Integer, Collection<Display>> displays = new HashMap<>();

        for(String key : section.getKeys(false)) {

            if(!Validate.isInt(key))
                throw new DisplayException(String.format("Key %s.%s must be a number.", section.getCurrentPath(), key));

            ConfigurationSection displaySection = section.getConfigurationSection(key);

            if(displaySection == null)
                throw new DisplayException(String.format("Invalid display section found at '%s.%s'.", section.getCurrentPath(), key));

            Collection<Display> list = super.getDisplays(path + "." + key);

            displays.put(Integer.parseInt(key), list);
        }
        return displays;
    }
}
