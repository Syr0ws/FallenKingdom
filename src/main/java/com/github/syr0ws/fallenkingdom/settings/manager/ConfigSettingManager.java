package com.github.syr0ws.fallenkingdom.settings.manager;

import com.github.syr0ws.fallenkingdom.settings.Setting;
import com.github.syr0ws.fallenkingdom.settings.SettingKey;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ConfigSettingManager implements SettingManager {

    private final Map<String, Setting<?>> settings = new HashMap<>();

    @Override
    public void addSetting(String key, Setting<?> setting) {

        if(key == null)
            throw new NullPointerException("Key cannot be null.");

        if(setting == null)
            throw new NullPointerException("Setting cannot be null.");

        this.settings.put(key, setting);
    }

    @Override
    public void addSetting(SettingKey key, Setting<?> setting) {

        if(key == null)
            throw new NullPointerException("SettingKey cannot be null.");

        this.addSetting(key.getKey(), setting);
    }

    @Override
    public void removeSetting(String key) {

        if(key == null)
            throw new NullPointerException("Key cannot be null.");

        this.settings.remove(key);
    }

    @Override
    public void removeSetting(SettingKey key) {

        if(key == null)
            throw new NullPointerException("SettingKey cannot be null.");

        this.removeSetting(key.getKey());
    }

    @Override
    public boolean hasSetting(String key) {

        if(key == null)
            throw new NullPointerException("Key cannot be null.");

        return this.settings.containsKey(key);
    }

    @Override
    public boolean hasSetting(SettingKey key) {

        if(key == null)
            throw new NullPointerException("SettingKey cannot be null.");

        return this.hasSetting(key.getKey());
    }

    @Override
    public <T, S extends Setting<T>> S getSetting(String key, Class<S> clazz) {

        if(!this.hasSetting(key))
            throw new NullPointerException(String.format("No setting found with key '%s'.", key));

        Setting<?> setting = this.settings.get(key);

        if(!clazz.isInstance(setting))
            throw new IllegalArgumentException(String.format("%s is not an instance of %s.", setting.getClass().getName(), clazz.getName()));

        return clazz.cast(setting);
    }

    @Override
    public <T, S extends Setting<T>> S getSetting(SettingKey key, Class<S> clazz) {

        if(key == null)
            throw new NullPointerException("SettingKey cannot be null.");

        return this.getSetting(key.getKey(), clazz);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T, S extends Setting<T>> S getGenericSetting(String key, Class<T> clazz) {

        if(!this.hasSetting(key))
            throw new NullPointerException(String.format("No setting found with key '%s'.", key));

        Setting<?> setting = this.settings.get(key);

        if(!clazz.equals(setting.getValueClass()))
            throw new IllegalArgumentException(String.format("%s cannot be cast to %s.", clazz.getName(), setting.getValueClass().getName()));

        return (S) setting;
    }

    @Override
    public <T, S extends Setting<T>> S getGenericSetting(SettingKey key, Class<T> clazz) {

        if(key == null)
            throw new NullPointerException("SettingKey cannot be null.");

        return this.getGenericSetting(key.getKey(), clazz);
    }

    @Override
    public Collection<Setting<?>> getSettings() {
        return this.settings.values();
    }
}
