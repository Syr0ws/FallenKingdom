package com.github.syr0ws.fallenkingdom.views;

import com.github.syr0ws.universe.commons.modules.lang.LangService;
import com.github.syr0ws.universe.commons.modules.lang.messages.impl.Text;
import com.github.syr0ws.universe.commons.modules.view.views.ActionBarView;
import com.github.syr0ws.universe.sdk.displays.types.ActionBar;
import com.github.syr0ws.universe.sdk.displays.types.LegacyActionBar;
import org.bukkit.entity.Player;

public class GameActionBar extends ActionBarView {

    public static final String ID = "LocationTrackView";

    private final Player player;
    private final LangService service;

    public GameActionBar(Player player, LangService service) {

        if(player == null)
            throw new IllegalArgumentException("Player cannot be null.");

        if(service == null)
            throw new IllegalArgumentException("LangService cannot be null.");

        this.player = player;
        this.service = service;
    }

    @Override
    public void set() {
        this.update();
    }

    @Override
    public void update() {

        Text format = this.service.getMessage("game-action-bar-text", Text.class);

        ActionBar actionBar = new LegacyActionBar(format.getText());
        actionBar.displayTo(this.player);
    }

    @Override
    public void remove() {}

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public int getPriority() {
        return NORMAL_PRIORITY;
    }
}
