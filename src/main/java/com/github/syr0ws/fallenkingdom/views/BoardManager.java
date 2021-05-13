package com.github.syr0ws.fallenkingdom.views;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BoardManager implements ScoreboardManager {

    private final Map<Player, Scoreboard> boards = new HashMap<>();

    @Override
    public void addScoreboard(Player player, Scoreboard board) {

        if(this.hasScoreboard(player)) this.removeScoreboard(player);

        this.boards.put(player, board);
    }

    @Override
    public Scoreboard removeScoreboard(Player player) {
        return this.boards.remove(player);
    }

    @Override
    public void updateScoreboards() {
        this.boards.values().forEach(Scoreboard::update);
    }

    @Override
    public boolean hasScoreboard(Player player) {
        return this.boards.containsKey(player);
    }

    @Override
    public Optional<Scoreboard> getScoreboard(Player player) {
        return Optional.ofNullable(this.boards.getOrDefault(player, null));
    }
}
