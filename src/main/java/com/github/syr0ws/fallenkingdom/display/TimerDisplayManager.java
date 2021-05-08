package com.github.syr0ws.fallenkingdom.display;

import com.github.syr0ws.fallenkingdom.tools.Validate;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimerDisplayManager {

    private final Map<Integer, List<Display>> displays = new HashMap<>();

    // TODO Add exceptions.
    public void load(ConfigurationSection section) {

        for(String key : section.getKeys(false)) {

            if(!Validate.isInt(key)) continue;

            int time = Integer.parseInt(key);
            List<Display> displays = this.loadDisplays(section.getConfigurationSection(key));

            this.displays.put(time, displays);
        }
    }

    private List<Display> loadDisplays(ConfigurationSection section) {

        List<Display> displays = new ArrayList<>();

        for(String key : section.getKeys(false)) {

            ConfigurationSection displaySection = section.getConfigurationSection(key);

            Display display = DisplayFactory.getDisplay(displaySection);
            displays.add(display);
        }
        return displays;
    }

    public void display(int time) {

        if(!this.displays.containsKey(time)) return;

        List<Display> displays = this.displays.get(time);
        displays.forEach(Display::displayAll);
    }
}
