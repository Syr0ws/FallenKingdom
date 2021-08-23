package com.github.syr0ws.fallenkingdom.views.scoreboards;

import com.github.syr0ws.fallenkingdom.game.model.FKAttribute;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.placeholders.FKPlaceholder;
import com.github.syr0ws.universe.api.attributes.Attribute;
import com.github.syr0ws.universe.api.attributes.AttributeObserver;
import com.github.syr0ws.universe.sdk.displays.types.Message;
import com.github.syr0ws.universe.sdk.game.model.GameAttribute;
import com.github.syr0ws.universe.sdk.modules.lang.LangService;
import com.github.syr0ws.universe.sdk.modules.lang.messages.impl.Text;
import com.github.syr0ws.universe.sdk.modules.lang.utils.LangUtils;
import com.github.syr0ws.universe.sdk.tools.time.TimeFormatter;
import com.github.syr0ws.universe.sdk.tools.time.TimeUnit;
import com.github.syr0ws.universe.sdk.tools.time.TimeUnitTranslation;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public abstract class FKGameBoard extends FKBoard implements AttributeObserver {

    private final FKModel model;
    private final TimeFormatter formatter;

    public FKGameBoard(Player player, LangService service, FKModel model) {
        super(player, service);

        if(model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        this.model = model;

        Map<TimeUnit, TimeUnitTranslation> translations = LangUtils.loadTranslations(service);
        this.formatter = new TimeFormatter(translations);
    }

    @Override
    protected String parse(String text) {

        text = this.formatter.format(text, this.model.getTime());

        Message message = new Message(text);

        message.addPlaceholder(FKPlaceholder.PVP_STATE.get(), this.getPvPState());
        message.addPlaceholder(FKPlaceholder.ASSAULTS_STATE.get(), this.getAssaultsState());
        message.addPlaceholder(FKPlaceholder.NETHER_STATE.get(), this.getNetherState());
        message.addPlaceholder(FKPlaceholder.END_STATE.get(), this.getEndState());

        return message.getText();
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

    private String getNetherState() {

        String key = String.format("%s.nether-state.%s", this.getSectionName(), (this.model.isNetherEnabled() ? "enabled" : "disabled"));
        Text text = this.getLangService().getMessage(key, Text.class);

        return text.getText();
    }

    private String getEndState() {

        String key = String.format("%s.end-state.%s", this.getSectionName(), (this.model.isEndEnabled() ? "enabled" : "disabled"));
        Text text = this.getLangService().getMessage(key, Text.class);

        return text.getText();
    }
}
