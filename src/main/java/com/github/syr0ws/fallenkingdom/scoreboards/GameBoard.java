package com.github.syr0ws.fallenkingdom.scoreboards;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.GameAttribute;
import com.github.syr0ws.fallenkingdom.game.model.placeholders.FKPlaceholder;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeamPlayer;
import com.github.syr0ws.fallenkingdom.tools.PeriodFormatter;
import com.github.syr0ws.universe.attributes.Attribute;
import com.github.syr0ws.universe.attributes.AttributeObserver;
import com.github.syr0ws.universe.displays.impl.Message;
import com.github.syr0ws.universe.modules.scoreboard.ScoreboardManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public class GameBoard extends FKBoard implements AttributeObserver {

    private static final String SCOREBOARD_SECTION = "game-scoreboard";

    private final FKGame game;
    private final FKModel model;
    private final FKTeamPlayer teamPlayer;
    private final PeriodFormatter formatter;

    public GameBoard(ScoreboardManager manager, Player player, FKGame game) {
        super(manager, player);

        if(game == null)
            throw new IllegalArgumentException("FKGame cannot be null.");

        this.game = game;
        this.model = game.getGameModel();
        this.formatter = new PeriodFormatter(this.getScoreboardSection().getConfigurationSection("time"));

        Optional<? extends FKTeamPlayer> optional = this.model.getTeamPlayer(player.getUniqueId());

        if(!optional.isPresent())
            throw new NullPointerException("FKTeamPlayer not found.");

        this.teamPlayer = optional.get();
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

        String timeFormat = this.getScoreboardSection().getString("time.format", "");
        String time = this.formatter.format(timeFormat, this.model.getTime());

        Message message = new Message(text);

        message.addPlaceholder(FKPlaceholder.TEAM_NAME, this.teamPlayer.getTeam().getDisplayName());
        message.addPlaceholder(FKPlaceholder.PVP_STATE, this.getPvPState());
        message.addPlaceholder(FKPlaceholder.ASSAULTS_STATE, this.getAssaultsState());
        message.addPlaceholder(FKPlaceholder.KILLS, Integer.toString(this.teamPlayer.getKills()));
        message.addPlaceholder(FKPlaceholder.DEATHS, Integer.toString(this.teamPlayer.getDeaths()));
        message.addPlaceholder(FKPlaceholder.KDR, Double.toString(this.teamPlayer.getKDR()));
        message.addPlaceholder(FKPlaceholder.TIME, time);

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

    private String getPvPState() {

        ConfigurationSection section = this.getScoreboardSection().getConfigurationSection("pvp-state");

        String path = this.model.isPvPEnabled() ? "enabled" : "disabled";

        return section.getString(path, "");
    }

    private String getAssaultsState() {

        ConfigurationSection section = this.getScoreboardSection().getConfigurationSection("assaults-state");

        String path = this.model.areAssaultsEnabled() ? "enabled" : "disabled";

        return section.getString(path, "");
    }
}
