package com.github.syr0ws.fallenkingdom.messages.types;

import com.github.syr0ws.fallenkingdom.messages.TextMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class SimpleMessage extends TextMessage {

    private final String text;

    public SimpleMessage(String text) {
        this.text = text;
    }

    public SimpleMessage(ConfigurationSection section) {
        this.text = section.getString("text");
    }

    public void send(CommandSender sender) {
        String message = super.format(this.text);
        sender.sendMessage(message);
    }

    @Override
    public void send(Player player) {
        String message = super.format(this.text);
        player.sendMessage(message);
    }

    @Override
    public void broadcast() {
        String message = super.format(this.text);
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(message));
    }

    public String getText() {
        return super.format(this.text);
    }
}
