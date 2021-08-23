package com.github.syr0ws.fallenkingdom.plugin.notifiers;

import com.github.syr0ws.fallenkingdom.api.model.FKModel;
import com.github.syr0ws.fallenkingdom.plugin.displays.GameDisplayEnum;
import com.github.syr0ws.fallenkingdom.plugin.game.model.FKAttribute;
import com.github.syr0ws.universe.api.attributes.Attribute;
import com.github.syr0ws.universe.api.attributes.AttributeObserver;
import com.github.syr0ws.universe.api.displays.Display;
import com.github.syr0ws.universe.api.displays.DisplayManager;
import org.bukkit.Bukkit;

import java.util.Collection;
import java.util.Collections;

public class PvPNotifier implements AttributeObserver {

    private final FKModel model;
    private final DisplayManager manager;

    public PvPNotifier(FKModel model, DisplayManager manager) {

        if(model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        if(manager == null)
            throw new IllegalArgumentException("DisplayManager cannot be null.");

        this.model = model;
        this.manager = manager;
    }

    @Override
    public void onUpdate(Attribute attribute) {

        GameDisplayEnum displayEnum = this.model.isPvPEnabled() ?
                GameDisplayEnum.PVP_ENABLED : GameDisplayEnum.PVP_DISABLED;

        // Retrieving displays.
        Collection<Display> displays = this.manager.getDisplays(displayEnum.getPath());

        // Displaying displays.
        Bukkit.getOnlinePlayers().forEach(player -> displays.forEach(display -> display.displayTo(player)));
    }

    @Override
    public Collection<Attribute> observed() {
        return Collections.singleton(FKAttribute.PVP_STATE);
    }
}
