package com.github.syr0ws.fallenkingdom.displays.placeholders.impl;

import com.github.syr0ws.fallenkingdom.displays.placeholders.Placeholder;

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
