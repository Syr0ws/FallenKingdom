package com.github.syr0ws.fallenkingdom.views;

import org.bukkit.entity.Player;

import java.util.Optional;

public interface ScoreboardManager {

    void addScoreboard(Player player, Scoreboard board);

    Scoreboard removeScoreboard(Player player);

    void updateScoreboards();

    boolean hasScoreboard(Player player);

    Optional<Scoreboard> getScoreboard(Player player);
}
