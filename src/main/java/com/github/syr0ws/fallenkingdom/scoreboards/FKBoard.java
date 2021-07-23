package com.github.syr0ws.fallenkingdom.scoreboards;

import com.github.syr0ws.universe.commons.modules.lang.LangService;
import com.github.syr0ws.universe.commons.modules.lang.messages.impl.Text;
import com.github.syr0ws.universe.commons.modules.lang.messages.impl.TextList;
import com.github.syr0ws.universe.commons.modules.scoreboard.ScoreboardManager;
import com.github.syr0ws.universe.commons.modules.scoreboard.impl.AbstractScoreboard;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.stream.Collectors;

public abstract class FKBoard extends AbstractScoreboard {

    private final LangService service;
    private FastBoard board;

    public FKBoard(ScoreboardManager manager, Player player, LangService service) {
        super(manager, player);

        if(service == null)
            throw new IllegalArgumentException("LangService cannot be null.");

        this.service = service;
    }

    protected abstract String getSectionName();

    protected abstract String parse(String text);

    @Override
    public void set() {
        super.set();
        this.board = new FastBoard(this.getPlayer());
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

    private Collection<String> getLines() {

        String key = this.getSectionName() + ".lines";
        TextList list = this.service.getMessage(key, TextList.class);

        return list.getList().stream()
                .map(this::parse)
                .collect(Collectors.toList());
    }

    private String getTitle() {

        String key = this.getSectionName() + ".title";
        Text text = this.service.getMessage(key, Text.class);

        String title = text.getText();

        return this.parse(title);
    }

    public LangService getLangService() {
        return this.service;
    }
}
