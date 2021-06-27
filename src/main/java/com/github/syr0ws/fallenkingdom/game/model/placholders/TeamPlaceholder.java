package com.github.syr0ws.fallenkingdom.game.model.placholders;

import com.github.syr0ws.universe.displays.placeholders.Placeholder;

public enum TeamPlaceholder implements Placeholder {

    TEAM_NAME("team_name");

    private final String placeholder;

    TeamPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public String get() {
        return "%" + this.placeholder + "%";
    }
}
