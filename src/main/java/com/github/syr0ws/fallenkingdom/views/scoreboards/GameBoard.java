package com.github.syr0ws.fallenkingdom.views.scoreboards;

import com.github.syr0ws.fallenkingdom.game.model.FKAttribute;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.placeholders.FKPlaceholder;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeamPlayer;
import com.github.syr0ws.universe.commons.model.GameAttribute;
import com.github.syr0ws.universe.commons.modules.lang.LangService;
import com.github.syr0ws.universe.commons.modules.lang.messages.impl.Text;
import com.github.syr0ws.universe.sdk.attributes.Attribute;
import com.github.syr0ws.universe.sdk.attributes.AttributeObserver;
import com.github.syr0ws.universe.sdk.displays.types.Message;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;

public class GameBoard extends FKBoard implements AttributeObserver {

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

    @Override
    protected String getSectionName() {
        return "game-scoreboard";
    }

    @Override
    public void set() {
        super.set();
        this.model.addObserver(this);
    }

    @Override
    public void remove() {
        super.remove();
        this.model.removeObserver(this);
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
    public void onUpdate(Attribute attribute) {
        super.update();
    }

    @Override
    public Collection<Attribute> observed() {
        return Arrays.asList(
                GameAttribute.TIME_CHANGE,
                FKAttribute.PVP_STATE,
                FKAttribute.ASSAULTS_STATE,
                FKAttribute.NETHER_STATE,
                FKAttribute.END_STATE
        );
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
