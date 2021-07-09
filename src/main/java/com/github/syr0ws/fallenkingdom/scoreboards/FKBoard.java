package com.github.syr0ws.fallenkingdom.scoreboards;

import com.github.syr0ws.universe.displays.impl.Message;
import com.github.syr0ws.universe.modules.scoreboard.ScoreboardManager;
import com.github.syr0ws.universe.modules.scoreboard.impl.AbstractScoreboard;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class FKBoard extends AbstractScoreboard {

    private final Player player;
    private FastBoard board;

    public FKBoard(ScoreboardManager manager, Player player) {
        super(manager, player);
        this.player = player;
    }

    protected abstract ConfigurationSection getScoreboardSection();

    @Override
    public void set() {
        super.set();
        this.board = new FastBoard(this.player);
        this.update();
    }

    @Override
    public void remove() {
        super.remove();
        this.board.delete();
    }

    @Override
    public void update() {

        String title = this.getTitle();
        Collection<String> lines = this.getLines();

        this.board.updateTitle(title);
        this.board.updateLines(lines);
    }

    protected String parse(String text) {
        return new Message(text).getText();
    }

    private Collection<String> getLines() {

        ConfigurationSection section = this.getScoreboardSection();

        if(section == null)
            throw new NullPointerException("Scoreboard section cannot be null.");

        List<String> lines = section.getStringList("lines");

        return lines.stream()
                .map(this::parse)
                .collect(Collectors.toList());
    }

    private String getTitle() {

        ConfigurationSection section = this.getScoreboardSection();

        if(section == null)
            throw new NullPointerException("Scoreboard section cannot be null.");

        String title = section.getString("title", "");

        return this.parse(title);
    }
}
