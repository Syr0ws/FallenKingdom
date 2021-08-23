package com.github.syr0ws.fallenkingdom.capture.area.settings;

import com.github.syr0ws.universe.api.settings.MutableSetting;

public interface CaptureSettingsAccessor {

    MutableSetting<Integer> getCaptureDurationSetting();
}
