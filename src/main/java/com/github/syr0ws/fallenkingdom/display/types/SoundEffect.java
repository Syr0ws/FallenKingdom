package com.github.syr0ws.fallenkingdom.display.types;

import com.github.syr0ws.fallenkingdom.display.Display;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class SoundEffect implements Display {

    private final Sound sound;
    private final int volume;
    private final float pitch;

    public SoundEffect(org.bukkit.Sound sound, int volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public SoundEffect(ConfigurationSection section) {
        this.sound = Sound.valueOf(section.getString("sound"));
        this.volume = section.getInt("volume", 1);
        this.pitch = (float) section.getDouble("pitch", 1);
    }

    @Override
    public void displayTo(Player player) {
        player.playSound(player.getLocation(), this.sound, this.volume, this.pitch);
    }

    @Override
    public void displayAll() {
        Bukkit.getOnlinePlayers().forEach(this::displayTo);
    }
}
