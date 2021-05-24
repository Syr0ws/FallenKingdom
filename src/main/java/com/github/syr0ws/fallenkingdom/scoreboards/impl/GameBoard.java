package com.github.syr0ws.fallenkingdom.scoreboards.impl;

import com.github.syr0ws.fallenkingdom.attributes.Attribute;
import com.github.syr0ws.fallenkingdom.attributes.AttributeObserver;
import com.github.syr0ws.fallenkingdom.displays.impl.Message;
import com.github.syr0ws.fallenkingdom.displays.placeholders.impl.GamePlaceholder;
import com.github.syr0ws.fallenkingdom.displays.placeholders.impl.TeamPlaceholder;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.attributes.GameAttribute;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;
import com.github.syr0ws.fallenkingdom.scoreboards.AbstractFastBoard;
import com.github.syr0ws.fallenkingdom.utils.TextUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class GameBoard extends AbstractFastBoard implements AttributeObserver {

    private final GameModel game;
    private final TeamPlayer player;
    private final FileConfiguration config;

    public GameBoard(GameModel game, TeamPlayer player, FileConfiguration config) {
        super(player.getPlayer());

        if(game == null)
            throw new IllegalArgumentException("GameModel cannot be null.");

        if(config == null)
            throw new IllegalArgumentException("Config cannot be null.");

        this.game = game;
        this.player = player;
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

        return lines.stream().map(line -> {

            Message message = new Message(line);
            message.addPlaceholder(TeamPlaceholder.TEAM_NAME, this.getPlayerTeam());
            message.addPlaceholder(GamePlaceholder.PVP_STATE, this.getPvPState());
            message.addPlaceholder(GamePlaceholder.ASSAULTS_STATE, this.getAssaultsState());
            message.addPlaceholder(GamePlaceholder.TIME, "1h30m40s");

            return message.getText();

        }).collect(Collectors.toList());
    }

    @Override
    protected ConfigurationSection getBoardSection() {
        return this.config.getConfigurationSection("game-scoreboard");
    }

    @Override
    public void onUpdate(Attribute attribute) {
        this.update();
    }

    @Override
    public Collection<Attribute> observed() {
        return Arrays.asList(GameAttribute.PVP_STATE, GameAttribute.ASSAULTS_STATE, GameAttribute.TIME_CHANGE);
    }

    private String getPlayerTeam() {

        Team team = this.player.getTeam();

        return team.getDisplayName();
    }

    private String getPvPState() {

        ConfigurationSection section = this.getBoardSection().getConfigurationSection("pvp-state");

        String path = this.game.isPvPEnabled() ? "enabled" : "disabled";

        return TextUtils.parseColors(section.getString(path));
    }

    private String getAssaultsState() {

        ConfigurationSection section = this.getBoardSection().getConfigurationSection("assaults-state");

        String path = this.game.areAssaultsEnabled() ? "enabled" : "disabled";

        return TextUtils.parseColors(section.getString(path));
    }
}
