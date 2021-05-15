package com.github.syr0ws.fallenkingdom.settings.impl;

public class ImmutableSetting<T> extends AbstractSetting<T> {

    private final T value;

    public ImmutableSetting(String name, T value) {
        super(name, null);
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
