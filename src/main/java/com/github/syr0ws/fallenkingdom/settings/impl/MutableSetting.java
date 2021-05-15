package com.github.syr0ws.fallenkingdom.settings.impl;

import com.github.syr0ws.fallenkingdom.settings.SettingFilter;

public class MutableSetting<T> extends AbstractSetting<T> {

    private final T defaultValue;
    private T value;

    public MutableSetting(String name, T defaultValue) {
        super(name, null);
        this.defaultValue = defaultValue;
    }

    public MutableSetting(String name, T defaultValue, SettingFilter<T> filter) {
        super(name, filter);

        this.validate(defaultValue);

        this.value = defaultValue;
        this.defaultValue = defaultValue;
    }

    public MutableSetting(String name, T value, T defaultValue, SettingFilter<T> filter) {
        super(name, filter);

        this.validate(value);
        this.validate(defaultValue);

        this.value = value;
        this.defaultValue = defaultValue;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public void setValue(T value) {
        this.validate(value);
        this.value = value;
    }

    @Override
    public T getValue() {
        return this.value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<T> getValueClass() {
        return (Class<T>) this.value.getClass();
    }
}
