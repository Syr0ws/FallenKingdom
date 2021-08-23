package com.github.syr0ws.fallenkingdom.views.scoreboards;

import com.github.syr0ws.universe.sdk.modules.lang.LangService;
import com.github.syr0ws.universe.sdk.modules.lang.messages.impl.Text;
import com.github.syr0ws.universe.sdk.modules.lang.messages.impl.TextList;
import com.github.syr0ws.universe.sdk.modules.view.views.ScoreboardView;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.stream.Collectors;

public abstract class FKBoard extends ScoreboardView {

    private final Player player;
    private final LangService service;
    private FastBoard board;

    public FKBoard(Player player, LangService service) {

        if(player == null)
            throw new IllegalArgumentException("Player cannot be null.");

        if(service == null)
            throw new IllegalArgumentException("LangService cannot be null.");

        this.player = player;
        this.service = service;
    }

    protected abstract String parse(String text);

    protected abstract String getSectionName();

    @Override
    public void set() {
        this.board = new FastBoard(this.player);
        this.update();
    }

    @Override
    public void remove() {
        this.board.delete();
    }

    @Override
    public void update() {

        String title = this.getTitle();
        Collection<String> lines = this.getLines();

        this.board.updateTitle(title);
        this.board.updateLines(lines);
    }

    @Override
    public boolean isUpdatable() {
        return false;
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
