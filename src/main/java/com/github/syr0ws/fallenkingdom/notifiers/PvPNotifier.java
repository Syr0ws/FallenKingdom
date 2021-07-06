package com.github.syr0ws.fallenkingdom.notifiers;

import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.GameAttribute;
import com.github.syr0ws.universe.attributes.Attribute;
import com.github.syr0ws.universe.attributes.AttributeObserver;
import com.github.syr0ws.universe.displays.Display;
import com.github.syr0ws.universe.displays.DisplayException;
import com.github.syr0ws.universe.displays.dao.ConfigDisplayDAO;
import com.github.syr0ws.universe.displays.dao.DisplayDAO;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Collections;

public class PvPNotifier implements AttributeObserver {

    private final FKModel model;
    private final DisplayDAO dao;

    public PvPNotifier(FKModel model, Plugin plugin) {

        if(model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        if(plugin == null)
            throw new IllegalArgumentException("Plugin cannot be null.");

        this.model = model;
        this.dao = new ConfigDisplayDAO(plugin.getConfig().getConfigurationSection("pvp"));
    }

    @Override
    public void onUpdate(Attribute attribute) {

        String path = this.model.isPvPEnabled() ? "enabled" : "disabled";

        try {

            Collection<Display> displays = this.dao.getDisplays(path);
            displays.forEach(Display::displayAll);

        } catch (DisplayException e) { e.printStackTrace(); }
    }

    @Override
    public Collection<Attribute> observed() {
        return Collections.singleton(GameAttribute.PVP_STATE);
    }
}
