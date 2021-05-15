package com.github.syr0ws.fallenkingdom.settings;

import java.util.Optional;

public interface Setting<T> {

    String getName();

    T getValue();

    Class<T> getValueClass();

    Optional<SettingFilter<T>> getFilter();
}
