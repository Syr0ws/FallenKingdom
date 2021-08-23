package com.github.syr0ws.fallenkingdom.api.capture.area.model.settings;

import com.github.syr0ws.universe.api.settings.MutableSetting;

public interface CaptureSettingsAccessor {

    MutableSetting<Integer> getCaptureDurationSetting();
}
