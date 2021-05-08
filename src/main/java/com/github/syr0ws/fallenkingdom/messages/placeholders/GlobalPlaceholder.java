package com.github.syr0ws.fallenkingdom.messages.placeholders;

import com.github.syr0ws.fallenkingdom.messages.Placeholder;

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
