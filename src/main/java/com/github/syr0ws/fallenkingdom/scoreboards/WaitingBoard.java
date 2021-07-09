package com.github.syr0ws.fallenkingdom.scoreboards;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.GameAttribute;
import com.github.syr0ws.fallenkingdom.game.model.GameState;
import com.github.syr0ws.fallenkingdom.game.model.placeholders.FKPlaceholder;
import com.github.syr0ws.fallenkingdom.game.model.settings.SettingAccessor;
import com.github.syr0ws.universe.attributes.Attribute;
import com.github.syr0ws.universe.attributes.AttributeObserver;
import com.github.syr0ws.universe.displays.impl.Message;
import com.github.syr0ws.universe.modules.scoreboard.ScoreboardManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;

public class WaitingBoard extends FKBoard implements AttributeObserver {

    private static final String SCOREBOARD_SECTION = "waiting-scoreboard";

    private final FKGame game;
    private final FKModel model;

    public WaitingBoard(ScoreboardManager manager, Player player, FKGame game) {
        super(manager, player);

        if(game == null)
            throw new IllegalArgumentException("FKGame cannot be null.");

        this.game = game;
        this.model = game.getGameModel();
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

        SettingAccessor accessor = this.model.getSettings();

        int maxPlayers = accessor.getMaxPlayersSetting().getValue();
        int onlinePlayers = this.model.getOnlinePlayers().size();

        Message message = new Message(text);

        message.addPlaceholder(FKPlaceholder.GAME_STATE, this.getState());
        message.addPlaceholder(FKPlaceholder.MAX_PLAYERS, Integer.toString(maxPlayers));
        message.addPlaceholder(FKPlaceholder.ONLINE_PLAYERS, Integer.toString(onlinePlayers));

        return super.parse(message.getText());
    }

    @Override
    protected ConfigurationSection getScoreboardSection() {

        FileConfiguration config = this.game.getConfig();
        ConfigurationSection section = config.getConfigurationSection(SCOREBOARD_SECTION);

        if(section == null)
            throw new NullPointerException(String.format("ConfigurationSection '%s' not found.", SCOREBOARD_SECTION));

        return section;
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

        ConfigurationSection section = this.getScoreboardSection().getConfigurationSection("state");

        String path = this.model.getState() == GameState.WAITING ? "waiting" : "starting";

        return section.getString(path, "");
    }
}
