package com.github.syr0ws.fallenkingdom.capture.area.dao;

import com.github.syr0ws.fallenkingdom.capture.Capturable;
import com.github.syr0ws.fallenkingdom.capture.CapturableDAO;
import com.github.syr0ws.fallenkingdom.capture.area.model.CraftAreaModel;
import com.github.syr0ws.universe.tools.Cuboid;
import org.bukkit.configuration.ConfigurationSection;

public class AreaDAO implements CapturableDAO {

    @Override
    public Capturable loadCapturable(ConfigurationSection section) {

        Cuboid cuboid = new Cuboid(section);

        return new CraftAreaModel(cuboid);
    }
}
