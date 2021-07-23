package com.github.syr0ws.fallenkingdom.capture.area.dao;

import com.github.syr0ws.fallenkingdom.capture.Capturable;
import com.github.syr0ws.fallenkingdom.capture.CapturableDAO;
import com.github.syr0ws.fallenkingdom.capture.area.model.CraftAreaModel;
import com.github.syr0ws.universe.sdk.tools.Cuboid;
import org.bukkit.configuration.ConfigurationSection;

public class AreaDAO implements CapturableDAO {

    @Override
    public Capturable loadCapturable(ConfigurationSection section) {

        ConfigurationSection areaSection = section.getConfigurationSection("area");

        if(areaSection == null)
            throw new IllegalArgumentException(String.format("Section '%s.area' not found.", section.getName()));

        Cuboid cuboid = new Cuboid(areaSection);

        return new CraftAreaModel(cuboid);
    }
}
