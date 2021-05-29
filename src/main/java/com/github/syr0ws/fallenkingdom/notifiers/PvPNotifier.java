package com.github.syr0ws.fallenkingdom.notifiers;

import com.github.syr0ws.fallenkingdom.attributes.Attribute;
import com.github.syr0ws.fallenkingdom.attributes.AttributeObserver;
import com.github.syr0ws.fallenkingdom.displays.Display;
import com.github.syr0ws.fallenkingdom.displays.DisplayException;
import com.github.syr0ws.fallenkingdom.displays.dao.ConfigDisplayDAO;
import com.github.syr0ws.fallenkingdom.displays.dao.DisplayDAO;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.attributes.GameAttribute;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Collections;

public class PvPNotifier implements AttributeObserver {

    private final GameModel model;
    private final DisplayDAO dao;

    public PvPNotifier(GameModel model, Plugin plugin) {

        if(model == null)
            throw new IllegalArgumentException("GameModel cannot be null.");

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
