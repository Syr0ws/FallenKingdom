package com.github.syr0ws.fallenkingdom.settings.manager;

import com.github.syr0ws.fallenkingdom.settings.Setting;
import com.github.syr0ws.fallenkingdom.settings.SettingType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CacheSettingManager implements SettingManager {

    private final Map<SettingType, Setting<?>> settings = new HashMap<>();

    @Override
    public void addSetting(SettingType type, Setting<?> setting) {

        if(type == null)
            throw new NullPointerException("Key cannot be null.");

        if(setting == null)
            throw new NullPointerException("Setting cannot be null.");

        this.settings.put(type, setting);
    }

    @Override
    public void removeSetting(SettingType type) {

        if(type == null)
            throw new NullPointerException("SettingKey cannot be null.");

        this.settings.remove(type);
    }

    @Override
    public boolean hasSetting(SettingType type) {

        if(type == null)
            throw new NullPointerException("SettingKey cannot be null.");

        return this.settings.containsKey(type);
    }

    @Override
    public <T, S extends Setting<T>> S getSetting(SettingType type, Class<S> clazz) {

        if(!this.hasSetting(type))
            throw new NullPointerException(String.format("No setting found with key '%s'.", type));

        Setting<?> setting = this.settings.get(type);

        if(!clazz.isInstance(setting))
            throw new IllegalArgumentException(String.format("%s is not an instance of %s.", setting.getClass().getName(), clazz.getName()));

        return clazz.cast(setting);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T, S extends Setting<T>> S getGenericSetting(SettingType type, Class<T> clazz) {

        if(!this.hasSetting(type))
            throw new NullPointerException(String.format("No setting found with key '%s'.", type));

        Setting<?> setting = this.settings.get(type);

        Class<?> valueClass = setting.getValue().getClass();

        if(!clazz.equals(setting.getValue().getClass()))
            throw new IllegalArgumentException(String.format("%s cannot be cast to %s.", clazz.getName(), valueClass));

        return (S) setting;
    }

    @Override
    public Collection<Setting<?>> getSettings() {
        return this.settings.values();
    }
}
