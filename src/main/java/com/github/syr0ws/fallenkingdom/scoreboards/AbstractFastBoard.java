package com.github.syr0ws.fallenkingdom.scoreboards;

import com.github.syr0ws.fallenkingdom.utils.TextUtils;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public abstract class AbstractFastBoard implements Scoreboard {

    private final FastBoard board;

    public AbstractFastBoard(Player player) {

        if(player == null)
            throw new IllegalArgumentException("Player cannot be null.");

        this.board = new FastBoard(player);
    }

    protected abstract Collection<String> parseLines(Collection<String> lines);

    protected abstract ConfigurationSection getBoardSection();

    @Override
    public void update() {

        ConfigurationSection section = this.getBoardSection();

        String title = TextUtils.parseColors(section.getString("title", ""));
        List<String> lines = section.getStringList("lines");

        Collection<String> parsed = this.parseLines(lines);

        this.board.updateTitle(title);
        this.board.updateLines(parsed);
    }

    @Override
    public void delete() {
        this.board.delete();
    }
}
