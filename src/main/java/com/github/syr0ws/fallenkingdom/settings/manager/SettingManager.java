package com.github.syr0ws.fallenkingdom.settings.manager;

import com.github.syr0ws.fallenkingdom.settings.Setting;
import com.github.syr0ws.fallenkingdom.settings.SettingType;

import java.util.Collection;

public interface SettingManager {

    void addSetting(SettingType type, Setting<?> setting);

    void removeSetting(SettingType type);

    boolean hasSetting(SettingType type);

    <T, S extends Setting<T>> S getSetting(SettingType type, Class<S> clazz);

    <T, S extends Setting<T>> S getGenericSetting(SettingType type, Class<T> clazz);

    Collection<Setting<?>> getSettings();
}
