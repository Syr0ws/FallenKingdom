package com.github.syr0ws.fallenkingdom.scoreboards;

import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.GameAttribute;
import com.github.syr0ws.fallenkingdom.game.model.placeholders.FKPlaceholder;
import com.github.syr0ws.fallenkingdom.game.model.settings.FKSettings;
import com.github.syr0ws.universe.commons.modules.lang.LangService;
import com.github.syr0ws.universe.commons.modules.lang.messages.impl.Text;
import com.github.syr0ws.universe.commons.modules.scoreboard.ScoreboardManager;
import com.github.syr0ws.universe.sdk.attributes.Attribute;
import com.github.syr0ws.universe.sdk.attributes.AttributeObserver;
import com.github.syr0ws.universe.sdk.displays.types.Message;
import com.github.syr0ws.universe.sdk.game.model.GameState;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;

public class WaitingBoard extends FKBoard implements AttributeObserver {

    private static final String SCOREBOARD_SECTION = "waiting-scoreboard";

    private final FKModel model;

    public WaitingBoard(ScoreboardManager manager, Player player, LangService service, FKModel model) {
        super(manager, player, service);

        if(model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        this.model = model;
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
    public void onUpdate(Attribute attribute) {
        this.update();
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
