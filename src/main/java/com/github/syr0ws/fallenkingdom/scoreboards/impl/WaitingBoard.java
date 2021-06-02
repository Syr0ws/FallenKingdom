package com.github.syr0ws.fallenkingdom.scoreboards.impl;

import com.github.syr0ws.fallenkingdom.attributes.Attribute;
import com.github.syr0ws.fallenkingdom.attributes.AttributeObserver;
import com.github.syr0ws.fallenkingdom.displays.impl.Message;
import com.github.syr0ws.fallenkingdom.displays.placeholders.Placeholder;
import com.github.syr0ws.fallenkingdom.displays.placeholders.impl.GamePlaceholder;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.GameState;
import com.github.syr0ws.fallenkingdom.game.model.attributes.GameAttribute;
import com.github.syr0ws.fallenkingdom.game.model.settings.SettingAccessor;
import com.github.syr0ws.fallenkingdom.scoreboards.AbstractFastBoard;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class WaitingBoard extends AbstractFastBoard implements AttributeObserver {

    private final GameModel game;
    private final FileConfiguration config;

    public WaitingBoard(GameModel game, Player player, FileConfiguration config) {
        super(player);

        if(game == null)
            throw new IllegalArgumentException("GameModel cannot be null.");

        if(config == null)
            throw new IllegalArgumentException("Config cannot be null.");

        this.game = game;
        this.config = config;

        this.game.addObserver(this);
    }

    @Override
    public void delete() {
        super.delete();
        this.game.removeObserver(this);
    }

    @Override
    protected List<String> parseLines(Collection<String> lines) {

        SettingAccessor accessor = this.game.getSettings();

        int maxPlayers = accessor.getMaxPlayersSetting().getValue();
        int onlinePlayers = Bukkit.getOnlinePlayers().size();

        return lines.stream().map(line -> {

            Message message = new Message(line);

            message.addPlaceholder(SbPlaceholder.STATE, this.getState());
            message.addPlaceholder(GamePlaceholder.MAX_PLAYERS, Integer.toString(maxPlayers));
            message.addPlaceholder(GamePlaceholder.ONLINE_PLAYERS, Integer.toString(onlinePlayers));

            return message.getText();

        }).collect(Collectors.toList());
    }

    @Override
    protected ConfigurationSection getBoardSection() {
        return this.config.getConfigurationSection("waiting-scoreboard");
    }

    @Override
    public void onUpdate(Attribute attribute) {
        this.update();
    }

    @Override
    public Collection<Attribute> observed() {
        return Collections.singleton(GameAttribute.STATE_CHANGE);
    }

    private String getState() {

        ConfigurationSection section = this.getBoardSection().getConfigurationSection("state");

        String path = this.game.getState() == GameState.WAITING ? "waiting" : "starting";

        return section.getString(path, "");
    }

    private enum SbPlaceholder implements Placeholder {

        STATE("state");

        private final String placeholder;

        SbPlaceholder(String placeholder) {
            this.placeholder = placeholder;
        }

        @Override
        public String get() {
            return "%" + this.placeholder + "%";
        }
    }
}
