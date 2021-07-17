package com.github.syr0ws.fallenkingdom.scoreboards;

import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.GameAttribute;
import com.github.syr0ws.fallenkingdom.game.model.placeholders.FKPlaceholder;
import com.github.syr0ws.universe.attributes.Attribute;
import com.github.syr0ws.universe.attributes.AttributeObserver;
import com.github.syr0ws.universe.displays.types.Message;
import com.github.syr0ws.universe.modules.lang.LangService;
import com.github.syr0ws.universe.modules.lang.messages.impl.Text;
import com.github.syr0ws.universe.modules.scoreboard.ScoreboardManager;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;

public class SpectatorBoard extends FKBoard implements AttributeObserver {

    private static final String SCOREBOARD_SECTION = "spectator-scoreboard";
    private final FKModel model;
    // private final PeriodFormatter formatter;

    public SpectatorBoard(ScoreboardManager manager, Player player, LangService service, FKModel model) {
        super(manager, player, service);

        if(model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        this.model = model;
        // this.formatter = new PeriodFormatter(this.getScoreboardSection().getConfigurationSection("time"));
    }

    @Override
    protected String getSectionName() {
        return "spectator-scoreboard";
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
    public void onUpdate(Attribute attribute) {
        this.update();
    }

    @Override
    public Collection<Attribute> observed() {
        return Arrays.asList(GameAttribute.PVP_STATE, GameAttribute.ASSAULTS_STATE, GameAttribute.TIME_CHANGE);
    }

    @Override
    protected String parse(String text) {

        // String timeFormat = this.getScoreboardSection().getString("time.format", "");
        // String time = this.formatter.format(timeFormat, this.model.getTime());

        Message message = new Message(text);

        message.addPlaceholder(FKPlaceholder.PVP_STATE.get(), this.getPvPState());
        message.addPlaceholder(FKPlaceholder.ASSAULTS_STATE.get(), this.getAssaultsState());
        // message.addPlaceholder(FKPlaceholder.TIME, time);

        return message.getText();
    }

    private String getPvPState() {

        String key = String.format("%s.pvp-state.%s", this.getSectionName(), (this.model.isPvPEnabled() ? "enabled" : "disabled"));

        Text text = this.getLangService().getMessage(key, Text.class);

        return text.getText();
    }

    private String getAssaultsState() {

        String key = String.format("%s.assaults-state.%s", this.getSectionName(), (this.model.isPvPEnabled() ? "enabled" : "disabled"));

        Text text = this.getLangService().getMessage(key, Text.class);

        return text.getText();
    }
}
