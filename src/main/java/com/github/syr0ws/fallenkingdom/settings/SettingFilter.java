package com.github.syr0ws.fallenkingdom.settings;

@FunctionalInterface
public interface SettingFilter<T> {

    boolean validate(T value);
}
