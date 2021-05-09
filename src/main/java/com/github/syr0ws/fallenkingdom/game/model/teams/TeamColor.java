package com.github.syr0ws.fallenkingdom.game.model.teams;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Optional;

public enum TeamColor {

    BLUE(ChatColor.BLUE), RED(ChatColor.RED), YELLOW(ChatColor.YELLOW), GREEN(ChatColor.GREEN);

    private final ChatColor chatColor;

    TeamColor(ChatColor chatColor) {
        this.chatColor = chatColor;
    }

    public ChatColor getChatColor() {
        return this.chatColor;
    }

    public static Optional<TeamColor> getByName(String name) {
        return Arrays.stream(values())
                .filter(color -> color.name().equals(name))
                .findFirst();
    }
}
