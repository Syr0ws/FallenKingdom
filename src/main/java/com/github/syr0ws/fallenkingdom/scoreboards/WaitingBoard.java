package com.github.syr0ws.fallenkingdom.scoreboards;

import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.placeholders.FKPlaceholder;
import com.github.syr0ws.fallenkingdom.game.model.settings.FKSettings;
import com.github.syr0ws.universe.commons.modules.lang.LangService;
import com.github.syr0ws.universe.commons.modules.lang.messages.impl.Text;
import com.github.syr0ws.universe.sdk.displays.types.Message;
import com.github.syr0ws.universe.sdk.game.model.GameState;
import org.bukkit.entity.Player;

public class WaitingBoard extends FKBoard {

    public static final String ID = "WaitingBoard";

    private final FKModel model;

    public WaitingBoard(Player player, LangService service, FKModel model) {
        super(player, service);

        if(model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        this.model = model;
    }

    @Override
    protected String getSectionName() {
        return "waiting-scoreboard";
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

    private String getState() {

        String state = this.model.getState() == GameState.WAITING ? "waiting" : "starting";
        String key = String.format("%s.state.%s", this.getSectionName(), state);

        Text text = this.getLangService().getMessage(key, Text.class);

        return text.getText();
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public int getPriority() {
        return NORMAL_PRIORITY;
    }
}
