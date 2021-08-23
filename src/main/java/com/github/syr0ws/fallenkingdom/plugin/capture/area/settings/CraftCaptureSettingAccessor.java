package com.github.syr0ws.fallenkingdom.plugin.capture.area.settings;

import com.github.syr0ws.fallenkingdom.api.capture.area.model.settings.CaptureSettingsAccessor;
import com.github.syr0ws.universe.api.settings.MutableSetting;
import com.github.syr0ws.universe.api.settings.SettingLoader;
import com.github.syr0ws.universe.api.settings.SettingManager;
import com.github.syr0ws.universe.sdk.game.settings.dao.ConfigSettingLoader;
import com.github.syr0ws.universe.sdk.game.settings.manager.CacheSettingManager;
import org.bukkit.configuration.file.FileConfiguration;

public class CraftCaptureSettingAccessor implements CaptureSettingsAccessor {

    private final SettingManager manager;

    public CraftCaptureSettingAccessor(FileConfiguration config) {

        if(config == null)
            throw new IllegalArgumentException("FileConfiguration cannot be null.");

        this.manager = new CacheSettingManager();
        this.loadSettings(config);
    }

    private void loadSettings(FileConfiguration config) {

        for (CaptureSettingEnum value : CaptureSettingEnum.values()) {
            this.manager.addSetting(value, value.getSetting());
        }

        SettingLoader loader = new ConfigSettingLoader(config);
        loader.load(this.manager.getSettings());
    }

    @Override
    public MutableSetting<Integer> getCaptureDurationSetting() {
        return this.manager.getGenericSetting(CaptureSettingEnum.CAPTURE_DURATION_SETTING, Integer.class);
    }
}
