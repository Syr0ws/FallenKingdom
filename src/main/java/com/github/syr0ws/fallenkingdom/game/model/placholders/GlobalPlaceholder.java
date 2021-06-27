package com.github.syr0ws.fallenkingdom.game.model.placholders;

import com.github.syr0ws.universe.displays.placeholders.Placeholder;

public enum GlobalPlaceholder implements Placeholder {

    PLAYER_NAME("player"), DISPLAY_NAME("display_name"), MESSAGE("message");

    private final String placeholder;

    GlobalPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public String get() {
        return "%" + this.placeholder + "%";
    }
}
