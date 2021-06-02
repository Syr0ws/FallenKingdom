package com.github.syr0ws.fallenkingdom.settings.types;

import com.github.syr0ws.fallenkingdom.settings.SettingFilter;
import org.bukkit.configuration.ConfigurationSection;

public class SimpleConfigSetting<T> extends ConfigSetting<T> {

    private final Class<T> clazz;

    protected SimpleConfigSetting(String name, T defaultValue, T value, SettingFilter<T> filter, String path, Class<T> clazz) {
        super(name, defaultValue, value, filter, path);
        this.clazz = clazz;
    }

    public void read(ConfigurationSection section) {
        Object object = section.get(super.getPath());
        super.setValue(this.clazz.cast(object));
    }

    public Class<T> getValueClass() {
        return this.clazz;
    }

    public static class Builder<T> extends ConfigSetting.Builder<T> {

        private final Class<T> clazz;

        public Builder(String name, T defaultValue, String path, Class<T> clazz) {
            super(name, defaultValue, path);
            this.clazz = clazz;
        }

        @Override
        public SimpleConfigSetting<T> build() {
            return new SimpleConfigSetting<>(
                    super.getName(),
                    super.getDefaultValue(),
                    super.getValue(),
                    super.getFilter(),
                    super.getPath(),
                    this.clazz
            );
        }
    }
}
