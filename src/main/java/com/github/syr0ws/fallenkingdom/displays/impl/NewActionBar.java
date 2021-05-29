package com.github.syr0ws.fallenkingdom.displays.impl;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class NewActionBar extends ActionBar {

    public NewActionBar(String text) {
        super(text);
    }

    @Override
    public void displayTo(Player player) {
        BaseComponent[] components = TextComponent.fromLegacyText(super.getText());
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, components);
    }

    @Override
    public void displayAll() {
        BaseComponent[] components = TextComponent.fromLegacyText(super.getText());
        Bukkit.getOnlinePlayers().forEach(player -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, components));
    }
}
