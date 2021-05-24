package com.github.syr0ws.fallenkingdom.displays.placeholders.impl;

import com.github.syr0ws.fallenkingdom.displays.placeholders.Placeholder;

public enum GlobalPlaceholder implements Placeholder {

    PLAYER_NAME("player");

    private final String placeholder;

    GlobalPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public String get() {
        return "%" + this.placeholder + "%";
    }
}
