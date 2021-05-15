package com.github.syr0ws.fallenkingdom.settings.impl;

import com.github.syr0ws.fallenkingdom.settings.Setting;
import com.github.syr0ws.fallenkingdom.settings.SettingFilter;

import java.util.Optional;

public abstract class AbstractSetting<T> implements Setting<T> {

    private final String name;
    private final SettingFilter<T> filter;

    public AbstractSetting(String name, SettingFilter<T> filter) {
        this.name = name;
        this.filter = filter;
    }

    protected void validate(T value) {

        if(this.filter != null && ! this.filter.validate(value))
            throw new IllegalArgumentException("Invalid setting value.");
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Optional<SettingFilter<T>> getFilter() {
        return Optional.of(this.filter);
    }
}
