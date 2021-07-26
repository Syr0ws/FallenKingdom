package com.github.syr0ws.fallenkingdom.scoreboards;

import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.placeholders.FKPlaceholder;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeamPlayer;
import com.github.syr0ws.universe.commons.modules.lang.LangService;
import com.github.syr0ws.universe.commons.modules.lang.messages.impl.Text;
import com.github.syr0ws.universe.sdk.displays.types.Message;
import org.bukkit.entity.Player;

public class GameBoard extends FKBoard {

    public static final String ID = "GameBoard";

    private final FKModel model;
    private final FKTeamPlayer teamPlayer;

    public GameBoard(Player player, LangService service, FKModel model) {
        super(player, service);

        if(model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        this.model = model;
        this.teamPlayer = model.getTeamPlayer(player.getUniqueId())
                .orElseThrow(() -> new NullPointerException("FKTeamPlayer not found."));
    }

    @Override
    protected String getSectionName() {
        return "game-scoreboard";
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public int getPriority() {
        return NORMAL_PRIORITY;
    }

    @Override
    protected String parse(String text) {

        Message message = new Message(text);

        message.addPlaceholder(FKPlaceholder.TEAM_NAME.get(), this.teamPlayer.getTeam().getDisplayName());
        message.addPlaceholder(FKPlaceholder.PVP_STATE.get(), this.getPvPState());
        message.addPlaceholder(FKPlaceholder.ASSAULTS_STATE.get(), this.getAssaultsState());
        message.addPlaceholder(FKPlaceholder.KILLS.get(), Integer.toString(this.teamPlayer.getKills()));
        message.addPlaceholder(FKPlaceholder.DEATHS.get(), Integer.toString(this.teamPlayer.getDeaths()));
        message.addPlaceholder(FKPlaceholder.KDR.get(), Double.toString(this.teamPlayer.getKDR()));

        return message.getText();
    }

    private String getPvPState() {

        String key = String.format("%s.pvp-state.%s", this.getSectionName(), (this.model.isPvPEnabled() ? "enabled" : "disabled"));

        Text text = this.getLangService().getMessage(key, Text.class);

        return text.getText();
    }

    private String getAssaultsState() {

        String key = String.format("%s.assaults-state.%s", this.getSectionName(), (this.model.areAssaultsEnabled() ? "enabled" : "disabled"));

        Text text = this.getLangService().getMessage(key, Text.class);

        return text.getText();
    }
}
