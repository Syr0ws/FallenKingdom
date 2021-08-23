package com.github.syr0ws.fallenkingdom.views.scoreboards;

import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.placeholders.FKPlaceholder;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeamPlayer;
import com.github.syr0ws.universe.sdk.displays.types.Message;
import com.github.syr0ws.universe.sdk.modules.lang.LangService;
import org.bukkit.entity.Player;

public class PlayerGameBoard extends FKGameBoard {

    public static final String ID = "PlayerGameBoard";

    private final FKTeamPlayer teamPlayer;

    public PlayerGameBoard(Player player, LangService service, FKModel model) {
        super(player, service, model);

        this.teamPlayer = model.getTeamPlayer(player.getUniqueId())
                .orElseThrow(() -> new NullPointerException("FKTeamPlayer not found."));
    }

    @Override
    protected String parse(String text) {

        text = super.parse(text);

        Message message = new Message(text);

        message.addPlaceholder(FKPlaceholder.TEAM_NAME.get(), this.teamPlayer.getTeam().getDisplayName());
        message.addPlaceholder(FKPlaceholder.KILLS.get(), Integer.toString(this.teamPlayer.getKills()));
        message.addPlaceholder(FKPlaceholder.DEATHS.get(), Integer.toString(this.teamPlayer.getDeaths()));
        message.addPlaceholder(FKPlaceholder.KDR.get(), Double.toString(this.teamPlayer.getKDR()));

        return message.getText();
    }

    @Override
    protected String getSectionName() {
        return "game-scoreboard";
    }

    @Override
    public String getId() {
        return ID;
    }
}
