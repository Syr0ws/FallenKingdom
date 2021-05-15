package com.github.syr0ws.fallenkingdom.settings.impl;

import com.github.syr0ws.fallenkingdom.settings.Readable;
import com.github.syr0ws.fallenkingdom.settings.SettingFilter;
import org.bukkit.configuration.ConfigurationSection;

public class SimpleConfigSetting<T> extends MutableSetting<T> implements Readable {

    private final Class<T> clazz;

    public SimpleConfigSetting(String name, T defaultValue, Class<T> clazz) {
        super(name, defaultValue, null);
        this.clazz = clazz;
    }

    public SimpleConfigSetting(String name, T defaultValue, Class<T> clazz, SettingFilter<T> filter) {
        super(name, defaultValue, filter);
        this.clazz = clazz;
    }

    public SimpleConfigSetting(String name, T value, T defaultValue, Class<T> clazz, SettingFilter<T> filter) {
        super(name, value, defaultValue, filter);
        this.clazz = clazz;
    }

    public void read(ConfigurationSection section, String key) {
        super.setValue(section.getObject(key, this.clazz));
    }
}
