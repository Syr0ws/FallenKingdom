package com.github.syr0ws.fallenkingdom.plugin.game.view.displays;

public enum GameDisplayEnum {

    TEAM_ADD("team.add"),
    TEAM_REMOVE("team.remove"),
    PVP_ENABLED("pvp.enabled"),
    PVP_DISABLED("pvp.disabled"),
    ASSAULTS_ENABLED("assaults.enabled"),
    ASSAULTS_DISABLED("assaults.disabled"),
    NETHER_ENABLED("nether.enabled"),
    NETHER_DISABLED("nether.disabled"),
    END_ENABLED("end.enabled"),
    END_DISABLED("end.disabled"),
    WIN_ALL("win.all"),
    WIN_SELF("win.self"),
    WIN_OTHER("win.other");

    private final String path;

    GameDisplayEnum(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }
}
