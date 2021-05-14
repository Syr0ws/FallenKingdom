package com.github.syr0ws.fallenkingdom.listeners;

import com.github.syr0ws.fallenkingdom.game.controller.GameController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.Plugin;

public class GlobalListener implements Listener {

    private final Plugin plugin;
    private final GameController controller;

    public GlobalListener(Plugin plugin, GameController controller) {
        this.plugin = plugin;
        this.controller = controller;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        this.controller.onJoin(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        this.controller.onQuit(player);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {

        // TODO Set this configurable.
        event.setCancelled(true);
    }
}
