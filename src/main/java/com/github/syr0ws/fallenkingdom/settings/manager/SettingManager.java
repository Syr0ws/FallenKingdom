package com.github.syr0ws.fallenkingdom.settings.manager;

import com.github.syr0ws.fallenkingdom.settings.Setting;
import com.github.syr0ws.fallenkingdom.settings.SettingKey;

import java.util.Collection;

public interface SettingManager {

    void addSetting(String key, Setting<?> setting);

    void addSetting(SettingKey key, Setting<?> setting);

    void removeSetting(String key);

    void removeSetting(SettingKey key);

    boolean hasSetting(String key);

    boolean hasSetting(SettingKey key);

    <T, S extends Setting<T>> S getSetting(String key, Class<S> clazz);

    <T, S extends Setting<T>> S getSetting(SettingKey key, Class<S> clazz);

    <T, S extends Setting<T>> S getGenericSetting(String key, Class<T> clazz);

    <T, S extends Setting<T>> S getGenericSetting(SettingKey key, Class<T> clazz);

    Collection<Setting<?>> getSettings();
}
