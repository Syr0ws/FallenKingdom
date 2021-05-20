package com.github.syr0ws.fallenkingdom.displays.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class NewTitle extends Title {

    public NewTitle(String title, String subtitle) {
        super(title, subtitle);
    }

    public NewTitle(String title, String subtitle, int fadeIn, int fadeOut, int stay) {
        super(title, subtitle, fadeIn, fadeOut, stay);
    }

    @Override
    public void displayTo(Player player) {

        String title = super.getTitle();
        String subtitle = super.getSubtitle();

        int fadeIn = super.getFadeIn();
        int fadeOut = super.getFadeOut();
        int stay = super.getStay();

        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    @Override
    public void displayAll() {
        Bukkit.getOnlinePlayers().forEach(this::displayTo);
    }
}
