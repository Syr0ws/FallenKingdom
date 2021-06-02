package com.github.syr0ws.fallenkingdom.settings;

public interface Setting<T> {

    T getValue();

    String getName();
}
