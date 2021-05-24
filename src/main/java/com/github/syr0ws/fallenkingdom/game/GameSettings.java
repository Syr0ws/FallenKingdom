package com.github.syr0ws.fallenkingdom.game;

import com.github.syr0ws.fallenkingdom.settings.Setting;
import com.github.syr0ws.fallenkingdom.settings.SettingKey;
import com.github.syr0ws.fallenkingdom.settings.impl.LocationSetting;
import com.github.syr0ws.fallenkingdom.settings.impl.MaterialSetting;
import com.github.syr0ws.fallenkingdom.settings.impl.SimpleConfigSetting;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public enum GameSettings implements SettingKey {

    PVP_ACTIVATION_TIME(
            new SimpleConfigSetting<>("pvp-activation-time", 1800, Integer.class, time -> time > 0)),

    ASSAULTS_ACTIVATION_TIME(
            new SimpleConfigSetting<>("assaults-activation-time", 3600, Integer.class, time -> time > 0)),

    MAX_GAME_DURATION(
            new SimpleConfigSetting<>("max-game-duration", 10800, Integer.class, duration -> duration > 0)),

    ALLOWED_BLOCKS(
            new MaterialSetting("allowed-blocks", new ArrayList<>())),

    VAULT_BLOCKS(
            new MaterialSetting("vault-blocks", Arrays.asList(Material.CHEST, Material.ENDER_CHEST))
    ),

    SPAWN_LOCATION(
            new LocationSetting("game-spawn", Objects::nonNull)
    ),

    MAX_PLAYERS(
            new SimpleConfigSetting<>("max-players", 8, Integer.class, players -> players >= 2)
    );

    private final Setting<?> setting;

    GameSettings(Setting<?> setting) {
        this.setting = setting;
    }

    public Setting<?> getSetting() {
        return this.setting;
    }

    @Override
    public String getKey() {
        return this.name();
    }
}
