package com.github.syr0ws.fallenkingdom.capture.area.settings;

import com.github.syr0ws.universe.sdk.settings.types.MutableSetting;

public interface CaptureSettingsAccessor {

    MutableSetting<Integer> getCaptureDurationSetting();
}
