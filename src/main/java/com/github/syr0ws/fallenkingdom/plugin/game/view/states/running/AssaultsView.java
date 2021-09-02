package com.github.syr0ws.fallenkingdom.plugin.game.view.states.running;

import com.github.syr0ws.fallenkingdom.api.model.FKModel;
import com.github.syr0ws.fallenkingdom.plugin.game.model.FKAttribute;
import com.github.syr0ws.fallenkingdom.plugin.game.view.displays.GameDisplayEnum;
import com.github.syr0ws.universe.api.attributes.Attribute;
import com.github.syr0ws.universe.api.attributes.AttributeObserver;
import com.github.syr0ws.universe.api.displays.Display;
import com.github.syr0ws.universe.api.displays.DisplayManager;
import com.github.syr0ws.universe.api.game.view.GameView;
import org.bukkit.Bukkit;

import java.util.Collection;
import java.util.Collections;

public class AssaultsView implements GameView, AttributeObserver {

    private final FKModel model;
    private final DisplayManager manager;

    public AssaultsView(FKModel model, DisplayManager manager) {

        if(model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        if(manager == null)
            throw new IllegalArgumentException("DisplayManager cannot be null.");

        this.model = model;
        this.manager = manager;
    }

    @Override
    public void enable() {
        this.model.addObserver(this);
    }

    @Override
    public void disable() {
        this.model.removeObserver(this);
    }

    @Override
    public void onUpdate(Attribute attribute) {

        GameDisplayEnum displayEnum = this.model.areAssaultsEnabled() ?
                GameDisplayEnum.ASSAULTS_ENABLED : GameDisplayEnum.ASSAULTS_DISABLED;

        // Retrieving displays.
        Collection<Display> displays = this.manager.getDisplays(displayEnum.getPath());

        // Displaying displays.
        Bukkit.getOnlinePlayers().forEach(player -> displays.forEach(display -> display.displayTo(player)));
    }

    @Override
    public Collection<Attribute> observed() {
        return Collections.singleton(FKAttribute.ASSAULTS_STATE);
    }
}
