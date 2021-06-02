package com.github.syr0ws.fallenkingdom.settings.types;

public class ImmutableSetting<T> extends AbstractSetting<T> {

    private final T value;

    public ImmutableSetting(String name, T value) {
        super(name);
        this.value = value;
    }

    @Override
    public T getValue() {
        return this.value;
    }
}
