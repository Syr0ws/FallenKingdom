package com.github.syr0ws.fallenkingdom.game;

import com.github.syr0ws.fallenkingdom.settings.Setting;
import com.github.syr0ws.fallenkingdom.settings.impl.LocationSetting;
import com.github.syr0ws.fallenkingdom.settings.impl.MaterialSetting;
import com.github.syr0ws.fallenkingdom.settings.impl.SimpleConfigSetting;

import java.util.ArrayList;
import java.util.Objects;

public enum GameSettings {

    PVP_ACTIVATION_TIME(new SimpleConfigSetting<>("pvp-activation-time", 1800, Integer.class, time -> time > 0)),
    ASSAULTS_ACTIVATION_TIME(new SimpleConfigSetting<>("assaults-activation-time", 3600, Integer.class, time -> time > 0)),
    ALLOWED_BLOCKS(new MaterialSetting("allowed-blocks", new ArrayList<>())),
    SPAWN_LOCATION(new LocationSetting("spawn-location", Objects::nonNull));

    private final Setting<?> setting;

    GameSettings(Setting<?> setting) {
        this.setting = setting;
    }

    public Setting<?> getSetting() {
        return this.setting;
    }
}
