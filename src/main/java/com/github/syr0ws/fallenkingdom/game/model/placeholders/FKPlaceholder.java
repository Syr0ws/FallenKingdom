package com.github.syr0ws.fallenkingdom.game.model.placeholders;

import com.github.syr0ws.universe.placeholders.Placeholder;

public enum FKPlaceholder implements Placeholder {

    GAME_STATE("game_state"),
    PVP_STATE("pvp_state"),
    ASSAULTS_STATE("assaults_state"),
    TIME("time"),
    KILLS("kills"),
    DEATHS("deaths"),
    KDR("kdr"),
    ONLINE_PLAYERS("online_players"),
    MAX_PLAYERS("max_players"),
    TEAM_NAME("team_name");

    private final String placeholder;

    FKPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public String get() {
        return "%" + this.placeholder + "%";
    }
}
