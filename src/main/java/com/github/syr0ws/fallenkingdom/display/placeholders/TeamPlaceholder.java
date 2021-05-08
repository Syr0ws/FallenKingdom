package com.github.syr0ws.fallenkingdom.display.placeholders;

public enum TeamPlaceholder implements Placeholder {

    TEAM_NAME("team_name"), TEAM_COLOR("team_color");

    private final String placeholder;

    TeamPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public String get() {
        return "%" + this.placeholder + "%";
    }
}
