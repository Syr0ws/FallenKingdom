package com.github.syr0ws.fallenkingdom.settings.types;

import com.github.syr0ws.fallenkingdom.settings.SettingFilter;
import com.github.syr0ws.fallenkingdom.settings.SettingValidationException;

public class MutableSetting<T> extends AbstractSetting<T> {

    private final T defaultValue;
    private final SettingFilter<T> filter;
    private T value;

    protected MutableSetting(String name, T defaultValue, T value, SettingFilter<T> filter) {
        super(name);

        this.filter = filter;

        this.validate(defaultValue);
        if(value != null) this.validate(value);

        this.defaultValue = defaultValue;
        this.value = value == null ? defaultValue : value;
    }

    private void validate(T value) {

        if(this.filter != null && ! this.filter.filter(value))
            throw new SettingValidationException("Invalid setting value.");
    }

    public void setValue(T value) {
        this.validate(value);
        this.value = value;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public T getValue() {
        return this.value;
    }

    public static class Builder<T> {

        private final String name;
        private final T defaultValue;

        private T value;
        private SettingFilter<T> filter;

        public Builder(String name, T defaultValue) {
            this.name = name;
            this.defaultValue = defaultValue;
        }

        public Builder<T> withValue(T value) {
            this.value = value;
            return this;
        }

        public Builder<T> withFilter(SettingFilter<T> filter) {
            this.filter = filter;
            return this;
        }

        public MutableSetting<T> build() {
            return new MutableSetting<>(this.name, this.defaultValue, this.value, this.filter);
        }

        public String getName() {
            return this.name;
        }

        public T getDefaultValue() {
            return this.defaultValue;
        }

        public T getValue() {
            return this.value;
        }

        public SettingFilter<T> getFilter() {
            return this.filter;
        }
    }
}
