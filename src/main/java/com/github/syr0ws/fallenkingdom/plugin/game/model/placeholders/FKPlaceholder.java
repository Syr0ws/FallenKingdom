package com.github.syr0ws.fallenkingdom.plugin.game.model.placeholders;

import com.github.syr0ws.universe.sdk.placeholders.Placeholder;

public enum FKPlaceholder implements Placeholder {

    GAME_STATE("game_state"),
    PVP_STATE("pvp_state"),
    ASSAULTS_STATE("assaults_state"),
    NETHER_STATE("nether_state"),
    END_STATE("end_state"),
    TIME("time"),
    KILLS("kills"),
    DEATHS("deaths"),
    KDR("kdr"),
    ONLINE_PLAYERS("online_players"),
    MAX_PLAYERS("max_players"),
    TEAM_NAME("team_name"),
    CATCHER_TEAM_NAME("catcher_team_name"),
    CAPTURED_TEAM_NAME("captured_team_name"),
    CENTER_DIRECTION("center_direction"),
    BASE_DIRECTION("base_direction");

    private final String placeholder;

    FKPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public String get() {
        return "%" + this.placeholder + "%";
    }
}
