package com.github.syr0ws.fallenkingdom.game;

import com.github.syr0ws.fallenkingdom.settings.Setting;
import com.github.syr0ws.fallenkingdom.settings.SettingKey;
import com.github.syr0ws.fallenkingdom.settings.impl.SimpleConfigSetting;

public enum GameRule implements SettingKey {

    FRIENDLY_FIRE(
            new SimpleConfigSetting<>("friendly-fire", false, Boolean.class)
    );

    private final Setting<?> setting;

    GameRule(Setting<?> setting) {
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
