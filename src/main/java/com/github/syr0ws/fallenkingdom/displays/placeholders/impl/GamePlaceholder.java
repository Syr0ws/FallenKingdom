package com.github.syr0ws.fallenkingdom.displays.placeholders.impl;

import com.github.syr0ws.fallenkingdom.displays.placeholders.Placeholder;

public enum GamePlaceholder implements Placeholder {

    PVP_STATE("pvp_state"),
    ASSAULTS_STATE("assaults_state"),
    TIME("time"),
    ONLINE_PLAYERS("online_players"),
    MAX_PLAYERS("max_players");

    private final String placeholder;

    GamePlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public String get() {
        return "%" + this.placeholder + "%";
    }
}
