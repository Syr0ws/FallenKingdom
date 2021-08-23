package com.github.syr0ws.fallenkingdom.notifiers;

import com.github.syr0ws.fallenkingdom.game.cycles.displays.GameRunningDisplayEnum;
import com.github.syr0ws.fallenkingdom.game.model.FKAttribute;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.universe.api.attributes.Attribute;
import com.github.syr0ws.universe.api.attributes.AttributeObserver;
import com.github.syr0ws.universe.api.displays.Display;
import com.github.syr0ws.universe.api.displays.DisplayManager;
import org.bukkit.Bukkit;

import java.util.Collection;
import java.util.Collections;

public class EndNotifier implements AttributeObserver {

    private final FKModel model;
    private final DisplayManager manager;

    public EndNotifier(FKModel model, DisplayManager manager) {

        if(model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        if(manager == null)
            throw new IllegalArgumentException("DisplayManager cannot be null.");

        this.model = model;
        this.manager = manager;
    }

    @Override
    public void onUpdate(Attribute attribute) {

        GameRunningDisplayEnum displayEnum = this.model.isEndEnabled() ?
                GameRunningDisplayEnum.END_ENABLED : GameRunningDisplayEnum.END_DISABLED;

        // Retrieving displays.
        Collection<Display> displays = this.manager.getDisplays(displayEnum.getPath());

        // Displaying displays.
        Bukkit.getOnlinePlayers().forEach(player -> displays.forEach(display -> display.displayTo(player)));
    }

    @Override
    public Collection<Attribute> observed() {
        return Collections.singleton(FKAttribute.END_STATE);
    }
}
