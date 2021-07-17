package com.github.syr0ws.fallenkingdom.capture.area.settings;

import com.github.syr0ws.universe.settings.dao.ConfigSettingLoader;
import com.github.syr0ws.universe.settings.dao.SettingLoader;
import com.github.syr0ws.universe.settings.manager.CacheSettingManager;
import com.github.syr0ws.universe.settings.manager.SettingManager;
import com.github.syr0ws.universe.settings.types.MutableSetting;
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
