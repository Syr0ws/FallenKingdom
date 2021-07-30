package com.github.syr0ws.fallenkingdom.views;

import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.placeholders.FKPlaceholder;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeamPlayer;
import com.github.syr0ws.universe.commons.modules.lang.LangService;
import com.github.syr0ws.universe.commons.modules.lang.messages.impl.Text;
import com.github.syr0ws.universe.commons.modules.lang.utils.LangUtils;
import com.github.syr0ws.universe.commons.modules.view.views.ActionBarView;
import com.github.syr0ws.universe.sdk.displays.types.ActionBar;
import com.github.syr0ws.universe.sdk.displays.types.LegacyActionBar;
import com.github.syr0ws.universe.sdk.tools.direction.Direction;
import com.github.syr0ws.universe.sdk.tools.direction.DirectionUtils;
import org.bukkit.Location;

public class GameActionBar extends ActionBarView {

    public static final String ID = "GameActionBar";

    private final FKTeamPlayer player;
    private final FKModel model;
    private final LangService service;

    public GameActionBar(FKTeamPlayer player, FKModel model, LangService service) {

        if(player == null)
            throw new IllegalArgumentException("FKTeamPlayer cannot be null.");

        if(model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        if(service == null)
            throw new IllegalArgumentException("LangService cannot be null.");

        this.player = player;
        this.model = model;
        this.service = service;
    }

    @Override
    public void set() {
        this.update();
    }

    @Override
    public void update() {

        Text format = this.service.getMessage("game-action-bar-text", Text.class);

        Location spawn = this.model.getSpawn();
        Location base = this.player.getTeam().getBase().getSpawn();

        String spawnDirection = this.getDirection(spawn);
        String baseDirection = this.getDirection(base);

        format.addPlaceholder(FKPlaceholder.CENTER_DIRECTION.get(), spawnDirection);
        format.addPlaceholder(FKPlaceholder.BASE_DIRECTION.get(), baseDirection);

        ActionBar actionBar = new LegacyActionBar(format.getText());
        actionBar.displayTo(this.player.getPlayer());
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

    @Override
    public boolean isUpdatable() {
        return true;
    }

    private String getDirection(Location location) {

        Direction direction = DirectionUtils.getDirectionToGoTo(this.player.getPlayer(), location);

        return LangUtils.getDirection(direction, this.service);
    }
}
