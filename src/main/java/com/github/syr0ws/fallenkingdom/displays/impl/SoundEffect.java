package com.github.syr0ws.fallenkingdom.displays.impl;

import com.github.syr0ws.fallenkingdom.displays.Display;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundEffect implements Display {

    private final Sound sound;
    private final int volume;
    private final float pitch;

    public SoundEffect(Sound sound, int volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public void displayTo(Player player) {
        player.playSound(player.getLocation(), this.sound, this.volume, this.pitch);
    }

    @Override
    public void displayAll() {
        Bukkit.getOnlinePlayers().forEach(this::displayTo);
    }

    public Sound getSound() {
        return this.sound;
    }

    public int getVolume() {
        return this.volume;
    }

    public float getPitch() {
        return this.pitch;
    }
}
