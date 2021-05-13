package com.github.syr0ws.fallenkingdom.display.placeholders;

public enum GamePlaceholder implements Placeholder {

    PVP_STATE("pvp_state"), ASSAULTS_STATE("assaults_state"), TIME("time");

    private final String placeholder;

    GamePlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public String get() {
        return "%" + this.placeholder + "%";
    }
}
