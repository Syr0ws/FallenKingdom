package com.github.syr0ws.fallenkingdom.scoreboards;

import org.bukkit.entity.Player;

import java.util.*;

public class ScoreboardManager {

    private final Map<Player, Scoreboard> boards = new HashMap<>();

    public void updateScoreboards() {
        this.boards.values().forEach(Scoreboard::update);
    }

    public void addScoreboard(Player player, Scoreboard scoreboard) {

        if(this.hasScoreboard(player))
            throw new UnsupportedOperationException("Player already has a scoreboard. Remove it first.");

        this.boards.put(player, scoreboard);

        scoreboard.update();
    }

    public void removeScoreboard(Player player) {

        if(!this.hasScoreboard(player))
            throw new UnsupportedOperationException("Player doesn't have scoreboard.");

        Scoreboard board = this.boards.remove(player);
        board.delete();
    }

    public boolean hasScoreboard(Player player) {
        return this.boards.containsKey(player);
    }

    public Optional<Scoreboard> getScoreboard(Player player) {
        return Optional.ofNullable(this.boards.get(player));
    }

    public Collection<Scoreboard> getScoreboards() {
        return Collections.unmodifiableCollection(this.boards.values());
    }
}
