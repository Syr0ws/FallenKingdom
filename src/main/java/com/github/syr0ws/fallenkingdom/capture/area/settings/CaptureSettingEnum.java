package com.github.syr0ws.fallenkingdom.capture.area.settings;

import com.github.syr0ws.universe.sdk.settings.Setting;
import com.github.syr0ws.universe.sdk.settings.SettingType;
import com.github.syr0ws.universe.sdk.settings.types.SimpleConfigSetting;

public enum CaptureSettingEnum implements SettingType {

    CAPTURE_DURATION_SETTING {
        @Override
        public Setting<?> getSetting() {
            return new SimpleConfigSetting
                    .Builder<>("captureDuration", 60, CAPTURE_SETTING_SECTION + ".capture-duration", Integer.class)
                    .withFilter(time -> time >= 0)
                    .build();
        }
    };

    public static final String CAPTURE_SETTING_SECTION = "area-capture";

    public abstract Setting<?> getSetting();
}
