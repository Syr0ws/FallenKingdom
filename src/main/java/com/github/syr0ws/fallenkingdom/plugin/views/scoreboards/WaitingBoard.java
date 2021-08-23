package com.github.syr0ws.fallenkingdom.plugin.views.scoreboards;

import com.github.syr0ws.fallenkingdom.api.model.FKModel;
import com.github.syr0ws.fallenkingdom.api.model.FKSettings;
import com.github.syr0ws.fallenkingdom.plugin.game.model.placeholders.FKPlaceholder;
import com.github.syr0ws.universe.api.attributes.Attribute;
import com.github.syr0ws.universe.api.attributes.AttributeObserver;
import com.github.syr0ws.universe.api.game.model.GameState;
import com.github.syr0ws.universe.sdk.displays.types.Message;
import com.github.syr0ws.universe.sdk.game.model.GameAttribute;
import com.github.syr0ws.universe.sdk.modules.lang.LangService;
import com.github.syr0ws.universe.sdk.modules.lang.messages.impl.Text;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;

public class WaitingBoard extends FKBoard implements AttributeObserver {

    public static final String ID = "WaitingBoard";

    private final FKModel model;

    public WaitingBoard(Player player, LangService service, FKModel model) {
        super(player, service);

        if(model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        this.model = model;
    }

    @Override
    protected String parse(String text) {

        FKSettings accessor = this.model.getSettings();

        int maxPlayers = accessor.getMaxPlayersSetting().getValue();
        int onlinePlayers = this.model.getOnlinePlayers().size();

        Message message = new Message(text);

        message.addPlaceholder(FKPlaceholder.GAME_STATE.get(), this.getState());
        message.addPlaceholder(FKPlaceholder.MAX_PLAYERS.get(), Integer.toString(maxPlayers));
        message.addPlaceholder(FKPlaceholder.ONLINE_PLAYERS.get(), Integer.toString(onlinePlayers));

        return message.getText();
    }

    @Override
    protected String getSectionName() {
        return "waiting-scoreboard";
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
        return Arrays.asList(GameAttribute.STATE_CHANGE, GameAttribute.GAME_PLAYER_CHANGE);
    }

    private String getState() {

        String state = this.model.getState() == GameState.WAITING ? "waiting" : "starting";
        String key = String.format("%s.state.%s", this.getSectionName(), state);

        Text text = this.getLangService().getMessage(key, Text.class);

        return text.getText();
    }
}
