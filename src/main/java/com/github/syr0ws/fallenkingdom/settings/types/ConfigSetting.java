package com.github.syr0ws.fallenkingdom.settings.types;

import com.github.syr0ws.fallenkingdom.settings.SettingFilter;
import com.github.syr0ws.fallenkingdom.settings.config.Configurable;

public abstract class ConfigSetting<T> extends MutableSetting<T> implements Configurable {

    private final String path;

    protected ConfigSetting(String name, T defaultValue, T value, SettingFilter<T> filter, String path) {
        super(name, defaultValue, value, filter);
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    public static abstract class Builder<T> extends MutableSetting.Builder<T> {

        private final String path;

        public Builder(String name, T defaultValue, String path) {
            super(name, defaultValue);
            this.path = path;
        }

        public String getPath() {
            return this.path;
        }
    }
}
