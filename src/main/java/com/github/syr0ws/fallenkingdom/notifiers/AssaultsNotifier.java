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

public class AssaultsNotifier implements AttributeObserver {

    private final FKModel model;
    private final DisplayManager manager;

    public AssaultsNotifier(FKModel model, DisplayManager manager) {

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
                GameRunningDisplayEnum.ASSAULTS_ENABLED : GameRunningDisplayEnum.ASSAULTS_DISABLED;

        // Retrieving displays.
        Collection<Display> displays = this.manager.getDisplays(displayEnum.getPath());

        // Displaying displays.
        Bukkit.getOnlinePlayers().forEach(player -> displays.forEach(display -> display.displayTo(player)));
    }

    @Override
    public Collection<Attribute> observed() {
        return Collections.singleton(GameAttribute.ASSAULTS_STATE);
    }
}
