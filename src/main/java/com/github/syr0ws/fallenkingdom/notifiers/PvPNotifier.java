package com.github.syr0ws.fallenkingdom.notifiers;

import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.GameAttribute;
import com.github.syr0ws.fallenkingdom.game.model.cycles.displays.GameRunningDisplayEnum;
import com.github.syr0ws.universe.attributes.Attribute;
import com.github.syr0ws.universe.attributes.AttributeObserver;
import com.github.syr0ws.universe.displays.Display;
import com.github.syr0ws.universe.displays.DisplayManager;
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

        GameRunningDisplayEnum displayEnum = this.model.areAssaultsEnabled() ?
                GameRunningDisplayEnum.PVP_ENABLED : GameRunningDisplayEnum.PVP_DISABLED;

        // Retrieving displays.
        Collection<Display> displays = this.manager.getDisplays(displayEnum.getPath());

        // Displaying displays.
        Bukkit.getOnlinePlayers().forEach(player -> displays.forEach(display -> display.displayTo(player)));
    }

    @Override
    public Collection<Attribute> observed() {
        return Collections.singleton(GameAttribute.PVP_STATE);
    }
}
