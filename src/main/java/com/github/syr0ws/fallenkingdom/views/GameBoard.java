package com.github.syr0ws.fallenkingdom.views;

import com.github.syr0ws.fallenkingdom.attributes.Attribute;
import com.github.syr0ws.fallenkingdom.attributes.AttributeObserver;
import com.github.syr0ws.fallenkingdom.display.placeholders.GamePlaceholder;
import com.github.syr0ws.fallenkingdom.display.placeholders.TeamPlaceholder;
import com.github.syr0ws.fallenkingdom.display.types.Message;
import com.github.syr0ws.fallenkingdom.game.model.GameAttribute;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.utils.TextUtils;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class GameBoard extends FastBoard implements Scoreboard, AttributeObserver {

    private final Plugin plugin;
    private final GameModel model;

    public GameBoard(Player player, Plugin plugin, GameModel model) {
        super(player);
        this.plugin = plugin;
        this.model = model;
    }

    @Override
    public void update() {

        FileConfiguration config = this.plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection("running-cycle.scoreboard");

        String title = TextUtils.parseColors(section.getString("title"));
        List<String> lines = section.getStringList("lines");

        lines = lines.stream().map(line -> {

            Message message = new Message(line);
            message.addPlaceholder(TeamPlaceholder.TEAM_NAME, this.getPlayerTeam());
            message.addPlaceholder(GamePlaceholder.PVP_STATE, this.getPvPState());
            message.addPlaceholder(GamePlaceholder.ASSAULTS_STATE, this.getAssaultsState());
            message.addPlaceholder(GamePlaceholder.TIME, this.getGameTime());

            return message.getText();

        }).collect(Collectors.toList());

        this.updateTitle(title);
        this.updateLines(lines);
    }

    @Override
    public void onUpdate(Attribute attribute) {
        this.update();
    }

    @Override
    public Collection<Attribute> observed() {
        return Arrays.asList(GameAttribute.PVP_STATE, GameAttribute.ASSAULTS_STATE);
    }

    private String getPvPState() {

        ConfigurationSection section = this.getBoardSection().getConfigurationSection("pvp-state");

        String path = this.model.isPvPEnabled() ? "enabled" : "disabled";

        return TextUtils.parseColors(section.getString(path));
    }

    private String getAssaultsState() {

        ConfigurationSection section = this.getBoardSection().getConfigurationSection("assaults-state");

        String path = this.model.areAssaultsEnabled() ? "enabled" : "disabled";

        return TextUtils.parseColors(section.getString(path));
    }

    private String getPlayerTeam() {

        // Players with this scoreboard will always have a team.
        Team team = this.model.getTeam(super.getPlayer()).get();

        return team.getDisplayName();
    }

    private String getGameTime() {
        return "1min30s";
    }

    private ConfigurationSection getBoardSection() {
        FileConfiguration config = this.plugin.getConfig();
        return config.getConfigurationSection("running-cycle.scoreboard");
    }
}
