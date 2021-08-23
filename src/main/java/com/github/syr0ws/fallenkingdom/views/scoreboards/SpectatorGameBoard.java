package com.github.syr0ws.fallenkingdom.views.scoreboards;

import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.universe.sdk.modules.lang.LangService;
import org.bukkit.entity.Player;

public class SpectatorGameBoard extends FKGameBoard {

    public static final String ID = "SpectatorGameBoard";

    public SpectatorGameBoard(Player player, LangService service, FKModel model) {
        super(player, service, model);
    }

    @Override
    protected String getSectionName() {
        return "spectator-scoreboard";
    }

    @Override
    public String getId() {
        return ID;
    }
}
