package com.github.syr0ws.fallenkingdom.game;

import com.github.syr0ws.fallenkingdom.settings.Setting;
import com.github.syr0ws.fallenkingdom.settings.SettingKey;
import com.github.syr0ws.fallenkingdom.settings.impl.SimpleConfigSetting;

public enum GeneralSettings implements SettingKey {

    WEATHER_CHANGE(new SimpleConfigSetting<>("weather-change", false, Boolean.class));
    // WEATHER(new SimpleConfigSetting<>("default-weather", Weather.SUNNY, Weather.class));

    private final Setting<?> setting;

    GeneralSettings(Setting<?> setting) {
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
